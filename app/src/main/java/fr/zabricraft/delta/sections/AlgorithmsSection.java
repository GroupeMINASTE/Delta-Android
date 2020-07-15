package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.HomeCell;
import fr.zabricraft.delta.views.LabelCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class AlgorithmsSection extends Section {

    private final AlgorithmLoader loader;
    private final AlgorithmsContainer container;
    private int title;

    public AlgorithmsSection(int title, AlgorithmsContainer container, AlgorithmLoader loader) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().emptyViewWillBeProvided().build());

        this.title = title;
        this.container = container;
        this.loader = loader;
    }

    public int getContentItemsTotal() {
        return container.getAlgorithms(title).size();
    }

    public View getItemView(ViewGroup parent) {
        return new HomeCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext());
    }

    public View getEmptyView(ViewGroup parent) {
        return new LabelCell(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        EmptyViewHolder itemHolder = (EmptyViewHolder) holder;
        final Algorithm algorithm = container.getAlgorithms(title).get(position);

        // bind your view here
        if (itemHolder.itemView instanceof HomeCell) {
            ((HomeCell) itemHolder.itemView).with(algorithm);
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
        return new EmptyViewHolder(view);
    }

    public RecyclerView.ViewHolder getEmptyViewHolder(View view) {
        // Check if it's a labelCell
        if (view instanceof LabelCell) {
            ((LabelCell) view).with(R.string.no_algorithm);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    // Loader interface
    public interface AlgorithmLoader {
        void load(Algorithm algorithm);
    }

    // Container interface
    public interface AlgorithmsContainer {
        List<Algorithm> getAlgorithms(int title);

        void startEditor(Algorithm algorithm);

        void setLongClickAlgorithm(Algorithm algorithm);

        void openCloud();

        void openAccount();
    }

}
