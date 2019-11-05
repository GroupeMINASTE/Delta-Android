package fr.zabricraft.delta.sections;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import fr.zabricraft.delta.actions.Action;
import fr.zabricraft.delta.utils.EditorLineCategory;
import fr.zabricraft.delta.views.ActionSelectorCell;
import fr.zabricraft.delta.views.HeaderCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ActionSelectorSection extends Section {

    private final ActionSelectionContainer container;
    private final EditorLineCategory category;

    public ActionSelectorSection(ActionSelectionContainer container, EditorLineCategory category) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
        this.category = category;
    }

    public int getContentItemsTotal() {
        return category.catalog().length;
    }

    public View getItemView(ViewGroup parent) {
        return new ActionSelectorCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext(), false);
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SectionedRecyclerViewAdapter.EmptyViewHolder itemHolder = (SectionedRecyclerViewAdapter.EmptyViewHolder) holder;
        final Action action = category.catalog()[position];

        // bind your view here
        if (itemHolder.itemView instanceof ActionSelectorCell) {
            ((ActionSelectorCell) itemHolder.itemView).with(action.toEditorLines().get(0));
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    container.selectAndClose(action);
                }
            });
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(category.title);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    // Container interface
    public interface ActionSelectionContainer {
        void selectAndClose(Action action);
    }

}
