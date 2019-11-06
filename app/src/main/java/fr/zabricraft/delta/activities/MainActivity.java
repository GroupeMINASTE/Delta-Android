package fr.zabricraft.delta.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.NotificationNameExtension;
import fr.zabricraft.delta.fragments.AlgorithmFragment;
import fr.zabricraft.delta.fragments.HomeFragment;
import fr.zabricraft.delta.sections.AlgorithmsSection;
import fr.zabricraft.delta.utils.Algorithm;

public class MainActivity extends AppCompatActivity implements AlgorithmsSection.AlgorithmLoader {

    private HomeFragment fragment;
    private AlgorithmFragment algorithmFragment;
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

        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_main);

        fragment = new HomeFragment();

        getFragmentManager().beginTransaction().add(R.id.homeFragment, fragment).commit();
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void load(Algorithm algorithm) {
        currentAlgorithm = algorithm;

        if (findViewById(R.id.algorithmFragment) != null) {
            algorithmFragment = AlgorithmFragment.create(algorithm);
            getFragmentManager().beginTransaction().replace(R.id.algorithmFragment, algorithmFragment).addToBackStack(null).commit();
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
        startActivityForResult(intent, 667);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 667 && resultCode == Activity.RESULT_OK && algorithmFragment != null) {
            // Get data from Intent
            Object algorithm = data.getSerializableExtra("algorithm");

            // Check if data is valid
            if (algorithm instanceof Algorithm) {
                // Update with new algorithm
                algorithmFragment.selectAlgorithm((Algorithm) algorithm);

                // Update home list
                onAlgorithmsChanged(new NotificationNameExtension.AlgorithmsChanged());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlgorithmsChanged(NotificationNameExtension.AlgorithmsChanged event) {
        // Update home list
        fragment.loadAlgorithms();
    }

}
