package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.views.EditorCell;
import fr.zabricraft.delta.views.HeaderCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class SettingsSection extends Section {

    private final SettingsContainer container;

    public SettingsSection(SettingsContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
    }

    public int getContentItemsTotal() {
        return container.settingsCount();
    }

    public View getItemView(ViewGroup parent) {
        return new EditorCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext(), false);
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EmptyViewHolder itemHolder = (EmptyViewHolder) holder;
        EditorLine line = container.getSettings().get(position);

        // bind your view here
        if (itemHolder.itemView instanceof EditorCell) {
            ((EditorCell) itemHolder.itemView).with(line, container, position);
            itemHolder.itemView.setOnClickListener(view -> {
                if (line.getFormat() == R.string.settings_icon) {
                    // Open icon editor
                    container.openIconEditor();
                } else if (line.getFormat() == R.string.settings_cloud) {
                    // Open cloud settings
                    container.openCloudSettings();
                }
            });
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(R.string.settings);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    // Container interface
    public interface SettingsContainer extends EditorLinesSection.EditorLinesContainer {
        List<EditorLine> getSettings();

        int settingsCount();

        void openIconEditor();

        void openCloudSettings();
    }

}
