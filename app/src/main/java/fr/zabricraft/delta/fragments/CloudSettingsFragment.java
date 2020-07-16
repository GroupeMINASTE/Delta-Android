package fr.zabricraft.delta.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.api.APIRequest;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.sections.CloudSwitchSection;
import fr.zabricraft.delta.utils.Algorithm;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CloudSettingsFragment extends Fragment implements CloudSwitchSection.CloudSwitchContainer {

    private SectionedRecyclerViewAdapter sectionAdapter;
    private CloudSwitchSection publicSection;

    private Algorithm algorithm;
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
        sectionAdapter.addSection(new CloudSwitchSection(R.string.cloud_settings_sync_title, this));
        sectionAdapter.addSection(publicSection);

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
        sectionAdapter.notifyDataSetChanged();
    }

    public void fetchMetadatas() {
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
        algorithm.toAPIAlgorithm(public_, notes).upload(getActivity(), new APIRequest.CompletionHandler() {
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
                    // TODO: ???
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
        return public_;
    }

    public void syncChanged(boolean enabled) {
        // Check if switch is enabled
        if (enabled) {
            // Upload
            sendMetadatas();
        }
    }

    public void publicChanged(boolean enabled) {
        // Update value
        public_ = enabled;

        // Send metadatas
        sendMetadatas();
    }

}
