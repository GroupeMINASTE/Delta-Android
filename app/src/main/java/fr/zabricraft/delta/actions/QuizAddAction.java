package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;
import fr.zabricraft.delta.utils.TokenParser;

public class QuizAddAction implements Action {

    private String text;
    private String correct;

    public QuizAddAction(String text) {
        this.text = text;
        this.correct = null;
    }

    public QuizAddAction(String text, String correct) {
        this.text = text;
        this.correct = correct;
    }

    public void execute(Process process) {
        if (process.quiz != null) {
            if (correct != null) {
                process.quiz.addQuestion(StringExtension.replaceTokens(text, process), new TokenParser(correct, process).execute().compute(process.variables, ComputeMode.simplify));
            } else {
                process.quiz.addParagraph(StringExtension.replaceTokens(text, process));
            }
        }
    }

    public String toString() {
        if (correct != null) {
            return "quiz_add \"" + text.replaceAll("\"", "\\\\\"") + "\" correct \"" + correct + "\"";
        }
        return "quiz_add \"" + text.replaceAll("\"", "\\\\\"") + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        if (correct != null) {
            lines.add(new EditorLine(R.string.action_quiz_add_question, EditorLineCategory.quiz, 0, new String[]{text, correct}, true));
        } else {
            lines.add(new EditorLine(R.string.action_quiz_add_paragraph, EditorLineCategory.quiz, 0, new String[]{text}, true));
        }

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
            this.text = line.getValues()[0];
            this.correct = line.getValues()[1];
        } else if (line.getValues().length == 1) {
            this.text = line.getValues()[0];
        }
    }

    public List<Pair<String, String>> extractInputs() {
        return new ArrayList<>();
    }

}
