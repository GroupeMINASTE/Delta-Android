package fr.zabricraft.delta.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.QuizFragment;
import fr.zabricraft.delta.quiz.Quiz;

public class QuizActivity extends AppCompatActivity {

    QuizFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Object quiz = getIntent().getSerializableExtra("quiz");
        if (quiz instanceof Quiz) {
            fragment = QuizFragment.create(((Quiz) quiz));
        } else {
            fragment = QuizFragment.create(new Quiz(""));
        }

        setTitle(R.string.category_quiz);

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
    }
}
