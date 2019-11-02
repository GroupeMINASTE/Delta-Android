package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;

public class RootAction implements ActionBlock {

    private List<Action> actions;

    public RootAction() {
        this.actions = new ArrayList<>();
    }

    public RootAction(List<Action> actions) {
        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void append(List<Action> actions) {
        this.actions.addAll(actions);
    }

    public void execute(Process process) {
        // Execute actions
        for (Action action : actions) {
            action.execute(process);
        }
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        for (Action action : actions) {
            string.append("\n");
            string.append(action.toString());
        }
        if (string.length() > 0) {
            string.substring(1);
        }

        return string.toString();
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        for (Action action : actions) {
            lines.addAll(action.toEditorLines());
        }

        lines.add(new EditorLine(R.string.category_add, EditorLineCategory.add, 0, new String[]{}));

        return lines;
    }

    public int editorLinesCount() {
        int count = 1;

        for (Action action : actions) {
            count += action.editorLinesCount();
        }

        return count;
    }

    public Triplet<Action, Action, Integer> action(int index, Action parent, int parentIndex) {
        if (index < editorLinesCount() - 1) {
            // Iterate actions
            int i = 0;
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

        return Triplet.with(((Action) this), ((Action) this), index);
    }

    public void insert(Action action, int index) {
        if (index < editorLinesCount() - 1) {
            // Iterate actions
            int i = 0;
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
        if (index < editorLinesCount() - 1) {
            // Iterate actions
            int i = 0;
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
        // Nothing to update
    }

    public List<Pair<String, Token>> extractInputs() {
        List<Pair<String, Token>> inputs = new ArrayList<>();

        for (Action action : actions) {
            inputs.addAll(action.extractInputs());
        }

        return inputs;
    }

}
