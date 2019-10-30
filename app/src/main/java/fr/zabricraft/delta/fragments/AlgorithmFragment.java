package fr.zabricraft.delta.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

import fr.zabricraft.delta.sections.InputsSection;
import fr.zabricraft.delta.sections.OutputsSection;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.Database;
import fr.zabricraft.delta.utils.Process;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AlgorithmFragment extends Fragment implements InputsSection.InputsContainer, OutputsSection.OutputsContainer {

    private Algorithm algorithm;
    private List<String> currentOutputs = new ArrayList<>();

    private RecyclerView recyclerView;
    private OutputsSection outputsSection;

    public static AlgorithmFragment create(int algorithm) {
        Bundle args = new Bundle();
        args.putInt("id", algorithm);

        AlgorithmFragment fragment = new AlgorithmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void selectAlgorithm(int id) {
        // Get algorithm from database
        algorithm = Database.getInstance(getActivity()).getAlgorithm(id);

        // Check if not null
        if (algorithm != null) {
            // Set name
            getActivity().setTitle(algorithm.getName());

            // Update inputs
            algorithm.extractInputs();
            recyclerView.getAdapter().notifyDataSetChanged();

            // Update result shown on screen
            updateResult();
        }
    }

    public void inputChanged(Pair<String, Token> input) {
        // Get vars
        if (algorithm != null && input != null) {
            // Update the input
            for (int i = 0; i < algorithm.getInputs().size(); i++) {
                // Check key
                if (algorithm.getInputs().get(i).getValue0().equals(input.getValue0())) {
                    // Set value
                    algorithm.getInputs().remove(i);
                    algorithm.getInputs().add(i, input);
                }
            }

            // Update result shown on screen
            updateResult();
        }
    }

    public void updateResult() {
        if (algorithm != null) {
            // Count outputs before
            int before = currentOutputs.size();

            // Execute algorithm
            Process process = algorithm.execute(getActivity());
            currentOutputs = process.outputs;

            // Refresh the output section
            ((SectionedRecyclerViewAdapter) recyclerView.getAdapter()).notifyItemRangeRemovedFromSection(outputsSection, 0, before);
            ((SectionedRecyclerViewAdapter) recyclerView.getAdapter()).notifyItemRangeInsertedInSection(outputsSection, 0, currentOutputs.size());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        // Initialize sections
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new InputsSection(this));
        outputsSection = new OutputsSection(this);
        sectionAdapter.addSection(outputsSection);

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Load algorithm
        selectAlgorithm(getArguments().getInt("id"));

        return recyclerView;
    }

    public List<Pair<String, Token>> getInputs() {
        if (algorithm != null) {
            // Return algorithms inputs
            return algorithm.getInputs();
        }

        return new ArrayList<>();
    }

    public List<String> getOutputs() {
        return currentOutputs;
    }

}
