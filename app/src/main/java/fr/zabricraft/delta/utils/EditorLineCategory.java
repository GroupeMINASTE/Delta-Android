package fr.zabricraft.delta.utils;

import fr.zabricraft.delta.R;
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
    variable(R.string.category_variable, R.drawable.variable),
    structure(R.string.category_structure, R.drawable.structure),
    output(R.string.category_output, R.drawable.output),
    settings(R.string.category_settings, R.drawable.settings),
    add(R.string.category_add, 0);

    // Properties
    public final int title;
    public final int image;

    // Constructor
    EditorLineCategory(int title, int image) {
        this.title = title;
        this.image = image;
    }

    // List
    public static final EditorLineCategory[] list = {variable, structure, output};

    // Catalog
    public Action[] catalog() {
        switch (this) {
            case variable:
                return new Action[]{
                        new InputAction("a", new TokenParser("0").execute()),
                        new SetAction("a", new TokenParser("0").execute())
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
