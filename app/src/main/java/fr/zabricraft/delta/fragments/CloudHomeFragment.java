package fr.zabricraft.delta.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.api.APIRequest;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.sections.APIAlgorithmsSection;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CloudHomeFragment extends Fragment implements APIAlgorithmsSection.APIAlgorithmsContainer {

    private List<APIAlgorithm> algorithms;
    private String search = "";

    private RecyclerView recyclerView;

    public void loadAlgorithms() {
        // Load algorithms from API
        new APIRequest("GET", "/algorithm/search.php", new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                if (object instanceof JSONArray) {
                    JSONArray array = (JSONArray) object;

                    // Update data
                    algorithms.clear();
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            // Get JSONObject and decode it
                            algorithms.add(new APIAlgorithm(array.getJSONObject(i)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Refresh recyclerView
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }).with("search", search).execute();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Init lists
        algorithms = new ArrayList<>();

        // Create the view
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background));

        // Initialize sections
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new APIAlgorithmsSection(this, ((APIAlgorithmsSection.APIAlgorithmLoader) getActivity())));

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Load algorithms
        loadAlgorithms();

        return recyclerView;
    }

    public List<APIAlgorithm> getAlgorithms() {
        return algorithms;
    }

    public void search(String search) {
        // Update search
        this.search = search;

        // Reload algorithms
        loadAlgorithms();
    }
}
