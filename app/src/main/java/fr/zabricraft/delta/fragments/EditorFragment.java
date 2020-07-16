package fr.zabricraft.delta.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.actions.Action;
import fr.zabricraft.delta.actions.RootAction;
import fr.zabricraft.delta.activities.ActionSelectionActivity;
import fr.zabricraft.delta.activities.CloudSettingsActivity;
import fr.zabricraft.delta.activities.IconEditorActivity;
import fr.zabricraft.delta.sections.EditorLinesSection;
import fr.zabricraft.delta.sections.SettingsSection;
import fr.zabricraft.delta.utils.Account;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.AlgorithmIcon;
import fr.zabricraft.delta.utils.Database;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.ItemMoveCallback;
import fr.zabricraft.delta.views.EditorCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class EditorFragment extends Fragment implements SettingsSection.SettingsContainer, EditorLinesSection.EditorLinesContainer, ItemMoveCallback.ItemTouchHelperContract {

    private Algorithm algorithm;

    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;

    private EditorLinesSection editorLinesSection;

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
            this.algorithm = new Algorithm(0, null, true, getString(R.string.new_algorithm), new Date(), new AlgorithmIcon(), new RootAction());
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
        editorLinesSection = new EditorLinesSection(this);
        sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(new SettingsSection(this));
        sectionAdapter.addSection(editorLinesSection);

        // Bind adapter to recyclerView
        recyclerView.setAdapter(sectionAdapter);

        // Add support for drag and drop
        ItemTouchHelper.Callback callback = new ItemMoveCallback(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

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
            sectionAdapter.notifyDataSetChanged();
        }
    }

    public void editorLineDeleted(EditorLine line, int index) {
        if (algorithm != null) {
            // Delete the line into algorithm
            Pair<Integer, Integer> range = algorithm.delete(index);

            // Delete old rows
            sectionAdapter.notifyDataSetChanged();
        }
    }

    public void editorLineMoved(int fromIndex, int toIndex) {
        if (algorithm != null) {
            // Add the line into algorithm
            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> ranges = algorithm.move(fromIndex, toIndex);

            // Delete old rows
            sectionAdapter.notifyDataSetChanged();
        }
    }

    public void saveAndClose() {
        // Update last update
        algorithm.setLastUpdate(new Date());

        // Save algorithm to database
        Algorithm newAlgorithm = Database.getInstance(getActivity()).updateAlgorithm(algorithm);

        // Give new algorithm as result
        Intent result = new Intent();
        result.putExtra("algorithm", newAlgorithm);
        getActivity().setResult(Activity.RESULT_OK, result);

        // Dismiss activity
        getActivity().finish();
    }

    public void openActionSelection(int index) {
        Intent intent = new Intent(getActivity(), ActionSelectionActivity.class);
        intent.putExtra("index", index);
        startActivityForResult(intent, 666);
    }

    public void openIconEditor() {
        Intent intent = new Intent(getActivity(), IconEditorActivity.class);
        intent.putExtra("icon", algorithm.getIcon());
        startActivityForResult(intent, 665);
    }

    public void openCloudSettings() {
        // Check if user is connected
        if (Account.current.access_token != null) {
            // Open cloud sharing settings
            // TODO: Open with ability to send back algorithm
            Intent intent = new Intent(getActivity(), CloudSettingsActivity.class);
            intent.putExtra("algorithm", algorithm);
            startActivity(intent);
        } else {
            // User is not logged in
            new AlertDialog.Builder(getActivity()).setTitle(R.string.settings_cloud_error).setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            }).create().show();
        }
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
        } else if (requestCode == 665 && resultCode == Activity.RESULT_OK) {
            /// Get data from Intent
            Object icon = data.getSerializableExtra("icon");

            // Check if data is valid
            if (icon instanceof AlgorithmIcon) {
                algorithm.setIcon((AlgorithmIcon) icon);
            }
        }
    }

    public boolean canDropOver(int toPosition) {
        return sectionAdapter.getSectionForPosition(toPosition) == editorLinesSection;
    }

    public boolean onRowMoved(int fromPosition, int toPosition) {
        // Check if section is correct
        if (sectionAdapter.getSectionForPosition(fromPosition) == editorLinesSection && sectionAdapter.getSectionForPosition(toPosition) == editorLinesSection) {
            // Get indexes
            int fromIndex = sectionAdapter.getPositionInSection(fromPosition);
            int toIndex = sectionAdapter.getPositionInSection(toPosition);

            // Get editor line
            EditorLine line = algorithm.toEditorLines().get(fromIndex);

            // Check if can be moved
            if (line.isMovable()) {
                // Move lines
                editorLineMoved(fromIndex, toIndex);

                // Return true
                return true;
            }
        }

        // Line was not moved
        return false;
    }

    public void onRowSelected(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.itemView instanceof EditorCell) {
            // Scale the cell
            ((EditorCell) viewHolder.itemView).animate().scaleX(0.9f).scaleY(0.9f).setDuration(500);
        }
    }

    public void onRowClear(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.itemView instanceof EditorCell) {
            // Scale the cell
            ((EditorCell) viewHolder.itemView).animate().scaleX(1f).scaleY(1f).setDuration(500);
        }
    }

}
