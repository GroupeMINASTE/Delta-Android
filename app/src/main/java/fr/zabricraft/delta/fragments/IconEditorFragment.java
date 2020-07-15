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
import fr.zabricraft.delta.sections.IconSection;
import fr.zabricraft.delta.utils.AlgorithmIcon;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class IconEditorFragment extends Fragment implements IconSection.IconContainer {

    private SectionedRecyclerViewAdapter sectionAdapter;

    private AlgorithmIcon icon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background));

        // Initialize sections
        sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new IconSection(this, R.string.icon_image));
        sectionAdapter.addSection(new IconSection(this, R.string.icon_color));

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Retrieve icon
        Object object = getActivity().getIntent().getSerializableExtra("icon");
        if (object instanceof AlgorithmIcon) {
            this.icon = (AlgorithmIcon) object;
        }

        return recyclerView;
    }

    public String getValue(int type, int position) {
        return type == R.string.icon_image ? AlgorithmIcon.valuesIcon[position] : AlgorithmIcon.valuesColor[position];
    }

    public boolean isValueSelected(int type, int position) {
        return type == R.string.icon_image ? icon.getIcon().equals(AlgorithmIcon.valuesIcon[position]) : icon.getColor().equals(AlgorithmIcon.valuesColor[position]);
    }

    public void selectValue(int type, int position) {
        if (type == R.string.icon_image) {
            icon.setIcon(AlgorithmIcon.valuesIcon[position]);
        } else {
            icon.setColor(AlgorithmIcon.valuesColor[position]);
        }

        sectionAdapter.notifyDataSetChanged();
    }

    public void saveAndClose() {
        // Give new algorithm as result
        Intent result = new Intent();
        result.putExtra("icon", icon);
        getActivity().setResult(Activity.RESULT_OK, result);

        // Dismiss activity
        getActivity().finish();
    }
}
