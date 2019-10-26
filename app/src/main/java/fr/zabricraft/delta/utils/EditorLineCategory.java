package fr.zabricraft.delta.utils;

import fr.zabricraft.delta.actions.Action;
import fr.zabricraft.delta.actions.ElseAction;
import fr.zabricraft.delta.actions.ForAction;
import fr.zabricraft.delta.actions.IfAction;
import fr.zabricraft.delta.actions.InputAction;
import fr.zabricraft.delta.actions.PrintAction;
import fr.zabricraft.delta.actions.PrintTextAction;
import fr.zabricraft.delta.actions.SetAction;
import fr.zabricraft.delta.actions.WhileAction;

public enum EditorLineCategory {

    // Values
    variable("variable"), structure("structure"), output("output"), settings("settings"), add("add");

    // Properties
    String rawValue;

    // Constructor
    EditorLineCategory(String rawValue) {
        this.rawValue = rawValue;
    }

    // Catalog
    Action[] catalog() {
        switch (this) {
            case variable:
                return new Action[]{
                        new InputAction("a", new TokenParser("0").execute()),
                        new SetAction("a", new TokenParser("0").execute()),
                        new SetAction("f(x)", new TokenParser("ax+b").execute(), true)
                };
            case structure:
                return new Action[]{
                        new IfAction(new TokenParser("a=b").execute(), new ElseAction()),
                        new WhileAction(new TokenParser("a=b").execute()),
                        new ForAction("a", new TokenParser("b").execute())
                };
            case output:
                return new Action[]{
                        new PrintAction("a"),
                        new PrintTextAction("Hello world!")
                };
            default:
                return new Action[]{};
        }
    }

}
