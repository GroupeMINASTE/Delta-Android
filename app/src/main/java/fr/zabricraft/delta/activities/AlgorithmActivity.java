package fr.zabricraft.delta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.AlgorithmFragment;
import fr.zabricraft.delta.utils.Algorithm;

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
                Object algorithm = getIntent().getSerializableExtra("algorithm");
                if (algorithm instanceof Algorithm) {
                    startEditor(((Algorithm) algorithm));
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
        intent.putExtra("algorithm", algorithm);
        startActivity(intent);
    }

}
