package fc.doc.plugins.jdbc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import com.sun.javadoc.ClassDoc;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fc.doc.api.Model;
import fc.doc.api.Plugin;
import fc.doc.plugins.jdbc.model.Column;
import fc.doc.plugins.jdbc.model.ForeignKey;
import fc.doc.plugins.jdbc.model.JdbcModel;
import fc.doc.plugins.jdbc.model.Table;

public class JdbcPlugin implements Plugin {

    private static final String INMEMORY_DB_JDBC_URL = "jdbc:h2:./test;MODE=Oracle";
    private static final String INMEMORY_DB_USER = "sa";
    
    private static final String LOC = System.getProperty("doc.plugin.jdbc.db.migration", "classpath:db/migration");

    DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getJdbcUserName());
        config.setPassword(getJdbcUsernamePassword(config.getUsername()));
        config.setAutoCommit(false);
        config.addDataSourceProperty("remarksReporting", true);
        HikariDataSource dataSource = new HikariDataSource(config);
        initMigrations(dataSource);
        return dataSource;

    }

    void initMigrations(HikariDataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(LOC);
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            flyway.clean();
            flyway.migrate();
        }
    }

    private File getOutputLocation() {
        File dir = new File(System.getProperty("destDir", "target/jdbc"));
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException("Could not create output directory:" + dir);
            }
        }
        return dir;
    }

    private String getJdbcUsernamePassword(String username) {
        return System.getProperty("doc.plugin.jdbc.password", username);
    }

    private String getJdbcUserName() {
        return System.getProperty("doc.plugin.jdbc.username", INMEMORY_DB_USER);
    }

    private String getJdbcUrl() {
        return System.getProperty("doc.plugin.jdbc.url", INMEMORY_DB_JDBC_URL);
    }

    @Override
    public boolean accept(ClassDoc classDoc) {
        // doesn't use classes
        return false;
    }

    @Override
    public void process(Model model) throws Exception {
        JdbcModel jdbcModel = new JdbcModel();
        readJdbcModel(jdbcModel);
        writeJdbcModel(jdbcModel);

    }

    private void writeJdbcModel(JdbcModel jdbcModel) {
        writeToJson(jdbcModel);
        writeToAsciidoc(jdbcModel);
    }

    private void writeToJson(JdbcModel jdbcModel) {
        JsonMapper mapper = new JsonMapper();
        try (OutputStream ous = new FileOutputStream(new File(getOutputLocation(), "jdbc.json"));) {
            mapper.writeTo(ous, jdbcModel);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to open file", e);
        }
    }

    private void writeToAsciidoc(JdbcModel jdbcModel) {
        AsciidocMapper mapper = new AsciidocMapper();
        File outputFile = new File(getOutputLocation(), "jdbc");
        mapper.writeTo(outputFile.toPath(), jdbcModel);
    }

    private void readJdbcModel(JdbcModel jdbcModel) throws SQLException {
        try (Connection c = getDataSource().getConnection()) {
            DatabaseMetaData metaData = c.getMetaData();
            String catalog = c.getCatalog();
            ResultSet tables = metaData.getTables(catalog, null, "%", new String[] { "TABLE", "VIEW" });
            while (tables.next()) {
                String name = tables.getString("TABLE_NAME");
                String type = tables.getString("TABLE_TYPE");
                String ddl = tables.getString(11);
                String remarks = tables.getString("REMARKS");
                Table table = new Table(name, type, remarks).withDdl(ddl);
                jdbcModel.addTable(table);

                readColumns(metaData, catalog, name, table);

                if (table.isTable()) {
                    readForeignKeys(c, metaData, name, jdbcModel, table);
                }
            }

        }
    }

    private void readForeignKeys(Connection c, DatabaseMetaData metaData, String name, JdbcModel jdbcModel, Table table)
            throws SQLException {
        ResultSet foreignKeys = metaData.getImportedKeys(c.getCatalog(), null, name);
        while (foreignKeys.next()) {
            String fkTableName = foreignKeys.getString("FKTABLE_NAME");
            String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
            String pkTableName = foreignKeys.getString("PKTABLE_NAME");
            String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");

            table.addForeignKey(new ForeignKey(fkTableName, fkColumnName, pkTableName, pkColumnName));
            System.out.println(fkTableName + "." + fkColumnName + " -> " + pkTableName + "." + pkColumnName);
        }
    }

    private void readColumns(DatabaseMetaData metaData, String catalog, String name, Table table) throws SQLException {
        ResultSet columns = metaData.getColumns(catalog, null, name, "%");
        while (columns.next()) {
            String colName = columns.getString("COLUMN_NAME");
            String colType = columns.getString("TYPE_NAME");
            int colSize = columns.getInt("COLUMN_SIZE");
            String remarks = columns.getString("REMARKS");
            String defaultValue = columns.getString("COLUMN_DEF");

            // YES = ISO def av nullable
            boolean isNullable = "YES".equals(columns.getString("IS_NULLABLE"));

            Column column = new Column(colName, colType, colSize, defaultValue, isNullable, remarks);
            table.addColumn(column);
        }
    }
}
