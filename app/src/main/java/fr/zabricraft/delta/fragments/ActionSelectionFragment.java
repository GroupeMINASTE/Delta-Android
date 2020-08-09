package fr.zabricraft.delta.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.actions.Action;
import fr.zabricraft.delta.sections.ActionSelectorSection;
import fr.zabricraft.delta.utils.EditorLineCategory;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ActionSelectionFragment extends Fragment implements ActionSelectorSection.ActionSelectionContainer {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background));

        // Initialize sections
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        for (EditorLineCategory category : EditorLineCategory.values) {
            // Add section
            sectionAdapter.addSection(new ActionSelectorSection(this, category));
        }

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        return recyclerView;
    }

    public void selectAndClose(Action action) {
        Intent result = new Intent();
        result.putExtra("action", action);
        result.putExtra("index", getActivity().getIntent().getIntExtra("index", -1));

        getActivity().setResult(Activity.RESULT_OK, result);
        getActivity().finish();
    }

}
