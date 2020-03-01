package fr.zabricraft.delta.sections;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.views.StatusCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class StatusSection extends Section {

    private final StatusContainer container;

    public StatusSection(StatusContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().build());

        this.container = container;
    }

    public int getContentItemsTotal() {
        return container.getStatus().text() != 0 ? 1 : 0;
    }

    public View getItemView(ViewGroup parent) {
        return new StatusCell(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SectionedRecyclerViewAdapter.EmptyViewHolder itemHolder = (SectionedRecyclerViewAdapter.EmptyViewHolder) holder;

        // bind your view here
        if (itemHolder.itemView instanceof StatusCell) {
            ((StatusCell) itemHolder.itemView).with(container.getStatus().text());
        }
    }

    // Container interface
    public interface StatusContainer {
        APIResponseStatus getStatus();
    }

}
