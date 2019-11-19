package fr.zabricraft.delta.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.AlgorithmExtension;
import fr.zabricraft.delta.extensions.NotificationNameExtension;
import fr.zabricraft.delta.fragments.AlgorithmFragment;
import fr.zabricraft.delta.fragments.HomeFragment;
import fr.zabricraft.delta.sections.AlgorithmsSection;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.Database;

public class MainActivity extends AppCompatActivity implements AlgorithmsSection.AlgorithmLoader {

    private HomeFragment fragment;
    private AlgorithmFragment algorithmFragment;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                if (algorithmFragment != null) {
                    startEditor(algorithmFragment.getAlgorithm());
                } else {
                    startEditor(null);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get build number
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
        int build_number = data.getInt("build_number", 0);

        // Check to update
        if (build_number < 13) {
            // Get all algorithms
            List<Algorithm> algorithms = Database.getInstance(this).getAlgorithms();

            // Clear downloaded algorithms and save them again
            for (Algorithm algorithm : algorithms) {
                // Check id it is a download
                if (!algorithm.isOwner()) {
                    // Remove it
                    Database.getInstance(this).deleteAlgorithm(algorithm);
                }
            }

            // Add again downloads
            for (Algorithm algorithm : AlgorithmExtension.defaults) {
                Database.getInstance(this).addAlgorithm(algorithm);
            }

            // Reload list
            algorithms = Database.getInstance(this).getAlgorithms();

            // Replace set_formatted by set in code
            for (Algorithm algorithm : algorithms) {
                // Just save them again
                Database.getInstance(this).updateAlgorithm(algorithm);
            }
        }

        // Get current version and save it
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            data.edit().putInt("build_number", pInfo.versionCode).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create views
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
