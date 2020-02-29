package fr.zabricraft.delta.utils;

import android.content.Context;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.actions.Action;
import fr.zabricraft.delta.actions.ActionBlock;
import fr.zabricraft.delta.actions.RootAction;

public class Algorithm implements Serializable {

    // Properties
    private int local_id;
    private Integer remote_id;
    private boolean owner;
    private String name;
    private Date last_update;
    private AlgorithmIcon icon;
    private List<Pair<String, String>> inputs;
    private RootAction root;

    // Initializer

    public Algorithm(int local_id, Integer remote_id, boolean owner, String name, Date last_update, AlgorithmIcon icon, RootAction root) {
        // Init values
        this.local_id = local_id;
        this.remote_id = remote_id;
        this.name = name;
        this.owner = owner;
        this.last_update = last_update;
        this.icon = icon;
        this.inputs = new ArrayList<>();
        this.root = root;

        // Extract inputs from actions
        this.extractInputs();
    }

    // Getters and setters

    public int getLocalId() {
        return local_id;
    }

    public void setLocalId(int local_id) {
        this.local_id = local_id;
    }

    public Integer getRemoteId() {
        return remote_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwner() {
        return owner;
    }

    public AlgorithmIcon getIcon() {
        return icon;
    }

    public void setIcon(AlgorithmIcon icon) {
        this.icon = icon;
    }

    public Date getLastUpdate() {
        return last_update;
    }

    public void setLastUpdate(Date last_update) {
        this.last_update = last_update;
    }

    public List<Pair<String, String>> getInputs() {
        return inputs;
    }

    public RootAction getRoot() {
        return root;
    }

    // Inputs

    public void extractInputs() {
        // Set inputs from root
        this.inputs = root.extractInputs();
    }

    // Execute

    public Process execute(Context context) {
        // Create a process with inputs
        Process process = new Process(inputs, context);

        // Execute root
        root.execute(process);

        // Return the process
        return process;
    }

    // Export

    public String toString() {
        return root.toString();
    }

    // Actions editor lines

    public List<EditorLine> toEditorLines() {
        return root.toEditorLines();
    }

    public int editorLinesCount() {
        return root.editorLinesCount();
    }

    public Triplet<Action, Action, Integer> action(int index) {
        return root.action(index, root, 0);
    }

    public Pair<Integer, Integer> insert(Action action, int index) {
        // Get where to add it
        Triplet<Action, Action, Integer> result = action(index);

        // Check if we have a block to add it
        if (result.getValue1() instanceof ActionBlock) {
            // Add it
            ((ActionBlock) result.getValue1()).insert(action, result.getValue2());

            // Return the range of new lines
            return Pair.with(index, index + action.editorLinesCount());
        }

        // Not added
        return Pair.with(0, 0);
    }

    public void update(EditorLine line, int index) {
        // Check settings
        if (line.getCategory().equals(EditorLineCategory.settings)) {
            // Update settings
            updateSettings(index, line.getValues());
        } else {
            // Update actions
            action(index).getValue0().update(line);
        }
    }

    public Pair<Integer, Integer> delete(int index) {
        // Get where to add it
        Triplet<Action, Action, Integer> result = action(index);

        // Check if we have a block to delete it
        if (result.getValue1() instanceof ActionBlock) {
            // Delete it
            ((ActionBlock) result.getValue1()).delete(result.getValue2());

            // Return the range of old lines
            return Pair.with(index, index + result.getValue0().editorLinesCount());
        }

        // Not deleted
        return Pair.with(0, 0);
    }

    public Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> move(int fromIndex, int toIndex) {
        // Check that index is different
        if (fromIndex != toIndex) {
            // Check if destination is not in the deleted range
            Triplet<Action, Action, Integer> sourceAction = this.action(fromIndex);
            Pair<Integer, Integer> sourceRange = Pair.with(fromIndex, fromIndex + sourceAction.getValue0().editorLinesCount());
            if (toIndex < sourceRange.getValue0() || toIndex >= sourceRange.getValue1()) {
                // Delete the old line
                Pair<Integer, Integer> range = delete(fromIndex);

                // Calculate new destination
                int destination = toIndex < fromIndex ? toIndex : toIndex - (range.getValue1() - range.getValue0()) + 1;

                // Get action at destination
                if (destination > 0) {
                    EditorLine currentDestination = this.root.toEditorLines().get(destination - 1);
                    if (currentDestination.getCategory() == EditorLineCategory.add) {
                        // Remove one to skip add button
                        destination--;
                    }
                }

                // Add the new line
                Pair<Integer, Integer> newRange = insert(sourceAction.getValue0(), destination);

                // Return modified ranges
                return Pair.with(range, newRange);
            }
        }

        // Not moved
        return Pair.with(Pair.with(0, 0), Pair.with(0, 0));
    }

    // Settings editor lines

    public List<EditorLine> getSettings() {
        List<EditorLine> list = new ArrayList<>();
        list.add(new EditorLine(R.string.settings_name, EditorLineCategory.settings, 0, new String[]{name}, false));
        return list;
    }

    public int settingsCount() {
        return 1;
    }

    public void updateSettings(int index, String[] values) {
        // Check index
        if (index == 0 && values.length == 1) {
            // Index 0 - Algorithm's name
            this.name = values[0];
        }
    }

    // Clone algorithm to edit

    public Algorithm clone(Context context) {
        // Check if owned
        if (owner) {
            // Create an instance with same information
            return new Algorithm(local_id, remote_id, true, name, last_update, icon, root);
        } else {
            // Create a copy
            return new Algorithm(0, null, true, context.getResources().getString(R.string.copy, name), last_update, icon, root);
        }
    }

}
