package fr.zabricraft.delta.utils;

public class AlgorithmIcon {

    public static final String defaultIcon = "x";
    public static final String defaultColor = "river";

    private String icon;
    private String color;

    public AlgorithmIcon(String icon, String color) {
        this.icon = icon;
        this.color = color;
    }

    public AlgorithmIcon() {
        this(defaultIcon, defaultColor);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
