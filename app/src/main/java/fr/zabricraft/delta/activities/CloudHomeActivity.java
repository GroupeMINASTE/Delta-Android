package fr.zabricraft.delta.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchViewMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchViewMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Set search query and load algorithms
                fragment.search(newText);
                return true;
            }
        });

        return super.onPrepareOptionsMenu(menu);
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
            startActivityForResult(intent, 669);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 669 && resultCode == Activity.RESULT_OK) {
            // Transfer intent
            setResult(Activity.RESULT_OK, data);

            // Dismiss activity
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
