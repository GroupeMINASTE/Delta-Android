package fr.zabricraft.delta.quiz;

import fr.zabricraft.delta.tokens.Token;

public class QuizQuestion extends QuizElement {

    public String text;
    public Token correct;

    public QuizQuestion(String text, Token correct) {
        this.text = text;
        this.correct = correct;
    }

}
