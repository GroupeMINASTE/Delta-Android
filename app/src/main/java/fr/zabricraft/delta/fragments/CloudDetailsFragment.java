package fr.zabricraft.delta.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.api.APIRequest;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.sections.CloudDetailsSection;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CloudDetailsFragment extends Fragment implements CloudDetailsSection.CloudDetailsContainer {

    private APIAlgorithm algorithm;
    private APIResponseStatus status = APIResponseStatus.ok;

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
        algorithm.fetchMissingData(new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                // Update status
                CloudDetailsFragment.this.status = status;

                // Check data
                if (object instanceof JSONObject) {
                    // Set algorithm
                    CloudDetailsFragment.this.algorithm = new APIAlgorithm((JSONObject) object);

                    // Update title
                    getActivity().setTitle(CloudDetailsFragment.this.algorithm.name);
                }

                // Update recyclerView
                recyclerView.getAdapter().notifyDataSetChanged();
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
        sectionAdapter.addSection(new CloudDetailsSection(this));

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Load algorithm
        Object algorithm = getArguments().getSerializable("apiAlgorithm");
        if (algorithm instanceof APIAlgorithm) {
            selectAlgorithm(((APIAlgorithm) algorithm));
        }

        return recyclerView;
    }

    @Override
    public APIAlgorithm getAlgorithm() {
        return algorithm;
    }
}
