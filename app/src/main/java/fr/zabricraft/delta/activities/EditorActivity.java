package fr.zabricraft.delta.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.EditorFragment;
import fr.zabricraft.delta.utils.Algorithm;

public class EditorActivity extends AppCompatActivity {

    public static EditorActivity lastInstance;

    private EditorFragment fragment;
    private boolean saving = false;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (!saving) {
                    saving = true;
                    fragment.saveAndClose();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.editor);

        Object algorithm = getIntent().getSerializableExtra("algorithm");
        if (algorithm instanceof Algorithm) {
            fragment = EditorFragment.create(((Algorithm) algorithm));
        } else {
            fragment = EditorFragment.create(null);
        }

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();

        lastInstance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        lastInstance = null;
    }

    public void updateRemoteId(Integer remote_id) {
        fragment.updateRemoteId(remote_id);
    }
}
