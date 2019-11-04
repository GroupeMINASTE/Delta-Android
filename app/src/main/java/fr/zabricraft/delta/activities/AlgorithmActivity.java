package fr.zabricraft.delta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.AlgorithmFragment;

public class AlgorithmActivity extends AppCompatActivity {

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.algorithm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startEditor(getIntent().getIntExtra("id", 0));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlgorithmFragment fragment = AlgorithmFragment.create(getIntent().getIntExtra("id", 0));

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
    }

    public void startEditor(int algorithm) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("id", algorithm);
        startActivity(intent);
    }

}
