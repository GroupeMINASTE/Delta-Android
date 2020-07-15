package fr.zabricraft.delta.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.sections.LoadingSection;

public class LoadingCell extends LinearLayout {

    private ProgressBar loading;
    private LoadingSection.LoadingContainer container;

    public LoadingCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp44 = IntExtension.dpToPixel(44, getResources());

        // Configure LinearLayout
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(android.R.color.white);

        // Init loading
        loading = new ProgressBar(context);
        LinearLayout.LayoutParams loadingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp44);
        loadingParams.setMargins(dp8, dp8, dp8, dp8);
        loading.setLayoutParams(loadingParams);

        // Add them to layout
        addView(loading);
        addView(new Separator(context));
    }

    public void with(LoadingSection.LoadingContainer container) {
        // Set container
        this.container = container;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        container.loadMore();
    }
}
