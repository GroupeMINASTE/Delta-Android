package fr.zabricraft.delta.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.utils.Account;
import fr.zabricraft.delta.utils.Exported;

public class AccountFragment extends Fragment implements View.OnClickListener {

    LinearLayout linearLayout;
    TextView label;
    Button button1;
    Button button2;
    TextView more;

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

        // Configure more
        more = new TextView(getActivity());
        more.setLayoutParams(buttonParams);
        more.setTextColor(getResources().getColor(R.color.colorPrimary));
        more.setOnClickListener(this);
        more.setText(R.string.more);
        more.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        linearLayout.addView(more);

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
            more.setVisibility(View.VISIBLE);
        } else {
            // User is not logged
            label.setText(R.string.not_logged);
            button1.setText(R.string.sign_in);
            button2.setText(R.string.sign_up);
            more.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        // Check for an account
        if (Account.current.user != null) {
            // Check button
            if (view == button1) {
                // Edit profile
                editProfile();
            } else if (view == button2) {
                // Sign out
                signOut();
            } else {
                // More
                openMore();
            }
        } else {
            // Check button
            if (view == button1) {
                // Sign in
                signIn();
            } else if (view == button2) {
                // Sign up
                signUp();
            }
        }
    }

    public void signIn() {
        // Create a dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()).setTitle(R.string.sign_in).setMessage(R.string.sign_in_description);

        // Create a linear layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        alert.setView(linearLayout);

        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp20 = IntExtension.dpToPixel(20, getResources());

        LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fieldParams.setMargins(dp20, 0, dp20, dp8);

        // Add username field
        final EditText username = new EditText(getActivity());
        username.setLayoutParams(fieldParams);
        username.setHint(R.string.field_username);
        linearLayout.addView(username);

        // Add password field
        final EditText password = new EditText(getActivity());
        password.setLayoutParams(fieldParams);
        password.setHint(R.string.field_password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        linearLayout.addView(password);

        // Add login button
        alert.setPositiveButton(R.string.sign_in, (dialogInterface, i) -> {
            // Extract text from fields
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            if (!usernameText.isEmpty() && !passwordText.isEmpty()) {
                // Show a loading
                AlertDialog loading = new AlertDialog.Builder(getActivity()).setTitle(R.string.loading).create();
                loading.show();

                // Start login process
                Account.current.login(usernameText, passwordText, getActivity(), status -> {
                    // Refresh the UI
                    loadAccount();
                    loading.dismiss();

                    // Check for a 404
                    if (status == APIResponseStatus.notFound) {
                        // User not found, show an alert
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.sign_in).setMessage(R.string.sign_in_error).setNeutralButton(R.string.close, (dialogInterface1, i1) -> {}).create().show();
                    }
                });
            }
        });

        // Add login button
        alert.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {});

        // Show it
        alert.create().show();
    }

    public void signUp() {
        // Create a dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()).setTitle(R.string.sign_up).setMessage(R.string.sign_up_description);

        // Create a linear layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        alert.setView(linearLayout);

        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp20 = IntExtension.dpToPixel(20, getResources());

        LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Add name field
        final EditText name = new EditText(getActivity());
        name.setLayoutParams(fieldParams);
        name.setHint(R.string.field_name);
        linearLayout.addView(name);
        fieldParams.setMargins(dp20, 0, dp20, dp8);

        // Add username field
        final EditText username = new EditText(getActivity());
        username.setLayoutParams(fieldParams);
        username.setHint(R.string.field_username);
        linearLayout.addView(username);

        // Add password field
        final EditText password = new EditText(getActivity());
        password.setLayoutParams(fieldParams);
        password.setHint(R.string.field_password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        linearLayout.addView(password);

        // Add login button
        alert.setPositiveButton(R.string.sign_up, (dialogInterface, i) -> {
            // Extract text from fields
            String nameText = name.getText().toString();
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            if (!nameText.isEmpty() && !usernameText.isEmpty() && !passwordText.isEmpty()) {
                // Show a loading
                AlertDialog loading = new AlertDialog.Builder(getActivity()).setTitle(R.string.loading).create();
                loading.show();

                // Start login process
                Account.current.register(nameText, usernameText, passwordText, getActivity(), status -> {
                    // Refresh the UI
                    loadAccount();
                    loading.dismiss();

                    // Check for a 400
                    if (status == APIResponseStatus.notFound) {
                        // Username already taken
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.sign_up).setMessage(R.string.sign_up_error).setNeutralButton(R.string.close, (dialogInterface1, i1) -> {}).create().show();
                    }
                });
            }
        });

        // Add login button
        alert.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {});

        // Show it
        alert.create().show();
    }

    public void signOut() {
        // Show a loading
        AlertDialog loading = new AlertDialog.Builder(getActivity()).setTitle(R.string.loading).create();
        loading.show();

        // Just call API
        Account.current.logout(getActivity(), status -> {
            // Reload the view
            loadAccount();
            loading.dismiss();
        });
    }

    public void editProfile() {
        // Create a dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()).setTitle(R.string.edit_profile).setMessage(R.string.edit_profile_description);

        // Create a linear layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        alert.setView(linearLayout);

        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp20 = IntExtension.dpToPixel(20, getResources());

        LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Add name field
        final EditText name = new EditText(getActivity());
        name.setLayoutParams(fieldParams);
        name.setHint(R.string.field_name);
        name.setText(Account.current.user != null && Account.current.user.name != null ? Account.current.user.name : "");
        linearLayout.addView(name);
        fieldParams.setMargins(dp20, 0, dp20, dp8);

        // Add username field
        final EditText username = new EditText(getActivity());
        username.setLayoutParams(fieldParams);
        username.setHint(R.string.field_username);
        username.setText(Account.current.user != null && Account.current.user.username != null ? Account.current.user.username : "");
        linearLayout.addView(username);

        // Add password field
        final EditText password = new EditText(getActivity());
        password.setLayoutParams(fieldParams);
        password.setHint(R.string.field_password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        linearLayout.addView(password);

        // Add login button
        alert.setPositiveButton(R.string.edit_profile, (dialogInterface, i) -> {
            // Extract text from fields
            String nameText = name.getText().toString();
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            if (!nameText.isEmpty() && !usernameText.isEmpty() && !passwordText.isEmpty()) {
                // Show a loading
                AlertDialog loading = new AlertDialog.Builder(getActivity()).setTitle(R.string.loading).create();
                loading.show();

                // Start edit process
                Account.current.editProfile(nameText, usernameText, passwordText, getActivity(), status -> {
                    // Refresh the UI
                    loadAccount();
                    loading.dismiss();

                    // Check for a 400
                    if (status == APIResponseStatus.notFound) {
                        // Username already taken
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.edit_profile).setMessage(R.string.edit_profile_error).setNeutralButton(R.string.close, (dialogInterface1, i1) -> {}).create().show();
                    }
                });
            }
        });

        // Add login button
        alert.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {});

        // Show it
        alert.create().show();
    }

    public void openMore() {
        // Get strings
        String[] strings = {
                getString(R.string.download_account),
                getString(R.string.delete_account),
                getString(R.string.cancel)
        };

        // Create an alert dialog
        new AlertDialog.Builder(getActivity()).setTitle(R.string.more).setItems(strings, (dialogInterface, i) -> {
            // Download
            if (i == 0) {
                // Show a loading
                AlertDialog loading = new AlertDialog.Builder(getActivity()).setTitle(R.string.status_downloading).create();
                loading.show();

                // Download data
                Account.current.downloadData(getActivity(), (object, status) -> {
                    loading.dismiss();

                    // Check data
                    if (object instanceof JSONObject && status == APIResponseStatus.ok) {
                        // Convert it
                        Exported data = new Exported((JSONObject) object);

                        // Create the export action
                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, data.toString());
                        getActivity().startActivity(Intent.createChooser(share, getString(R.string.download_account)));
                    } else {
                        // Download error
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.download_account).setMessage(R.string.status_error).setNeutralButton(R.string.close, (dialogInterface1, i1) -> {}).create().show();
                    }
                });
            }

            // Delete
            else if (i == 1) {
                // Ask for a confirmation (to be sure)
                new AlertDialog.Builder(getActivity()).setTitle(R.string.delete_account).setMessage(R.string.delete_account_description).setPositiveButton(R.string.delete, (dialogInterface12, i12) -> {
                    // Show a loading
                    AlertDialog loading = new AlertDialog.Builder(getActivity()).setTitle(R.string.loading).create();
                    loading.show();

                    // Delete data
                    Account.current.delete(getActivity(), status -> {
                        // Reload the view
                        loadAccount();
                        loading.dismiss();

                        // Check data
                        if (status == APIResponseStatus.ok) {
                            // Delete success
                            new AlertDialog.Builder(getActivity()).setTitle(R.string.delete_account).setMessage(R.string.delete_account_success).setNeutralButton(R.string.close, (dialogInterface1, i1) -> {}).create().show();
                        } else {
                            // Delete error
                            new AlertDialog.Builder(getActivity()).setTitle(R.string.delete_account).setMessage(R.string.status_error).setNeutralButton(R.string.close, (dialogInterface1, i1) -> {}).create().show();
                        }
                    });
                }).setNegativeButton(R.string.cancel, (dialogInterface1, i1) -> {}).create().show();
            }
        }).create().show();
    }

}
