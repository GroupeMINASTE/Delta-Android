package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.tokens.FormattedToken;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;
import fr.zabricraft.delta.utils.TokenParser;

public class SetAction implements Action {

    private String identifier;
    private Token value;
    private boolean format;

    public SetAction(String identifier, Token value, boolean format) {
        this.identifier = identifier;
        this.value = value;
        this.format = format;
    }

    public SetAction(String identifier, Token value) {
        this.identifier = identifier;
        this.value = value;
        this.format = false;
    }

    public void execute(Process process) {
        // Check if variable is not a constant
        if (TokenParser.constants.contains(identifier)) {
            process.outputs.add(process.context.getString(R.string.error_constant, identifier));
        }

        // Set value with process environment
        if (format) {
            process.variables.put(identifier, new FormattedToken(value));
        } else {
            process.variables.put(identifier, value.compute(process.variables, false));
        }
    }

    public String toString() {
        return (format ? "set_formatted" : "set") + " \"" + identifier + "\" to \"" + value.toString() + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(format ? R.string.action_set_formatted : R.string.action_set, EditorLineCategory.variable, 0, new String[]{identifier, value.toString()}));

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
        return new ArrayList<>();
    }

}
