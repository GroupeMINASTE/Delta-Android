package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;
import fr.zabricraft.delta.utils.TokenParser;

public class ListRemoveAction implements Action {

    private String value;
    private String identifier;

    public ListRemoveAction(String value, String identifier) {
        this.value = value;
        this.identifier = identifier;
    }

    public void execute(Process process) {
        // Try to get list
        Token list = process.get(identifier);
        if (list instanceof fr.zabricraft.delta.tokens.List) {
            // Remove value from list
            Token removal = new TokenParser(value, process).execute().compute(process.variables, ComputeMode.simplify);
            List<Token> values = new ArrayList<>(((fr.zabricraft.delta.tokens.List) list).getValues());
            Iterator<Token> it = values.iterator();
            while (it.hasNext()) {
                Token value = it.next();
                if (value.equals(removal)) {
                    it.remove();
                }
            }
            fr.zabricraft.delta.tokens.List newList = new fr.zabricraft.delta.tokens.List(values);

            // Set new value with process environment
            process.set(identifier, newList);
        }
    }

    public String toString() {
        return "list_remove \"" + value + "\" from \"" + identifier + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_list_remove, EditorLineCategory.list, 0, new String[]{value, identifier}, true));

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
