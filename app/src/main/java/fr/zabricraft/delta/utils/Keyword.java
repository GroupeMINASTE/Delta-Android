package fr.zabricraft.delta.utils;

public enum Keyword {

    // Values
    Input("input"),
    Default("default"),
    For("for"),
    In("in"),
    If("if"),
    Else("else"),
    Print("print"),
    PrintApproximated("print_approximated"),
    PrintText("print_text"),
    Set("set"),
    Unset("unset"),
    To("to"),
    From("from"),
    While("while"),
    ListCreate("list_create"),
    ListAdd("list_add"),
    ListRemove("list_remove"),
    QuizInit("quiz_init"),
    QuizAdd("quiz_add"),
    QuizShow("quiz_show"),
    Correct("correct");

    // Properties
    public final String rawValue;

    // Constructor
    Keyword(String rawValue) {
        this.rawValue = rawValue;
    }

}
