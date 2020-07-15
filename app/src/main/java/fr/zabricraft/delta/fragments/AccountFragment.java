package fr.zabricraft.delta.fragments;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.utils.Account;

public class AccountFragment extends Fragment implements View.OnClickListener {

    LinearLayout linearLayout;
    TextView label;
    Button button1;
    Button button2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Configure layout
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setBackgroundColor(getResources().getColor(R.color.background));

        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());
        int dp32 = IntExtension.dpToPixel(32, getResources());

        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        labelParams.setMargins(dp16, dp32, dp16, dp32);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(dp16, dp8, dp16, dp16);

        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(getResources().getColor(R.color.colorPrimary));
        buttonBackground.setCornerRadius(dp8);

        // Configure label
        label = new TextView(getActivity());
        label.setLayoutParams(labelParams);
        label.setTextColor(getResources().getColor(android.R.color.black));
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        linearLayout.addView(label);

        // Configure button1
        button1 = new Button(getActivity());
        button1.setLayoutParams(buttonParams);
        button1.setBackground(buttonBackground);
        button1.setTextColor(getResources().getColor(android.R.color.white));
        button1.setOnClickListener(this);
        linearLayout.addView(button1);

        // Configure button2
        button2 = new Button(getActivity());
        button2.setLayoutParams(buttonParams);
        button2.setBackground(buttonBackground);
        button2.setTextColor(getResources().getColor(android.R.color.white));
        button2.setOnClickListener(this);
        linearLayout.addView(button2);

        // Load account
        loadAccount();

        // Return the view
        return linearLayout;
    }

    public void loadAccount() {
        // Check for an account
        if (Account.current.user != null) {
            // User is logged
            label.setText(getResources().getString(R.string.logged_as, Account.current.user.name, Account.current.user.username));
            button1.setText(R.string.edit_profile);
            button2.setText(R.string.sign_out);
        } else {
            // User is not logged
            label.setText(R.string.not_logged);
            button1.setText(R.string.sign_in);
            button2.setText(R.string.sign_up);
        }
    }

    @Override
    public void onClick(View view) {
        // Check for an account
        if (Account.current.user != null) {
            // Check button
            if (view == button1) {
                // Edit profile

            } else {
                // Sign out

            }
        } else {
            // Check button
            if (view == button1) {
                // Sign in

            } else {
                // Sign up

            }
        }
    }
}
