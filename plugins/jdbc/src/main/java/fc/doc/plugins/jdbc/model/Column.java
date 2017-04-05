package fc.doc.plugins.jdbc.model;

public class Column {

    private final String columnName;
    private final String columnType;
    private final int columnSize;
    private final String comment;
    private boolean isNullable;
    private String defaultValue;

    public Column(String name, String type, int colSize, String defaultValue, boolean isNullable, String comment) {
        this.columnName = name;
        this.columnType = type;
        this.columnSize = colSize;
        this.defaultValue = defaultValue;
        this.isNullable = isNullable;
        this.comment = comment;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public String getComment() {
        return comment;
    }

    public String getName() {
        return columnName;
    }

    public String getType() {
        return columnType;
    }

    public int getSize() {
        return columnSize;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<name=" + columnName + ", type=" + columnType + ">";
    }
}