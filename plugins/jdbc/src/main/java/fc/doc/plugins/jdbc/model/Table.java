package fc.doc.plugins.jdbc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import io.github.swagger2markup.markup.builder.MarkupBlockStyle;
import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.github.swagger2markup.markup.builder.MarkupTableColumn;

public class Table implements MarkupOutput {

    private final String name;
    private String ddl;
    private final String type;
    private final String comment;
    private final List<Column> columns = new ArrayList<>();
    private final List<ForeignKey> importedKeys = new ArrayList<>();

    public Table(String name, String type, String comment) {
        this.name = name;
        this.type = type;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

    public String getDdl() {
        return ddl;
    }

    public String getType() {
        return type;
    }

    public Table withDdl(String ddl) {
        this.ddl = ddl;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<name=" + name + ">";
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public boolean isTable() {
        return Objects.equals("TABLE", type);
    }

    public void addForeignKey(ForeignKey fk) {
        importedKeys.add(fk);
    }

    public static List<Table> filterTables(List<Table> tables, boolean isTable) {
        return tables.stream().filter(t -> t.isTable() == isTable).collect(Collectors.toList());
    }

    @Override
    public void apply(int sectionLevel, MarkupDocBuilder doc) {
        writeTableComment(doc);

        List<MarkupTableColumn> columnSpecs = new ArrayList<>();
        columnSpecs.add(new MarkupTableColumn("#", true, 10));
        columnSpecs.add(new MarkupTableColumn("Navn", true, 20));
        columnSpecs.add(new MarkupTableColumn("Type", false, 10));
        columnSpecs.add(new MarkupTableColumn("Default", false, 10));
        columnSpecs.add(new MarkupTableColumn("Nullable", false, 10));
        columnSpecs.add(new MarkupTableColumn("Constraint", false, 20));
        columnSpecs.add(new MarkupTableColumn("Comment", false, 10));
        columnSpecs.add(new MarkupTableColumn("FK", false, 20));
        
        columnSpecs.forEach(c -> c.withMarkupSpecifiers(MarkupLanguage.ASCIIDOC, ".^" + c.widthRatio));
        
        List<java.util.List<String>> cells = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        columns.forEach(c -> {
            cells.add(Arrays.asList(
                    String.valueOf(i.incrementAndGet()), c.getName(), c.getType(), c.getDefaultValue(), c.isNullable() ? "X" : "", null,
                    c.getComment(), null));
        });

        doc.tableWithColumnSpecs(columnSpecs, cells);
    }

    private void writeTableComment(MarkupDocBuilder doc) {
        doc.block(comment == null || comment.isEmpty() ? "<MISSING DOCUMENTATION>" : comment, MarkupBlockStyle.LITERAL);
    }

}
