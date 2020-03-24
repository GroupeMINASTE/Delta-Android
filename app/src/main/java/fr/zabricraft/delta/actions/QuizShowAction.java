package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.utils.Process;

public class QuizShowAction implements Action {

    public void execute(final Process process) {
        if (process.quiz != null) {
            // Create a semaphore for thread management
            final Semaphore semaphore = new Semaphore(0);

            // Show quiz to user

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Add it to outputs
                    process.outputs.add(process.quiz);

                    // And continue process
                    semaphore.release();
                }
            }).start();

            // Wait for quiz to finish
            try {
                semaphore.acquire();
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Reset quiz
        process.quiz = null;
    }

    public String toString() {
        return "quiz_show";
    }

    public List<EditorLine> toEditorLines() {
        List<EditorLine> lines = new ArrayList<>();

        lines.add(new EditorLine(R.string.action_quiz_show, EditorLineCategory.quiz, 0, new String[]{}, true));

        return lines;
    }

    public int editorLinesCount() {
        return 1;
    }

    public Triplet<Action, Action, Integer> action(int index, Action parent, int parentIndex) {
        return Triplet.with(((Action) this), parent, parentIndex);
    }

    public void update(EditorLine line) {
        // Nothing to update
    }

    public List<Pair<String, String>> extractInputs() {
        return new ArrayList<>();
    }

}
