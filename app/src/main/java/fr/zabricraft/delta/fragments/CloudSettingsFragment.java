package fr.zabricraft.delta.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.activities.EditorActivity;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.api.APIRequest;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.sections.CloudOtherSection;
import fr.zabricraft.delta.sections.CloudSwitchSection;
import fr.zabricraft.delta.utils.Algorithm;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CloudSettingsFragment extends Fragment implements CloudSwitchSection.CloudSwitchContainer, CloudOtherSection.CloudOtherContainer {

    private SectionedRecyclerViewAdapter sectionAdapter;
    private CloudSwitchSection publicSection;
    private CloudOtherSection otherSection;

    private Algorithm algorithm;

    private boolean loaded = false;
    private Boolean public_;
    private String notes;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background));

        // Initialize sections
        sectionAdapter = new SectionedRecyclerViewAdapter();
        publicSection = new CloudSwitchSection(R.string.cloud_settings_public_title, this);
        publicSection.setVisible(false);
        otherSection = new CloudOtherSection(this);
        otherSection.setVisible(false);
        sectionAdapter.addSection(new CloudSwitchSection(R.string.cloud_settings_sync_title, this));
        sectionAdapter.addSection(publicSection);
        sectionAdapter.addSection(otherSection);

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Retrieve algorithm
        Object object = getActivity().getIntent().getSerializableExtra("algorithm");
        if (object instanceof Algorithm) {
            this.algorithm = (Algorithm) object;
            fetchMetadatas();
        }

        return recyclerView;
    }

    public void refreshRecyclerView() {
        // Refresh
        boolean visible = algorithm.getRemoteId() != null && !algorithm.getRemoteId().equals(0);
        publicSection.setVisible(visible);
        otherSection.setVisible(visible);
        sectionAdapter.notifyDataSetChanged();
    }

    public void fetchMetadatas() {
        // No ore loaded (in case of reset needed)
        loaded = false;

        // Call API
        new APIAlgorithm(algorithm.getRemoteId(), null, null, null, null, null).fetchMissingData(getActivity(), new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                // Check response
                if (object instanceof JSONObject && status == APIResponseStatus.ok) {
                    // Convert it to APIAlgorithm
                    APIAlgorithm data = new APIAlgorithm((JSONObject) object);

                    // Update data
                    public_ = data.public_;
                    notes = data.notes;
                    loaded = true;
                }

                // Refresh recycler view
                refreshRecyclerView();
            }
        });
    }

    public void sendMetadatas() {
        // Create an alert (for progress)
        AlertDialog alert = new AlertDialog.Builder(getActivity()).setTitle(R.string.status_uploading).create();
        alert.show();

        // Start uploading
        algorithm.toAPIAlgorithm(public_ != null ? public_ : false, notes != null ? notes : "").upload(getActivity(), new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                // Remove alert
                alert.dismiss();

                // Check data
                if (object instanceof JSONObject && (status == APIResponseStatus.ok || status == APIResponseStatus.created)) {
                    // Convert to APIAlgorithm
                    APIAlgorithm data = new APIAlgorithm((JSONObject) object);

                    // Transform returned data
                    algorithm.setRemoteId(data.id);
                    public_ = data.public_;
                    notes = data.notes;

                    // Send back new data
                    if (EditorActivity.lastInstance != null) {
                        EditorActivity.lastInstance.updateRemoteId(algorithm.getRemoteId());
                    }
                }

                // Refresh recycler view
                refreshRecyclerView();
            }
        });
    }

    public void removeFromCloud() {
        // Create an alert (for progress)
        AlertDialog alert = new AlertDialog.Builder(getActivity()).setTitle(R.string.status_deleting).create();
        alert.show();

        // Start deleting
        algorithm.toAPIAlgorithm().delete(getActivity(), new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                // Remove alert
                alert.dismiss();

                // Check data
                if (status == APIResponseStatus.ok) {
                    // Remove remote id
                    algorithm.setRemoteId(null);

                    // Send back new data
                    if (EditorActivity.lastInstance != null) {
                        EditorActivity.lastInstance.updateRemoteId(algorithm.getRemoteId());
                    }
                }

                // Refresh recycler view
                refreshRecyclerView();
            }
        });
    }

    public Boolean isSync() {
        return algorithm.getRemoteId() != null && !algorithm.getRemoteId().equals(0);
    }

    public Boolean isPublic() {
        return loaded ? public_ : null;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public String getNotes() {
        return loaded ? notes != null ? notes : getString(R.string.cloud_no_notes) : getString(R.string.loading);
    }

    public void syncChanged(boolean enabled) {
        // Check if switch is enabled
        if (enabled) {
            // Upload
            sendMetadatas();
        } else {
            // Remove from cloud
            removeFromCloud();
        }
    }

    public void publicChanged(boolean enabled) {
        // Update value
        public_ = enabled;

        // Send metadatas
        sendMetadatas();
    }

    public void changeNotes() {
        // Create a dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()).setTitle(R.string.cloud_settings_notes_title);

        // Create a linear layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        alert.setView(linearLayout);

        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp20 = IntExtension.dpToPixel(20, getResources());

        LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fieldParams.setMargins(dp20, dp8, dp20, dp8);

        // Add notes field
        final EditText notes = new EditText(getActivity());
        notes.setLayoutParams(fieldParams);
        notes.setText(this.notes != null ? this.notes : "");
        linearLayout.addView(notes);

        // Add save button
        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Update value
                CloudSettingsFragment.this.notes = notes.getText().toString();

                // Send metadatas
                sendMetadatas();
            }
        });

        // Add cancel button
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        // Show it
        alert.create().show();
    }
}
