package fr.zabricraft.delta.utils;

public class EditorLine {

    private int format;
    private EditorLineCategory category;
    private int indentation;
    private String[] values;
    private boolean movable;

    public EditorLine(int format, EditorLineCategory category, int indentation, String[] values, boolean movable) {
        this.format = format;
        this.category = category;
        this.indentation = indentation;
        this.values = values;
        this.movable = movable;
    }

    public int getFormat() {
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

    public boolean isMovable() {
        return movable;
    }

    public EditorLine incrementIndentation() {
        indentation++;

        return this;
    }
}
