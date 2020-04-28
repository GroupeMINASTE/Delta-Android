package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.views.EditorPreviewCell;
import fr.zabricraft.delta.views.HeaderCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class AlgorithmPreviewSection extends Section {

    private final AlgorithmPreviewContainer container;

    public AlgorithmPreviewSection(AlgorithmPreviewContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
    }

    public int getContentItemsTotal() {
        return container.getPreview() != null ? container.getPreview().size() : 0;
    }

    public View getItemView(ViewGroup parent) {
        return new EditorPreviewCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext(), false);
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EmptyViewHolder itemHolder = (EmptyViewHolder) holder;
        final EditorLine line = container.getPreview().get(position);

        // bind your view here
        if (itemHolder.itemView instanceof EditorPreviewCell) {
            ((EditorPreviewCell) itemHolder.itemView).with(line);
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(R.string.cloud_preview);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    // Container interface
    public interface AlgorithmPreviewContainer {
        List<EditorLine> getPreview();
    }
}
