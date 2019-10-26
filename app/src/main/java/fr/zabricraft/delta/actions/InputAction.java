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

public class InputAction implements Action {

    private String identifier;
    private Token value;

    public InputAction(String identifier, Token value) {
        this.identifier = identifier;
        this.value = value;
    }

    public void execute(Process process) {
        // Check if variable is not a constant
        if (TokenParser.constants.contains(identifier)) {
            process.outputs.add(process.context.getString(R.string.error_constant, identifier));
        }

        // Set value with process environment
        for (Pair<String, Token> input : process.inputs) {
            // Check key
            if (input.getValue0().equals(identifier)) {
                process.variables.put(identifier, input.getValue1().compute(process.variables, false));
            }
        }
    }

    public String toString() {
        return "input \"" + identifier + "\" default \"" + value.toString() + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine("action_input", EditorLineCategory.variable, 0, new String[]{identifier, value.toString()}));

        return lines;
    }

    public int editorLinesCount() {
        return 1;
    }

    public Triplet<Action, Action, Integer> action(int index, Action parent, int parentIndex) {
        return Triplet.with(((Action) this), parent, parentIndex);
    }

    public void update(EditorLine line) {
        if (line.getValues().length == 2) {
            this.identifier = line.getValues()[0];
            this.value = new TokenParser(line.getValues()[1]).execute();
        }
    }

    public List<Pair<String, Token>> extractInputs() {
        List<Pair<String, Token>> inputs = new ArrayList<>();

        inputs.add(Pair.with(identifier, value));

        return inputs;
    }

}
