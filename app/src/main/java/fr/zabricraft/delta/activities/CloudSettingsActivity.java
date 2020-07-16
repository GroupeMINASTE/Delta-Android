package fr.zabricraft.delta.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.CloudSettingsFragment;

public class CloudSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.cloud_settings_title);

        getFragmentManager().beginTransaction().add(android.R.id.content, new CloudSettingsFragment()).commit();
    }
}
