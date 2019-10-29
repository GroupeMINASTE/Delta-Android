package fr.zabricraft.delta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.AlgorithmFragment;
import fr.zabricraft.delta.fragments.HomeFragment;
import fr.zabricraft.delta.sections.AlgorithmsSection;

public class MainActivity extends AppCompatActivity implements AlgorithmsSection.AlgorithmLoader {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().add(R.id.homeFragment, new HomeFragment()).commit();
    }

    public void load(int algorithm) {
        if (findViewById(R.id.algorithmFragment) != null) {
            AlgorithmFragment fragment = AlgorithmFragment.create(algorithm);
            getFragmentManager().beginTransaction().replace(R.id.algorithmFragment, fragment).addToBackStack(null).commit();
        } else {
            Intent intent = new Intent(this, AlgorithmActivity.class);
            intent.putExtra("id", algorithm);
            startActivity(intent);
        }
    }

    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
