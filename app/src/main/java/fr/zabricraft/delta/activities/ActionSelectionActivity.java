package fr.zabricraft.delta.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.ActionSelectionFragment;

public class ActionSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.action_selector);

        getFragmentManager().beginTransaction().add(android.R.id.content, new ActionSelectionFragment()).commit();
    }
}
