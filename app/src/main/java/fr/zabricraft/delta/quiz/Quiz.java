package fr.zabricraft.delta.quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.tokens.Token;

public class Quiz implements Serializable {

    public String text;
    public List<QuizElement> elements = new ArrayList<>();

    public Quiz(String text) {
        this.text = text;
    }

    public void addQuestion(String text, Token correct) {
        elements.add(new QuizQuestion(text, correct));
    }

    public void addParagraph(String text) {
        elements.add(new QuizParagraph(text));
    }

}
