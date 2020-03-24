package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;

public class QuizAddAction implements Action {

    private String text;
    private String correct;

    public QuizAddAction(String text, String correct) {
        this.text = text;
        this.correct = correct;
    }

    public void execute(Process process) {
        if (process.quiz != null) {
            process.quiz.addQuestion(text, correct);
        }
    }

    public String toString() {
        return "quiz_add \"" + text + "\" correct \"" + correct + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_quiz_add, EditorLineCategory.quiz, 0, new String[]{text, correct}, true));

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
            this.text = line.getValues()[0];
            this.correct = line.getValues()[1];
        }
    }

    public List<Pair<String, String>> extractInputs() {
        return new ArrayList<>();
    }

}
