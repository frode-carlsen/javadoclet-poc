package fc.doc.plugins.jdbc;

import org.junit.Test;

public class PluginTest {

    @Test
    public void test_migrate_ddl() throws Exception {
        initDatabase();
    }

    @Test
    public void test_get_metadata_from_database() throws Exception {
        new JdbcPlugin().process(null);
    }

    private void initDatabase() {
        new JdbcPlugin().getDataSource();
    }

}
