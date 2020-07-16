package fr.zabricraft.delta.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.api.APIRequest;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.sections.APIAlgorithmsSection;
import fr.zabricraft.delta.sections.LoadingSection;
import fr.zabricraft.delta.sections.StatusSection;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CloudHomeFragment extends Fragment implements APIAlgorithmsSection.APIAlgorithmsContainer, StatusSection.StatusContainer, LoadingSection.LoadingContainer {

    private APIResponseStatus status = APIResponseStatus.ok;
    private List<APIAlgorithm> algorithms;
    private boolean loading = false;
    private boolean hasMore = true;
    private String search = "";

    private SwipeRefreshLayout layout;
    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;

    public void loadAlgorithms() {
        loadAlgorithms(false);
    }

    public void loadAlgorithms(boolean reset) {
        // Reset content if needed
        if (reset) {
            this.hasMore = true;
        }

        // Check that it is not already loading
        if (loading || !hasMore) { return; }
        loading = true;

        // Load algorithms from API
        new APIRequest("GET", "/algorithm/search.php", getActivity(), (object, status) -> {
            // Reset if needed
            if (reset) {
                algorithms.clear();
            }

            // Check data
            if (object instanceof JSONArray) {
                JSONArray array = (JSONArray) object;

                // Check content size
                if (array.length() > 0) {
                    // Update data
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            // Get JSONObject and decode it
                            algorithms.add(new APIAlgorithm(array.getJSONObject(i)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // No more content
                    hasMore = false;
                }
            }

            // No more loading
            loading = false;

            // Refresh recyclerView
            reloadData(status);
        }).with("search", search).with("start", reset ? 0 : algorithms.size()).execute();
    }

    public void reloadData(APIResponseStatus status) {
        // Reload recyclerView
        this.status = status;
        sectionAdapter.notifyDataSetChanged();

        // End refreshing
        if (layout.isRefreshing()) {
            layout.setRefreshing(false);
        }
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
        sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new StatusSection(this));
        sectionAdapter.addSection(new APIAlgorithmsSection(this, ((APIAlgorithmsSection.APIAlgorithmLoader) getActivity())));
        sectionAdapter.addSection(new LoadingSection(this));

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Add refresh
        layout = new SwipeRefreshLayout(getActivity());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(recyclerView);
        layout.setOnRefreshListener(() -> loadAlgorithms(true));

        // Load algorithms
        loadAlgorithms(true);

        return layout;
    }

    public List<APIAlgorithm> getAlgorithms() {
        return algorithms;
    }

    public void search(String search) {
        // Update search
        this.search = search;

        // Reload algorithms
        loadAlgorithms(true);
    }

    public APIResponseStatus getStatus() {
        return status;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public void loadMore() {
        loadAlgorithms();
    }
}
