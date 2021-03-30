package fr.zabricraft.delta.sections;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.LabelCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class AboutSection extends Section {

    public AboutSection() {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());
    }

    public int getContentItemsTotal() {
        return 3;
    }

    public View getItemView(ViewGroup parent) {
        return new LabelCell(parent.getContext());
    }

    public View getHeaderView(ViewGroup parent) {
        return new HeaderCell(parent.getContext());
    }

    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new EmptyViewHolder(view);
    }

    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EmptyViewHolder itemHolder = (EmptyViewHolder) holder;

        // bind your view here
        if (itemHolder.itemView instanceof LabelCell) {
            ((LabelCell) itemHolder.itemView).with(position == 0 ? R.string.about : position == 1 ? R.string.help : R.string.follow_twitter);
            itemHolder.itemView.setOnClickListener(view -> {
                if (position == 0) {
                    // About
                    new AlertDialog.Builder(itemHolder.itemView.getContext()).setTitle(R.string.about).setMessage(R.string.about_text).show();
                } else if (position == 1) {
                    // Help and documentation
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.delta-algorithms.com/documentation"));
                    itemHolder.itemView.getContext().startActivity(browserIntent);
                } else {
                    // Follow us on Twitter
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/DeltaAlgorithms"));
                    itemHolder.itemView.getContext().startActivity(browserIntent);
                }
            });
        }
    }

    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // Check if it's a headerCell
        if (view instanceof HeaderCell) {
            ((HeaderCell) view).with(R.string.about);
        }

        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

}
