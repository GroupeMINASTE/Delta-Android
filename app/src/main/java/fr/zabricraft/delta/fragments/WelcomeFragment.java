package fr.zabricraft.delta.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.IntExtension;

public class WelcomeFragment extends Fragment {

    ScrollView scrollView;
    LinearLayout contentView;
    TextView header1;
    TextView paragraph1;
    ImageView image1;
    TextView header2;
    TextView paragraph2;
    ImageView image2;
    TextView header3;
    TextView paragraph3;
    ImageView image3;
    TextView header4;
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = new ScrollView(getActivity());

        contentView = new LinearLayout(getActivity());
        contentView.setOrientation(LinearLayout.VERTICAL);
        contentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        contentView.setBackgroundColor(getResources().getColor(R.color.background));
        scrollView.addView(contentView);

        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());

        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerParams.setMargins(dp16, dp16, dp16, 0);

        LinearLayout.LayoutParams paragraphParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paragraphParams.setMargins(dp16, dp8, dp16, 0);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageParams.setMargins(0, dp8, 0, 0);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(dp16, dp8, dp16, dp16);

        header1 = new TextView(getActivity());
        header1.setLayoutParams(headerParams);
        header1.setText(R.string.welcome_header1);
        header1.setTextColor(getResources().getColor(android.R.color.black));
        header1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        contentView.addView(header1);

        paragraph1 = new TextView(getActivity());
        paragraph1.setLayoutParams(paragraphParams);
        paragraph1.setText(R.string.welcome_paragraph1);
        paragraph1.setTextColor(getResources().getColor(android.R.color.black));
        paragraph1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        contentView.addView(paragraph1);

        image1 = new ImageView(getActivity());
        image1.setLayoutParams(imageParams);
        image1.setImageResource(R.drawable.welcome1);
        image1.setAdjustViewBounds(true);
        image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        contentView.addView(image1);

        header2 = new TextView(getActivity());
        header2.setLayoutParams(headerParams);
        header2.setText(R.string.welcome_header2);
        header2.setTextColor(getResources().getColor(android.R.color.black));
        header2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        contentView.addView(header2);

        paragraph2 = new TextView(getActivity());
        paragraph2.setLayoutParams(paragraphParams);
        paragraph2.setText(R.string.welcome_paragraph2);
        paragraph2.setTextColor(getResources().getColor(android.R.color.black));
        paragraph2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        contentView.addView(paragraph2);

        image2 = new ImageView(getActivity());
        image2.setLayoutParams(imageParams);
        image2.setImageResource(R.drawable.welcome2);
        image2.setAdjustViewBounds(true);
        image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        contentView.addView(image2);

        header3 = new TextView(getActivity());
        header3.setLayoutParams(headerParams);
        header3.setText(R.string.welcome_header3);
        header3.setTextColor(getResources().getColor(android.R.color.black));
        header3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        contentView.addView(header3);

        paragraph3 = new TextView(getActivity());
        paragraph3.setLayoutParams(paragraphParams);
        paragraph3.setText(R.string.welcome_paragraph3);
        paragraph3.setTextColor(getResources().getColor(android.R.color.black));
        paragraph3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        contentView.addView(paragraph3);

        image3 = new ImageView(getActivity());
        image3.setLayoutParams(imageParams);
        image3.setImageResource(R.drawable.welcome3);
        image3.setAdjustViewBounds(true);
        image3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        contentView.addView(image3);

        header4 = new TextView(getActivity());
        header4.setLayoutParams(headerParams);
        header4.setText(R.string.welcome_header4);
        header4.setTextColor(getResources().getColor(android.R.color.black));
        header4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        contentView.addView(header4);

        button = new Button(getActivity());
        button.setLayoutParams(buttonParams);
        button.setText(R.string.welcome_button);
        button.setTextColor(getResources().getColor(android.R.color.white));
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(getResources().getColor(R.color.colorPrimary));
        buttonBackground.setCornerRadius(dp8);
        button.setBackground(buttonBackground);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Save welcome shown
                SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(getActivity());
                data.edit().putBoolean("welcomeShown", true).apply();

                // Close activity
                getActivity().finish();
            }
        });
        contentView.addView(button);

        return scrollView;
    }

}
