package fr.zabricraft.delta.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.actions.Action;
import fr.zabricraft.delta.actions.RootAction;
import fr.zabricraft.delta.activities.ActionSelectionActivity;
import fr.zabricraft.delta.sections.EditorLinesSection;
import fr.zabricraft.delta.sections.SettingsSection;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.Database;
import fr.zabricraft.delta.utils.EditorLine;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class EditorFragment extends Fragment implements SettingsSection.SettingsContainer, EditorLinesSection.EditorLinesContainer {

    private Algorithm algorithm;

    private RecyclerView recyclerView;

    public static EditorFragment create(Algorithm algorithm) {
        Bundle args = new Bundle();
        args.putSerializable("algorithm", algorithm);

        EditorFragment fragment = new EditorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void selectAlgorithm(Algorithm algorithm) {
        // Check if null
        if (algorithm == null) {
            // Create a new algorithm
            this.algorithm = new Algorithm(0, null, true, getString(R.string.new_algorithm), new Date(), new RootAction());
        } else {
            // Clone for editing
            this.algorithm = algorithm.clone(getActivity());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background));

        // Initialize sections
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new SettingsSection(this));
        sectionAdapter.addSection(new EditorLinesSection(this));

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Load algorithm
        Object algorithm = getArguments().getSerializable("algorithm");
        if (algorithm instanceof Algorithm) {
            selectAlgorithm(((Algorithm) algorithm));
        } else {
            selectAlgorithm(null);
        }

        return recyclerView;
    }

    public List<EditorLine> getSettings() {
        return algorithm != null ? algorithm.getSettings() : new ArrayList<EditorLine>();
    }

    public int settingsCount() {
        return algorithm != null ? algorithm.settingsCount() : 0;
    }

    public List<EditorLine> toEditorLines() {
        return algorithm != null ? algorithm.toEditorLines() : new ArrayList<EditorLine>();
    }

    public int editorLinesCount() {
        return algorithm != null ? algorithm.editorLinesCount() : 0;
    }

    public void editorLineChanged(EditorLine line, int index) {
        if (line != null && algorithm != null) {
            algorithm.update(line, index);
        }
    }

    public void editorLineAdded(Action action, int index) {
        if (algorithm != null) {
            // Add the line into algorithm
            Pair<Integer, Integer> range = algorithm.insert(action, index);

            // Insert new rows
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void editorLineDeleted(EditorLine line, int index) {
        if (line != null && algorithm != null) {
            // Delete the line into algorithm
            Pair<Integer, Integer> range = algorithm.delete(line, index);

            // Delete old rows
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void saveAndClose() {
        // Save algorithm to database
        Algorithm newAlgorithm = Database.getInstance(getActivity()).updateAlgorithm(algorithm);

        // Call completion handler
        // TODO: Refresh activities with new data

        // Dismiss activity
        getActivity().finish();
    }

    public void openActionSelection(int index) {
        Intent intent = new Intent(getActivity(), ActionSelectionActivity.class);
        intent.putExtra("index", index);
        startActivityForResult(intent, 666);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 666 && resultCode == Activity.RESULT_OK) {
            // Get data from Intent
            Object action = data.getSerializableExtra("action");
            int index = data.getIntExtra("index", -1);

            // Check if data is valid
            if (action instanceof Action && index != -1) {
                // Add action to algorithm
                editorLineAdded(((Action) action), index);
            }
        }
    }
}
