package fr.zabricraft.delta.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.zabricraft.delta.fragments.EditorFragment;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditorFragment fragment = EditorFragment.create(getIntent().getIntExtra("id", 0));

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
    }
}
