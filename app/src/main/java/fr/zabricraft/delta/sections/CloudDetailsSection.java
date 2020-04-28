package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.views.CloudDetailsCell;
import fr.zabricraft.delta.views.HeaderCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class CloudDetailsSection extends Section {

    private final CloudDetailsContainer container;

    public CloudDetailsSection(CloudDetailsContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
    }

    public int getContentItemsTotal() {
        return container.getAlgorithm() != null ? 1 : 0;
    }

    public View getItemView(ViewGroup parent) {
        return new CloudDetailsCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext(), false);
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        EmptyViewHolder itemHolder = (EmptyViewHolder) holder;

        // bind your view here
        if (itemHolder.itemView instanceof CloudDetailsCell) {
            ((CloudDetailsCell) itemHolder.itemView).with(container.getAlgorithm(), container.getOnDevice(), container);
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(R.string.cloud_details);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    // Container interface
    public interface CloudDetailsContainer {
        APIAlgorithm getAlgorithm();

        Algorithm getOnDevice();

        void open(Algorithm algorithm);
    }
}
