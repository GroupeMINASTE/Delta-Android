package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.RightDetailCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class CloudOtherSection extends Section {

    private final CloudOtherContainer container;

    public CloudOtherSection(CloudOtherContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
    }

    public int getContentItemsTotal() {
        return 1;
    }

    public View getItemView(ViewGroup parent) {
        return new RightDetailCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EmptyViewHolder itemHolder = (EmptyViewHolder) holder;

        // bind your view here
        if (itemHolder.itemView instanceof RightDetailCell) {
            ((RightDetailCell) itemHolder.itemView).with(R.string.cloud_settings_notes, container.getNotes());
            itemHolder.itemView.setOnClickListener(view -> {
                // Check that content is loaded before
                if (!container.isLoaded()) { return; }

                // Notes
                if (position == 0) {
                    container.changeNotes();
                }
            });
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(R.string.cloud_settings_other_title);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    public interface CloudOtherContainer {
        boolean isLoaded();

        String getNotes();

        void changeNotes();
    }

}
