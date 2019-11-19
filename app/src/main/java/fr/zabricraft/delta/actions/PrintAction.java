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

    public PrintAction(String identifier) {
        this.identifier = identifier;
    }

    public void execute(Process process) {
        // Get the value
        Token value = new TokenParser(identifier).execute();

        // Print it (add it to output)
        process.outputs.add(identifier + " = " + value.compute(process.variables, true).toString());
    }

    public String toString() {
        return "print \"" + identifier + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_print, EditorLineCategory.output, 0, new String[]{identifier}));

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

    public List<Pair<String, String>> extractInputs() {
        return new ArrayList<>();
    }

}
