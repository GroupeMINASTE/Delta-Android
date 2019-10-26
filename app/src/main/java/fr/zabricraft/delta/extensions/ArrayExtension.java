package fr.zabricraft.delta.extensions;

import java.util.List;

import fr.zabricraft.delta.utils.EditorLine;

public class ArrayExtension {

    public static List<EditorLine> incrementIndentation(List<EditorLine> list) {
        for (EditorLine line : list) {
            line.incrementIndentation();
        }

        return list;
    }

}
