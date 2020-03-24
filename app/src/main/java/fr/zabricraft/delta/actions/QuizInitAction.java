package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.quiz.Quiz;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;

public class QuizInitAction implements Action {

    private String text;

    public QuizInitAction(String text) {
        this.text = text;
    }

    public void execute(Process process) {
        process.quiz = new Quiz(StringExtension.replaceTokens(text, process));
    }

    public String toString() {
        return "quiz_init \"" + text.replaceAll("\"", "\\\\\"") + "\"";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_quiz_init, EditorLineCategory.quiz, 0, new String[]{text}, true));

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

    public List<Pair<String, String>> extractInputs() {
        return new ArrayList<>();
    }

}
