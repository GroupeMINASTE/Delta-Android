package fr.zabricraft.delta.sections;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.views.AlgorithmCell;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.LabelCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AlgorithmsSection extends Section {

    private final AlgorithmLoader loader;
    private final AlgorithmContainer container;
    private int title;

    public AlgorithmsSection(int title, AlgorithmContainer container, AlgorithmLoader loader) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().footerViewWillBeProvided().build());

        this.title = title;
        this.container = container;
        this.loader = loader;
    }

    public int getContentItemsTotal() {
        return container.getAlgorithms(title).size();
    }

    public View getItemView(ViewGroup parent) {
        return new AlgorithmCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext());
    }

    public View getFooterView(ViewGroup parent) {
        return title == R.string.myalgorithms ? new LabelCell(parent.getContext()) : new View(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        SectionedRecyclerViewAdapter.EmptyViewHolder itemHolder = (SectionedRecyclerViewAdapter.EmptyViewHolder) holder;
        final Algorithm algorithm = container.getAlgorithms(title).get(position);

        // bind your view here
        if (itemHolder.itemView instanceof AlgorithmCell) {
            ((AlgorithmCell) itemHolder.itemView).with(algorithm);
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // Load algorithm details
                    loader.load(algorithm.getLocalId());
                }
            });
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(title);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    public RecyclerView.ViewHolder getFooterViewHolder(View view) {
        // Check if it's a labelCell
        if (view instanceof LabelCell) {
            ((LabelCell) view).with(R.string.new_algorithm);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // Open editor for a new algorithm
                    container.startEditor(0);
                }
            });
        }

        // return an empty instance of ViewHolder for the footers of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    // Loader interface
    public interface AlgorithmLoader {
        void load(int algorithm);
    }

    // Container interface
    public interface AlgorithmContainer {
        List<Algorithm> getAlgorithms(int title);

        void startEditor(int algorithm);
    }

}
