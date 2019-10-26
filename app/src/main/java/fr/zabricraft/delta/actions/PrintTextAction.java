package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;

public class PrintTextAction implements Action {

    private String text;

    public PrintTextAction(String text) {
        this.text = text;
    }

    public void execute(Process process) {
        // Print text
        process.outputs.add(text);
    }

    public String toString() {
        return "print_text \"" + text + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine("action_print_text", EditorLineCategory.output, 0, new String[]{text}));

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
            this.text = line.getValues()[0];
        }
    }

    public List<Pair<String, Token>> extractInputs() {
        return new ArrayList<>();
    }

}
