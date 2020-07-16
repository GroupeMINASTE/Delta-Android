package fr.zabricraft.delta.sections;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;
import fr.zabricraft.delta.R;
import fr.zabricraft.delta.views.HeaderCell;
import fr.zabricraft.delta.views.SwitchCell;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class CloudSwitchSection extends Section {

    private final CloudSwitchContainer container;
    private int title;

    public CloudSwitchSection(int title, CloudSwitchContainer container) {
        super(SectionParameters.builder().itemViewWillBeProvided().headerViewWillBeProvided().build());

        this.container = container;
        this.title = title;
    }

    public int getContentItemsTotal() {
        return 1;
    }

    public View getItemView(ViewGroup parent) {
        return new SwitchCell(parent.getContext());
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
        if (itemHolder.itemView instanceof SwitchCell) {
            ((SwitchCell) itemHolder.itemView).with(
                    title == R.string.cloud_settings_sync_title ? R.string.cloud_settings_sync : R.string.cloud_settings_public,
                    title == R.string.cloud_settings_sync_title ? container.isSync() : container.isPublic(),
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (title == R.string.cloud_settings_sync_title) {
                                container.syncChanged(b);
                            } else {
                                container.publicChanged(b);
                            }
                        }
                    }
            );
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

    public interface CloudSwitchContainer {
        Boolean isSync();

        Boolean isPublic();

        void syncChanged(boolean enabled);

        void publicChanged(boolean enabled);
    }

}
