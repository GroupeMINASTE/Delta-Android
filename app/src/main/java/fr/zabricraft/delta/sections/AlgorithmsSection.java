package fr.zabricraft.delta.sections;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.views.AlgorithmCell;
import fr.zabricraft.delta.views.HeaderCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AlgorithmsSection extends Section {

    private final AlgorithmLoader loader;
    private int title;
    private List<Algorithm> algorithms;

    public AlgorithmsSection(int title, List<Algorithm> algorithms, AlgorithmLoader loader) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.title = title;
        this.algorithms = algorithms;
        this.loader = loader;
    }

    public int getContentItemsTotal() {
        return algorithms.size();
    }

    public View getItemView(ViewGroup parent) {
        return new AlgorithmCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        SectionedRecyclerViewAdapter.EmptyViewHolder itemHolder = (SectionedRecyclerViewAdapter.EmptyViewHolder) holder;
        Algorithm algorithm = algorithms.get(position);

        // bind your view here
        if (itemHolder.itemView instanceof AlgorithmCell) {
            ((AlgorithmCell) itemHolder.itemView).with(algorithm);
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a textView
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(title);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    // Loader interface
    public interface AlgorithmLoader {
        void load(int algorithm);
    }

}
