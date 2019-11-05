package fr.zabricraft.delta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.AlgorithmFragment;
import fr.zabricraft.delta.fragments.HomeFragment;
import fr.zabricraft.delta.sections.AlgorithmsSection;
import fr.zabricraft.delta.utils.Algorithm;

public class MainActivity extends AppCompatActivity implements AlgorithmsSection.AlgorithmLoader {

    private Algorithm currentAlgorithm = null;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startEditor(currentAlgorithm);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().add(R.id.homeFragment, new HomeFragment()).commit();
    }

    public void load(Algorithm algorithm) {
        currentAlgorithm = algorithm;

        if (findViewById(R.id.algorithmFragment) != null) {
            AlgorithmFragment fragment = AlgorithmFragment.create(algorithm);
            getFragmentManager().beginTransaction().replace(R.id.algorithmFragment, fragment).addToBackStack(null).commit();
        } else {
            Intent intent = new Intent(this, AlgorithmActivity.class);
            intent.putExtra("algorithm", algorithm);
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

    public void startEditor(Algorithm algorithm) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("algorithm", algorithm);
        startActivity(intent);
    }
}
