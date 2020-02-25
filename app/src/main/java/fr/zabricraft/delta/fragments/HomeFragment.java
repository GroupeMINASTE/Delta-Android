package fr.zabricraft.delta.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.activities.EditorActivity;
import fr.zabricraft.delta.extensions.AlgorithmExtension;
import fr.zabricraft.delta.sections.AboutSection;
import fr.zabricraft.delta.sections.AlgorithmsSection;
import fr.zabricraft.delta.sections.NewSection;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.Database;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class HomeFragment extends Fragment implements AlgorithmsSection.AlgorithmContainer {

    private List<Algorithm> myalgorithms;
    private List<Algorithm> downloads;

    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;

    private AlgorithmsSection myalgorithms_section;
    private AlgorithmsSection downloads_section;

    private Algorithm longClickAlgorithm;

    public void loadAlgorithms() {
        // Clear previous data
        myalgorithms.clear();
        downloads.clear();

        // Retrieve algorithms from database
        List<Algorithm> algorithms = Database.getInstance(getActivity()).getAlgorithms();

        // Iterate them
        for (Algorithm algorithm : algorithms) {
            // Check if owned or downloaded
            if (algorithm.isOwner()) {
                // Add to My algorithms
                myalgorithms.add(algorithm);
            } else {
                // Add to Downloads
                downloads.add(algorithm);
            }
        }

        // If downloads are empty
        if (downloads.isEmpty()) {
            // TODO: download them from API and save them
            for (Algorithm algorithm : AlgorithmExtension.defaults) {
                // Save it locally
                Database.getInstance(getActivity()).addAlgorithm(algorithm);
            }

            // Reload algorithms
            loadAlgorithms();
        }

        // Update states
        myalgorithms_section.setState(myalgorithms.size() == 0 ? Section.State.EMPTY : Section.State.LOADED);
        downloads_section.setState(downloads.size() == 0 ? Section.State.EMPTY : Section.State.LOADED);

        // Update recyclerView
        sectionAdapter.notifyDataSetChanged();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Init lists
        myalgorithms = new ArrayList<>();
        downloads = new ArrayList<>();

        // Create the view
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background));

        // Initialize sections
        myalgorithms_section = new AlgorithmsSection(R.string.myalgorithms, this, ((AlgorithmsSection.AlgorithmLoader) getActivity()));
        downloads_section = new AlgorithmsSection(R.string.downloads, this, ((AlgorithmsSection.AlgorithmLoader) getActivity()));
        sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new NewSection(this));
        sectionAdapter.addSection(myalgorithms_section);
        sectionAdapter.addSection(downloads_section);
        sectionAdapter.addSection(new AboutSection());

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Load algorithms
        loadAlgorithms();

        return recyclerView;
    }

    public List<Algorithm> getAlgorithms(int title) {
        if (title == R.string.myalgorithms) {
            return myalgorithms;
        } else {
            return downloads;
        }
    }

    public void startEditor(Algorithm algorithm) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra("algorithm", algorithm != null ? Database.getInstance(getActivity()).getAlgorithm(algorithm.getLocalId()) : null);
        startActivityForResult(intent, 667);
    }

    public void setLongClickAlgorithm(Algorithm algorithm) {
        this.longClickAlgorithm = algorithm;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 667 && resultCode == Activity.RESULT_OK) {
            // Get data from Intent
            Object algorithm = data.getSerializableExtra("algorithm");

            // Check if data is valid
            if (algorithm instanceof Algorithm) {
                // Update with new algorithm
                loadAlgorithms();
            }
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // Start editor
                startEditor(longClickAlgorithm);
                break;
            case 1:
                // Delete and refresh
                if (longClickAlgorithm != null) {
                    // Delete it from database
                    Database.getInstance(getActivity()).deleteAlgorithm(longClickAlgorithm);

                    // Update without algorithm
                    loadAlgorithms();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

}
