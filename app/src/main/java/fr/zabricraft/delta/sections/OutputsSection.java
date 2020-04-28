package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.OutputCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class OutputsSection extends Section {

    private final OutputsContainer container;

    public OutputsSection(OutputsContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
    }

    public int getContentItemsTotal() {
        return container.getOutputs().size();
    }

    public View getItemView(ViewGroup parent) {
        return new OutputCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        EmptyViewHolder itemHolder = (EmptyViewHolder) holder;
        Object output = container.getOutputs().get(position);

        // bind your view here
        if (itemHolder.itemView instanceof OutputCell && output instanceof String) {
            ((OutputCell) itemHolder.itemView).with((String) output);
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(R.string.outputs);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    // Container interface
    public interface OutputsContainer {
        List<Object> getOutputs();
    }

}
