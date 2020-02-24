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
    private final AlgorithmContainer container;
    private int title;

    public AlgorithmsSection(int title, AlgorithmContainer container, AlgorithmLoader loader) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

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
                    loader.load(algorithm);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    container.setLongClickAlgorithm(algorithm);
                    return false;
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

    // Loader interface
    public interface AlgorithmLoader {
        void load(Algorithm algorithm);
    }

    // Container interface
    public interface AlgorithmContainer {
        List<Algorithm> getAlgorithms(int title);

        void startEditor(Algorithm algorithm);

        void setLongClickAlgorithm(Algorithm algorithm);
    }

}
