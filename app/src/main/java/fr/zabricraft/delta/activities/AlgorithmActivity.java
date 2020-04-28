package fr.zabricraft.delta.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AppCompatActivity;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.NotificationNameExtension;
import fr.zabricraft.delta.fragments.AlgorithmFragment;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.Database;
import hotchemi.android.rate.AppRate;

public class AlgorithmActivity extends AppCompatActivity {

    AlgorithmFragment fragment;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.algorithm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                if (fragment != null) {
                    startEditor(fragment.getAlgorithm());
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

        Object algorithm = getIntent().getSerializableExtra("algorithm");
        if (algorithm instanceof Algorithm) {
            fragment = AlgorithmFragment.create(((Algorithm) algorithm));
        } else {
            fragment = AlgorithmFragment.create(null);
        }

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
    }

    public void startEditor(Algorithm algorithm) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("algorithm", algorithm != null ? Database.getInstance(this).getAlgorithm(algorithm.getLocalId()) : null);
        startActivityForResult(intent, 667);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 667 && resultCode == Activity.RESULT_OK) {
            // Get data from Intent
            Object algorithm = data.getSerializableExtra("algorithm");

            // Check if data is valid
            if (algorithm instanceof Algorithm) {
                // Update with new algorithm
                fragment.selectAlgorithm((Algorithm) algorithm);

                // Update home list
                EventBus.getDefault().post(new NotificationNameExtension.AlgorithmsChanged());

                // Check for a review
                AppRate.showRateDialogIfMeetsConditions(this);
            }
        }
    }

}
