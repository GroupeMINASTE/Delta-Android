package fr.zabricraft.delta.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.fragments.AccountFragment;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.my_account);

        getFragmentManager().beginTransaction().add(android.R.id.content, new AccountFragment()).commit();
    }
}
