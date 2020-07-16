package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.AlgorithmIcon;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.IconEditorCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class IconSection extends Section {

    private final IconContainer container;
    private final int type;

    public IconSection(IconContainer container, int type) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
        this.type = type;
    }

    public int getContentItemsTotal() {
        return type == R.string.icon_image ? AlgorithmIcon.valuesIcon.length : AlgorithmIcon.valuesColor.length;
    }

    public View getItemView(ViewGroup parent) {
        return new IconEditorCell(parent.getContext());
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
        final String value = container.getValue(type, position);

        // bind your view here
        if (itemHolder.itemView instanceof IconEditorCell) {
            ((IconEditorCell) itemHolder.itemView).with(type, value, container.isValueSelected(type, position));
            itemHolder.itemView.setOnClickListener(view -> container.selectValue(type, position));
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(type);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    // Container interface
    public interface IconContainer {
        String getValue(int type, int position);

        boolean isValueSelected(int type, int position);

        void selectValue(int type, int position);
    }

}
