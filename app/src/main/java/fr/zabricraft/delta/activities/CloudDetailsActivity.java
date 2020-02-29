package fr.zabricraft.delta.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.fragments.CloudDetailsFragment;

public class CloudDetailsActivity extends AppCompatActivity {

    CloudDetailsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Object apiAlgorithm = getIntent().getSerializableExtra("apiAlgorithm");
        if (apiAlgorithm instanceof APIAlgorithm) {
            fragment = CloudDetailsFragment.create(((APIAlgorithm) apiAlgorithm));
        } else {
            fragment = CloudDetailsFragment.create(null);
        }

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
    }
}
