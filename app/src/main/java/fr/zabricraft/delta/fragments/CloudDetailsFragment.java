package fr.zabricraft.delta.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.api.APIRequest;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.sections.AlgorithmPreviewSection;
import fr.zabricraft.delta.sections.CloudDetailsSection;
import fr.zabricraft.delta.sections.StatusSection;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.Database;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CloudDetailsFragment extends Fragment implements CloudDetailsSection.CloudDetailsContainer, AlgorithmPreviewSection.AlgorithmPreviewContainer, StatusSection.StatusContainer {

    private APIResponseStatus status = APIResponseStatus.ok;
    private APIAlgorithm algorithm;
    private Algorithm onDevice;
    private ArrayList<EditorLine> preview;

    private SwipeRefreshLayout layout;
    private RecyclerView recyclerView;

    public static CloudDetailsFragment create(APIAlgorithm apiAlgorithm) {
        Bundle args = new Bundle();
        args.putSerializable("apiAlgorithm", apiAlgorithm);

        CloudDetailsFragment fragment = new CloudDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void selectAlgorithm(APIAlgorithm algorithm) {
        // Set status to loading
        status = APIResponseStatus.loading;

        // Update recyclerView
        recyclerView.getAdapter().notifyDataSetChanged();

        // Fetch data
        algorithm.fetchMissingData(getActivity(), new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                // Update status
                CloudDetailsFragment.this.status = status;

                // Check data
                if (object instanceof JSONObject) {
                    // Read algorithm
                    APIAlgorithm data = new APIAlgorithm((JSONObject) object);

                    // Set algorithm
                    CloudDetailsFragment.this.algorithm = data;

                    // Set preview
                    preview = new ArrayList<>();
                    for (EditorLine line : data.toAlgorithm().toEditorLines()) {
                        if (line.getCategory() != EditorLineCategory.add) {
                            preview.add(line);
                        }
                    }

                    // Get it on device if exists
                    onDevice = Database.getInstance(getActivity()).getAlgorithm(-1, data.id);

                    // Update title
                    getActivity().setTitle(CloudDetailsFragment.this.algorithm.name);
                }

                // Update recyclerView
                recyclerView.getAdapter().notifyDataSetChanged();

                // End refreshing
                if (layout.isRefreshing()) {
                    layout.setRefreshing(false);
                }
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background));

        // Initialize sections
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new StatusSection(this));
        sectionAdapter.addSection(new CloudDetailsSection(this));
        sectionAdapter.addSection(new AlgorithmPreviewSection(this));

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Add refresh
        layout = new SwipeRefreshLayout(getActivity());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(recyclerView);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Load algorithm
        Object algorithm = getArguments().getSerializable("apiAlgorithm");
        if (algorithm instanceof APIAlgorithm) {
            selectAlgorithm(((APIAlgorithm) algorithm));
        }

        return layout;
    }

    public APIAlgorithm getAlgorithm() {
        return algorithm;
    }

    public Algorithm getOnDevice() {
        return onDevice;
    }

    public void refreshData() {
        selectAlgorithm(algorithm);
    }

    public void open(Algorithm algorithm) {
        // Use delegate to close cloud and open algorithm
        Intent result = new Intent();
        result.putExtra("algorithm", algorithm);
        getActivity().setResult(Activity.RESULT_OK, result);

        // Dismiss activity
        getActivity().finish();
    }

    public ArrayList<EditorLine> getPreview() {
        return preview;
    }

    public APIResponseStatus getStatus() {
        return status;
    }
}
