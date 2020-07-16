package fr.zabricraft.delta.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.sections.CloudDetailsSection;
import fr.zabricraft.delta.utils.Algorithm;

public class CloudDetailsCell extends LinearLayout {

    private ImageView icon;
    private TextView name;
    private TextView desc;
    private TextView notes;
    private Button button;
    private APIAlgorithm algorithm;
    private Algorithm onDevice;
    private CloudDetailsSection.CloudDetailsContainer delegate;

    public CloudDetailsCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp4 = IntExtension.dpToPixel(4, getResources());
        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp12 = IntExtension.dpToPixel(12, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());
        int dp44 = IntExtension.dpToPixel(44, getResources());

        // Configure LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp16, 0, dp16, dp16);
        setLayoutParams(params);
        setOrientation(LinearLayout.VERTICAL);

        // Background
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(dp8);
        setBackground(background);

        // Init item layout
        LinearLayout item = new LinearLayout(context);
        item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        item.setOrientation(LinearLayout.HORIZONTAL);

        // Init icon
        icon = new ImageView(context);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iconParams.setMargins(dp8, dp8, 0, dp8);
        iconParams.width = dp44;
        iconParams.height = dp44;
        icon.setLayoutParams(iconParams);
        icon.setClipToOutline(true);
        item.addView(icon);

        // Init names layout
        LinearLayout names = new LinearLayout(context);
        names.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        names.setOrientation(LinearLayout.VERTICAL);
        item.addView(names);

        // Init name
        name = new TextView(context);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(dp12, dp8, dp8, 0);
        name.setLayoutParams(nameParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        name.setTextColor(getResources().getColor(android.R.color.black));
        names.addView(name);

        // Init desc
        desc = new TextView(context);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(dp12, dp4, dp8, dp8);
        desc.setLayoutParams(descParams);
        desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        desc.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        names.addView(desc);

        // Init notes
        notes = new TextView(context);
        LinearLayout.LayoutParams notesParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        notesParams.setMargins(dp8, 0, dp8, dp8);
        notes.setLayoutParams(notesParams);
        notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        notes.setTextColor(getResources().getColor(android.R.color.black));

        // Create an horizontal LinearLayout
        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        // Button background
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(getResources().getColor(R.color.colorPrimary));
        buttonBackground.setCornerRadius(dp8);

        // Init button
        button = new Button(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(dp8, 0, dp8, dp8);
        buttonParams.weight = 1;
        button.setLayoutParams(buttonParams);
        button.setTextColor(getResources().getColor(android.R.color.white));
        button.setBackground(buttonBackground);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                buttonClicked();
            }
        });
        horizontal.addView(button);

        // Init share
        Button share = new Button(context);
        LinearLayout.LayoutParams shareParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        shareParams.setMargins(0, 0, dp8, dp8);
        shareParams.weight = 1;
        share.setLayoutParams(shareParams);
        share.setText(R.string.share);
        share.setTextColor(getResources().getColor(android.R.color.white));
        share.setBackground(buttonBackground);
        share.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                shareClicked();
            }
        });
        horizontal.addView(share);

        // Add them to layout
        addView(item);
        addView(notes);
        addView(horizontal);
    }

    public void with(APIAlgorithm algorithm, Algorithm onDevice, CloudDetailsSection.CloudDetailsContainer delegate) {
        // Set algorithms
        this.algorithm = algorithm;
        this.onDevice = onDevice;
        this.delegate = delegate;

        // Set views
        name.setText(algorithm.name);
        desc.setText(algorithm.owner != null ? algorithm.owner.name : "");
        notes.setText(algorithm.notes != null ? algorithm.notes : getResources().getString(R.string.cloud_no_notes));
        icon.setImageResource(StringExtension.toIcon(algorithm.icon.getIcon()));

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(IntExtension.dpToPixel(8, getResources()));
        background.setColor(getResources().getColor(StringExtension.toColor(algorithm.icon.getColor())));
        icon.setBackground(background);

        // Calculate button
        button.setText(getButton());
    }

    public int getButton() {
        if (onDevice != null && algorithm != null && algorithm.last_update != null) {
            Date last_update = StringExtension.toDate(algorithm.last_update);
            if (last_update != null) {
                // Compare last update date
                int compare = onDevice.getLastUpdate().compareTo(last_update);
                if (compare == 0) {
                    // Open
                    return R.string.open;
                }

                // Update
                return R.string.update;
            }
        }

        // Download
        return R.string.download;
    }

    public void buttonClicked() {
        // Get button and check what to do
        int button = getButton();
        if (button == R.string.download || button == R.string.update) {
            open(download());
        } else {
            open(onDevice);
        }
    }

    public void shareClicked() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "https://www.delta-math-helper.com/algorithm/" + algorithm.id);
        delegate.getActivity().startActivity(Intent.createChooser(share, delegate.getActivity().getString(R.string.share)));
    }

    public Algorithm download() {
        // Save the algorithm on the device
        return algorithm != null ? algorithm.saveToDatabase(getContext()) : null;
    }

    public void open(Algorithm algorithm) {
        // Open it
        if (algorithm != null && delegate != null) {
            delegate.open(algorithm);
        }
    }

}
