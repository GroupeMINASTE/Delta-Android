package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;
import fr.zabricraft.delta.utils.TokenParser;

public class PrintTextAction implements Action {

    private String text;

    public PrintTextAction(String text) {
        this.text = text;
    }

    public void execute(Process process) {
        // Get output
        String output = text;

        // Get "" to interpret them
        Matcher groups = Pattern.compile("\".*\"").matcher(output);
        while (groups.find()) {
            String group = groups.group();

            // Get token based on string
            Token token = new TokenParser(group.substring(1, group.length() - 1)).execute();

            // Replace with tokens
            output = output.replace(group, token.compute(process.variables, true).toString());
        }

        // Print text
        process.outputs.add(output);
    }

    public String toString() {
        return "print_text \"" + text.replaceAll("\"", "\\\"") + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_print_text, EditorLineCategory.output, 0, new String[]{text}));

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
