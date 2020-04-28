package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.HomeCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class APIAlgorithmsSection extends Section {

    private final APIAlgorithmLoader loader;
    private final APIAlgorithmsContainer container;

    public APIAlgorithmsSection(APIAlgorithmsContainer container, APIAlgorithmLoader loader) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
        this.loader = loader;
    }

    public int getContentItemsTotal() {
        return container.getAlgorithms().size();
    }

    public View getItemView(ViewGroup parent) {
        return new HomeCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        EmptyViewHolder itemHolder = (EmptyViewHolder) holder;
        final APIAlgorithm apiAlgorithm = container.getAlgorithms().get(position);

        // bind your view here
        if (itemHolder.itemView instanceof HomeCell) {
            ((HomeCell) itemHolder.itemView).with(apiAlgorithm);
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // Load algorithm details
                    loader.load(apiAlgorithm);
                }
            });
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(R.string.cloud_algorithms);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    // Loader interface
    public interface APIAlgorithmLoader {
        void load(APIAlgorithm apiAlgorithm);
    }

    // Container interface
    public interface APIAlgorithmsContainer {
        List<APIAlgorithm> getAlgorithms();
    }
}
