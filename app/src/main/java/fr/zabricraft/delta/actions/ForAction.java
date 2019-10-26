package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.ArrayExtension;
import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;
import fr.zabricraft.delta.utils.TokenParser;

public class ForAction implements ActionBlock {

    private String identifier;
    private Token token;
    private List<Action> actions;

    public ForAction(String identifier, Token token) {
        this.identifier = identifier;
        this.token = token;
        this.actions = new ArrayList<>();
    }

    public ForAction(String identifier, Token token, List<Action> actions) {
        this.identifier = identifier;
        this.token = token;
        this.actions = actions;
    }

    public void append(List<Action> actions) {
        this.actions.addAll(actions);
    }

    public void execute(Process process) {
        // Check if variable is not a constant
        if (TokenParser.constants.contains(identifier)) {
            process.outputs.add(process.context.getString(R.string.error_constant, identifier));
        }

        // Get computed token
        Token token = this.token.compute(process.variables, false);

        // Get list
        if (token instanceof fr.zabricraft.delta.tokens.List) {
            // Iterate list
            for (Token element : ((fr.zabricraft.delta.tokens.List) token).getValues()) {
                // Set value
                process.variables.put(identifier, element);

                // Execute actions
                for (Action action : actions) {
                    action.execute(process);
                }
            }
        }

        // Get interval
        // TODO
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("for \"");
        string.append(identifier);
        string.append("\" in \"");
        string.append(token.toString());
        string.append("\" {");

        for (Action action : actions) {
            string.append("\n");
            string.append(StringExtension.indentLines(action.toString()));
        }

        string.append("\n}");

        return string.toString();
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine("action_for", EditorLineCategory.structure, 0, new String[]{identifier, token.toString()}));

        for (Action action : actions) {
            lines.addAll(ArrayExtension.incrementIndentation(action.toEditorLines()));
        }

        lines.add(new EditorLine("", EditorLineCategory.add, 1, new String[]{}));
        lines.add(new EditorLine("action_end", EditorLineCategory.structure, 0, new String[]{}));

        return lines;
    }

    public int editorLinesCount() {
        int count = 3;

        for (Action action : actions) {
            count += action.editorLinesCount();
        }

        return count;
    }

    public Triplet<Action, Action, Integer> action(int index, Action parent, int parentIndex) {
        if (index != 0 && index < editorLinesCount() - 2) {
            // Iterate actions
            int i = 1;
            for (Action action : actions) {
                // Get size
                int size = action.editorLinesCount();

                // Check if index is in this action
                if (i + size > index) {
                    // Delegate to action
                    return action.action(index - i, this, index);
                } else {
                    // Continue
                    i += size;
                }
            }
        }

        return Triplet.with(((Action) this), index == 0 ? parent : this, index == 0 ? parentIndex : index);
    }

    public void insert(Action action, int index) {
        if (index != 0 && index < editorLinesCount() - 2) {
            // Iterate actions
            int i = 1;
            int ri = 0;
            for (Action action1 : actions) {
                // Get size
                int size = action1.editorLinesCount();

                // Check if index is in this action
                if (i + size > index) {
                    // Add it here
                    actions.add(ri, action);
                    return;
                } else {
                    // Continue
                    i += size;
                    ri++;
                }
            }
        }

        // No index found, add it at the end
        actions.add(action);
    }

    public void delete(int index) {
        if (index != 0 && index < editorLinesCount() - 2) {
            // Iterate actions
            int i = 1;
            int ri = 0;
            for (Action action : actions) {
                // Get size
                int size = action.editorLinesCount();

                // Check if index is in this action
                if (i + size > index) {
                    // Delete this one
                    actions.remove(ri);
                    return;
                } else {
                    // Continue
                    i += size;
                    ri++;
                }
            }
        }
    }

    public void update(EditorLine line) {
        if (line.getValues().length == 2) {
            // Get "for identifier in token"
            this.identifier = line.getValues()[0];
            this.token = new TokenParser(line.getValues()[1]).execute();
        }
    }

    public List<Pair<String, Token>> extractInputs() {
        List<Pair<String, Token>> inputs = new ArrayList<>();

        for (Action action : actions) {
            inputs.addAll(action.extractInputs());
        }

        return inputs;
    }

}
