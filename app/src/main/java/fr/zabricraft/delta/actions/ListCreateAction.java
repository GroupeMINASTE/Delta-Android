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

public class ListCreateAction implements Action {

    private String identifier;

    public ListCreateAction(String identifier) {
        this.identifier = identifier;
    }

    public void execute(Process process) {
        // Check if variable is not a constant
        if (TokenParser.constants.contains(identifier)) {
            process.outputs.add(process.context.getString(R.string.error_constant, identifier));
            return;
        }

        // Set value with process environment
        process.set(identifier, new fr.zabricraft.delta.tokens.List(new ArrayList<>()));
    }

    public String toString() {
        return "list_create \"" + identifier + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_list_create, EditorLineCategory.list, 0, new String[]{identifier}, true));

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
