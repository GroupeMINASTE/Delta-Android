package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;
import fr.zabricraft.delta.utils.TokenParser;

public class ListAddAction implements Action {

    private String value;
    private String identifier;

    public ListAddAction(String value, String identifier) {
        this.value = value;
        this.identifier = identifier;
    }

    public void execute(Process process) {
        // Try to get list
        Token list = process.get(identifier);
        if (list instanceof fr.zabricraft.delta.tokens.List) {
            // Add value to list
            List<Token> values = new ArrayList<>(((fr.zabricraft.delta.tokens.List) list).getValues());
            values.add(new TokenParser(value, process).execute().compute(process.variables, ComputeMode.simplify));
            fr.zabricraft.delta.tokens.List newList = new fr.zabricraft.delta.tokens.List(values);

            // Set new value with process environment
            process.set(identifier, newList);
        }
    }

    public String toString() {
        return "list_add \"" + value + "\" to \"" + identifier + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_list_add, EditorLineCategory.list, 0, new String[]{value, identifier}, true));

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
            this.value = line.getValues()[0];
            this.identifier = line.getValues()[1];
        }
    }

    public List<Pair<String, String>> extractInputs() {
        return new ArrayList<>();
    }

}
