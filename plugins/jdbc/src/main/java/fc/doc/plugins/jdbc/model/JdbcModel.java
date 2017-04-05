package fc.doc.plugins.jdbc.model;

import java.util.ArrayList;
import java.util.List;

import io.github.swagger2markup.markup.builder.MarkupDocBuilder;

public class JdbcModel implements MarkupOutput {

    private List<Table> tables = new ArrayList<>();

    private String schemaName;

    public JdbcModel() {
    }

    public JdbcModel(String schemaName) {
        this.schemaName = schemaName;
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public List<Table> getTables() {
        return tables;
    }

    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public void apply(int sectionLevel, MarkupDocBuilder doc) {
        // TODO Auto-generated method stub
        sectionLevel++;
        doc.sectionTitleLevel(sectionLevel, "Database");
        
        sectionLevel++;
        
        doc.sectionTitleLevel(sectionLevel, "Tabeller");
        for(Table t:Table.filterTables(tables, true)){
            doc.sectionTitleLevel(sectionLevel+1, "Tabell: " + t.getName());
            t.apply(sectionLevel, doc);
        }
        
        doc.sectionTitleLevel(sectionLevel, "Views");
        for(Table t:Table.filterTables(tables, false)){
            doc.sectionTitleLevel(sectionLevel+1, "View: " +t.getName());
            t.apply(sectionLevel, doc);
        }
    }
}
