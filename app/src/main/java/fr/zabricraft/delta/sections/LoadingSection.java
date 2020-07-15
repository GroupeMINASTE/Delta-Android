package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.views.LoadingCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class LoadingSection extends Section {

    private final LoadingContainer container;

    public LoadingSection(LoadingContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().build());

        this.container = container;
    }

    public int getContentItemsTotal() {
        return container.hasMore() ? 1 : 0;
    }

    public View getItemView(ViewGroup parent) {
        return new LoadingCell(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EmptyViewHolder itemHolder = (EmptyViewHolder) holder;

        // bind your view here
        if (itemHolder.itemView instanceof LoadingCell) {
            ((LoadingCell) itemHolder.itemView).with(container);
        }
    }

    public interface LoadingContainer {
        boolean hasMore();

        void loadMore();
    }

}
