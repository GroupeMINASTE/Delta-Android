package fr.zabricraft.delta.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.actions.RootAction;
import fr.zabricraft.delta.sections.EditorLinesSection;
import fr.zabricraft.delta.sections.SettingsSection;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.Database;
import fr.zabricraft.delta.utils.EditorLine;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class EditorFragment extends Fragment implements SettingsSection.SettingsContainer, EditorLinesSection.EditorLinesContainer {

    private Algorithm algorithm;

    private RecyclerView recyclerView;

    public static EditorFragment create(int algorithm) {
        Bundle args = new Bundle();
        args.putInt("id", algorithm);

        EditorFragment fragment = new EditorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void selectAlgorithm(int id) {
        // Get algorithm from database
        if (id != 0) {
            algorithm = Database.getInstance(getActivity()).getAlgorithm(id);
        } else {
            algorithm = null;
        }

        // Check if null
        if (algorithm == null) {
            // Create a new algorithm
            algorithm = new Algorithm(0, null, true, getString(R.string.new_algorithm), new Date(), new RootAction());
        } else {
            // Clone for editing
            algorithm = algorithm.clone(getActivity());
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
        selectAlgorithm(getArguments().getInt("id"));

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

}
