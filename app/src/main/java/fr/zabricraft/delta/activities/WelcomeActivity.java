package fr.zabricraft.delta.activities;

import androidx.appcompat.app.AppCompatActivity;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.WelcomeFragment;

import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.welcome_title);

        getFragmentManager().beginTransaction().add(android.R.id.content, new WelcomeFragment()).commit();
    }
}
