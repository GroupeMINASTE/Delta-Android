package fr.zabricraft.delta.quiz;

import org.javatuples.Triplet;

import java.util.ArrayList;

public class Quiz {

    public String text;
    public ArrayList<Triplet<String, String, String>> questions;

    public Quiz(String text) {
        this.text = text;
    }

    public void addQuestion(String text, String correct) {
        questions.add(Triplet.with(text, correct, ""));
    }

}
