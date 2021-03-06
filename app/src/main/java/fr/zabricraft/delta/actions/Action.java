package fr.zabricraft.delta.actions;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.Serializable;
import java.util.List;

import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.Process;

public interface Action extends Serializable {

    void execute(Process process);

    String toString();

    List<EditorLine> toEditorLines();

    int editorLinesCount();

    Triplet<Action, Action, Integer> action(int index, Action parent, int parentIndex);

    void update(EditorLine line);

    List<Pair<String, String>> extractInputs();

}
