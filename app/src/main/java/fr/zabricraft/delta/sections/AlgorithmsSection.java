package fr.zabricraft.delta.sections;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.views.AlgorithmItemViewHolder;
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
        return new TextView(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new TextView(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new AlgorithmItemViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlgorithmItemViewHolder itemHolder = (AlgorithmItemViewHolder) holder;
        Algorithm algorithm = algorithms.get(position);

        // bind your view here
        if (itemHolder.itemView instanceof TextView) {
            ((TextView) itemHolder.itemView).setText(algorithm.getName());
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a textView
        if (view instanceof TextView) {
            ((TextView) view).setText(title);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    // Loader interface
    public interface AlgorithmLoader {
        void load(int algorithm);
    }

}
