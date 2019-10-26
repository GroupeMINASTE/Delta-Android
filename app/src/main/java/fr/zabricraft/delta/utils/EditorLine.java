package fr.zabricraft.delta.utils;

public class EditorLine {

    private String format;
    private EditorLineCategory category;
    private int indentation;
    private String[] values;

    public EditorLine(String format, EditorLineCategory category, int indentation, String[] values) {
        this.format = format;
        this.category = category;
        this.indentation = indentation;
        this.values = values;
    }

    public String getFormat() {
        return format;
    }

    public EditorLineCategory getCategory() {
        return category;
    }

    public int getIndentation() {
        return indentation;
    }

    public String[] getValues() {
        return values;
    }

    public void setValue(int i, String text) {
        values[i] = text;
    }

    public EditorLine incrementIndentation() {
        indentation++;

        return this;
    }
}
