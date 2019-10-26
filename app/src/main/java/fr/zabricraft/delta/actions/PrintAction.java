package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;

public class PrintAction implements Action {

    private String identifier;

    public PrintAction(String identifier) {
        this.identifier = identifier;
    }

    public void execute(Process process) {
        // Get the value
        if (process.variables.containsKey(identifier)) {
            Token value = process.variables.get(identifier);

            // Print it (add it to output)
            if (value instanceof FormattedToken) {
                // Formatted
                process.outputs.add(identifier + " = " + value.compute(process.variables, true).toString());
            } else {
                // Not formatted
                process.outputs.add(identifier + " = " + value.toString());
            }
        }
    }

    public String toString() {
        return "print \"" + identifier + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine("action_print", EditorLineCategory.output, 0, new String[]{identifier}));

        return lines;
    }

    public int editorLinesCount() {
        return 1;
    }

    public Triplet<Action, Action, Integer> action(int index, Action parent, int parentIndex) {
        return Triplet.with(((Action) this), parent, parentIndex);
    }

    public void update(EditorLine line) {
        if (line.getValues().length == 1) {
            this.identifier = line.getValues()[0];
        }
    }

    public List<Pair<String, Token>> extractInputs() {
        return new ArrayList<>();
    }

}
