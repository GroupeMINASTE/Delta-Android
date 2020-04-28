package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.views.StatusCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

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
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EmptyViewHolder itemHolder = (EmptyViewHolder) holder;

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
