package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.LabelCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class NewSection extends Section {

    private final AlgorithmsSection.AlgorithmsContainer container;

    public NewSection(AlgorithmsSection.AlgorithmsContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
    }

    public int getContentItemsTotal() {
        return 3;
    }

    public View getItemView(ViewGroup parent) {
        return new LabelCell(parent.getContext());
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
        if (itemHolder.itemView instanceof LabelCell) {
            ((LabelCell) itemHolder.itemView).with(position == 0 ? R.string.new_algorithm : position == 1 ? R.string.download_algorithm : R.string.my_account);
            itemHolder.itemView.setOnClickListener(view -> {
                if (position == 0) {
                    // Open editor for a new algorithm
                    container.startEditor(null);
                } else if (position == 1) {
                    // Open the cloud
                    container.openCloud();
                } else {
                    // Open account
                    container.openAccount();
                }
            });
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(R.string.new_algorithm);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

}
