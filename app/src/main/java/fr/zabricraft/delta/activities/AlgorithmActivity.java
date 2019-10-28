package fr.zabricraft.delta.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.zabricraft.delta.fragments.AlgorithmFragment;

public class AlgorithmActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlgorithmFragment fragment = AlgorithmFragment.create(getIntent().getIntExtra("id", 0));

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
    }

}
