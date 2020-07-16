package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;
import fr.zabricraft.delta.utils.TokenParser;

public class InputAction implements Action {

    private String identifier;
    private String value;

    public InputAction(String identifier, String value) {
        this.identifier = identifier;
        this.value = value;
    }

    public void execute(Process process) {
        // Check if variable is not a constant
        if (TokenParser.constants.contains(identifier)) {
            process.outputs.add(process.context.getString(R.string.error_constant, identifier));
        }

        // Set value with process environment
        for (Pair<String, String> input : process.inputs) {
            // Check key
            if (input.getValue0().equals(identifier)) {
                process.set(identifier, new TokenParser(input.getValue1(), process).execute());
            }
        }
    }

    public String toString() {
        return "input \"" + identifier + "\" default \"" + value + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_input, EditorLineCategory.variable, 0, new String[]{identifier, value}, true));

        return lines;
    }

    public int editorLinesCount() {
        return 1;
    }

    public Triplet<Action, Action, Integer> action(int index, Action parent, int parentIndex) {
        return Triplet.with(this, parent, parentIndex);
    }

    public void update(EditorLine line) {
        if (line.getValues().length == 2) {
            this.identifier = line.getValues()[0];
            this.value = line.getValues()[1];
        }
    }

    public List<Pair<String, String>> extractInputs() {
        List<Pair<String, String>> inputs = new ArrayList<>();

        inputs.add(Pair.with(identifier, value));

        return inputs;
    }

}
