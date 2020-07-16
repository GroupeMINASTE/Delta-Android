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
import fr.zabricraft.delta.utils.TokenParser;

public class PrintAction implements Action {

    private String identifier;
    private boolean approximated;

    public PrintAction(String identifier, boolean approximated) {
        this.identifier = identifier;
        this.approximated = approximated;
    }

    public void execute(Process process) {
        // Get the value
        Token value = new TokenParser(identifier, process).execute();

        // Print it (add it to output)
        Double asDouble = null;
        if (approximated) {
            asDouble = value.compute(process.variables, false).asDouble();
        }
        if (approximated && asDouble != null) {
            if (Math.floor(asDouble) == asDouble) {
                process.outputs.add(identifier + " = " + asDouble.intValue());
            } else {
                process.outputs.add(identifier + " = " + asDouble);
            }
        } else {
            process.outputs.add(identifier + " = " + value.compute(process.variables, true).toString());
        }
    }

    public String toString() {
        return (approximated ? "print_approximated" : "print") + " \"" + identifier + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(approximated ? R.string.action_print_approximated : R.string.action_print, EditorLineCategory.output, 0, new String[]{identifier}, true));

        return lines;
    }

    public int editorLinesCount() {
        return 1;
    }

    public Triplet<Action, Action, Integer> action(int index, Action parent, int parentIndex) {
        return Triplet.with(this, parent, parentIndex);
    }

    public void update(EditorLine line) {
        if (line.getValues().length == 1) {
            this.identifier = line.getValues()[0];
        }
    }

    public List<Pair<String, String>> extractInputs() {
        return new ArrayList<>();
    }

}
