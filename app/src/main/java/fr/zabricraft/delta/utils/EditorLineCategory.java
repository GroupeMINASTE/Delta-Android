package fr.zabricraft.delta.utils;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.actions.Action;
import fr.zabricraft.delta.actions.ElseAction;
import fr.zabricraft.delta.actions.ForAction;
import fr.zabricraft.delta.actions.IfAction;
import fr.zabricraft.delta.actions.InputAction;
import fr.zabricraft.delta.actions.ListAddAction;
import fr.zabricraft.delta.actions.ListRemoveAction;
import fr.zabricraft.delta.actions.PrintAction;
import fr.zabricraft.delta.actions.PrintTextAction;
import fr.zabricraft.delta.actions.QuizAddAction;
import fr.zabricraft.delta.actions.QuizInitAction;
import fr.zabricraft.delta.actions.QuizShowAction;
import fr.zabricraft.delta.actions.SetAction;
import fr.zabricraft.delta.actions.UnsetAction;
import fr.zabricraft.delta.actions.WhileAction;

public enum EditorLineCategory {

    // Values
    variable(R.string.category_variable, R.drawable.variable),
    structure(R.string.category_structure, R.drawable.structure),
    output(R.string.category_output, R.drawable.output),
    list(R.string.category_list, R.drawable.list),
    quiz(R.string.category_quiz, R.drawable.quiz),
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
    public static final EditorLineCategory[] values = {variable, structure, output, list, quiz};

    // Catalog
    public Action[] catalog() {
        switch (this) {
            case variable:
                return new Action[]{
                        new InputAction("a", "0"),
                        new SetAction("a", "0"),
                        new UnsetAction("a")
                };
            case structure:
                return new Action[]{
                        new IfAction("a = b", new ElseAction()),
                        new WhileAction("a = b"),
                        new ForAction("a", "b")
                };
            case output:
                return new Action[]{
                        new PrintAction("a", false),
                        new PrintAction("a", true),
                        new PrintTextAction("Hello world!")
                };
            case list:
                return new Action[]{
                        new ListAddAction("x", "l"),
                        new ListRemoveAction("x", "l")
                };
            case quiz:
                return new Action[]{
                        new QuizInitAction("Solve equations:"),
                        new QuizAddAction("2x + 1 = 0"),
                        new QuizAddAction("x", "x"),
                        new QuizShowAction()
                };
            default:
                return new Action[]{};
        }
    }

}
