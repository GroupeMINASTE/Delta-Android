package fr.zabricraft.delta.utils;

public enum Keyword {

    // Values
    Input("input"), Default("default"), For("for"), In("in"), If("if"), Else("else"), Print("print"), PrintText("print_text"), Set("set"), SetFormatted("set_formatted"), To("to"), While("while");

    // Properties
    public final String rawValue;

    // Constructor
    Keyword(String rawValue) {
        this.rawValue = rawValue;
    }

}
