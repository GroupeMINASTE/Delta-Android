package fr.zabricraft.delta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.fragments.CloudDetailsFragment;
import fr.zabricraft.delta.fragments.CloudHomeFragment;
import fr.zabricraft.delta.sections.APIAlgorithmsSection;

public class CloudHomeActivity extends AppCompatActivity implements APIAlgorithmsSection.APIAlgorithmLoader {

    private CloudHomeFragment fragment;
    private CloudDetailsFragment detailsFragment;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cloud_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create views
        setTitle(R.string.cloud);

        setContentView(R.layout.activity_cloud_home);

        fragment = new CloudHomeFragment();

        getFragmentManager().beginTransaction().add(R.id.cloudHomeFragment, fragment).commit();
    }

    public void load(APIAlgorithm apiAlgorithm) {
        if (findViewById(R.id.cloudDetailsFragment) != null) {
            detailsFragment = CloudDetailsFragment.create(apiAlgorithm);
            getFragmentManager().beginTransaction().replace(R.id.cloudDetailsFragment, detailsFragment).addToBackStack(null).commit();
        } else {
            Intent intent = new Intent(this, CloudDetailsActivity.class);
            intent.putExtra("apiAlgorithm", apiAlgorithm);
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
