package fr.zabricraft.delta.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.api.APIRequest;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.sections.APIAlgorithmsSection;
import fr.zabricraft.delta.sections.StatusSection;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CloudHomeFragment extends Fragment implements APIAlgorithmsSection.APIAlgorithmsContainer, StatusSection.StatusContainer {

    private APIResponseStatus status = APIResponseStatus.ok;
    private List<APIAlgorithm> algorithms;
    private String search = "";

    private SwipeRefreshLayout layout;
    private RecyclerView recyclerView;

    public void loadAlgorithms() {
        // Load algorithms from API
        new APIRequest("GET", "/algorithm/search.php", getActivity(), new APIRequest.CompletionHandler() {
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
                } else {
                    // Clear data
                    algorithms.clear();
                }

                // Refresh recyclerView
                reloadData(status);
            }
        }).with("search", search).execute();
    }

    public void reloadData(APIResponseStatus status) {
        // Reload recyclerView
        this.status = status;
        recyclerView.getAdapter().notifyDataSetChanged();

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
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new StatusSection(this));
        sectionAdapter.addSection(new APIAlgorithmsSection(this, ((APIAlgorithmsSection.APIAlgorithmLoader) getActivity())));

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Add refresh
        layout = new SwipeRefreshLayout(getActivity());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(recyclerView);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAlgorithms();
            }
        });

        // Load algorithms
        loadAlgorithms();

        return layout;
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

    public APIResponseStatus getStatus() {
        return status;
    }
}
