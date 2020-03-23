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

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.sections.InputsSection;
import fr.zabricraft.delta.sections.OutputsSection;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.Process;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AlgorithmFragment extends Fragment implements InputsSection.InputsContainer, OutputsSection.OutputsContainer {

    private Algorithm algorithm;
    private List<String> currentOutputs = new ArrayList<>();

    private RecyclerView recyclerView;
    private OutputsSection outputsSection;

    public static AlgorithmFragment create(Algorithm algorithm) {
        Bundle args = new Bundle();
        args.putSerializable("algorithm", algorithm);

        AlgorithmFragment fragment = new AlgorithmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void selectAlgorithm(Algorithm algorithm) {
        // Check if not null
        if (algorithm != null) {
            // Set algorithm
            this.algorithm = algorithm;

            // Set name
            getActivity().setTitle(this.algorithm.getName());

            // Update inputs
            this.algorithm.extractInputs();
            recyclerView.getAdapter().notifyDataSetChanged();

            // Update result shown on screen
            updateResult();
        }
    }

    public void inputChanged(Pair<String, String> input) {
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

            // Clear current outputs
            currentOutputs.clear();

            // Refresh the output section
            ((SectionedRecyclerViewAdapter) recyclerView.getAdapter()).notifyItemRangeRemovedFromSection(outputsSection, 0, before);

            // Execute algorithm
            algorithm.execute(getActivity(), new Algorithm.CompletionHandler() {
                @Override
                public void completionHandler(Process process) {
                    // Get outputs
                    currentOutputs = process.outputs;

                    // Refresh the output section
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            ((SectionedRecyclerViewAdapter) recyclerView.getAdapter()).notifyItemRangeInsertedInSection(outputsSection, 0, currentOutputs.size());
                        }
                    });
                }
            });
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
        sectionAdapter.addSection(new InputsSection(this));
        outputsSection = new OutputsSection(this);
        sectionAdapter.addSection(outputsSection);

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Load algorithm
        Object algorithm = getArguments().getSerializable("algorithm");
        if (algorithm instanceof Algorithm) {
            selectAlgorithm(((Algorithm) algorithm));
        }

        return recyclerView;
    }

    public List<Pair<String, String>> getInputs() {
        return algorithm != null ? algorithm.getInputs() : new ArrayList<Pair<String, String>>();
    }

    public List<String> getOutputs() {
        return currentOutputs;
    }

}
