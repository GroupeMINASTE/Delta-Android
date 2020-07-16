package fr.zabricraft.delta.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.quiz.Quiz;
import fr.zabricraft.delta.quiz.QuizElement;
import fr.zabricraft.delta.quiz.QuizParagraph;
import fr.zabricraft.delta.quiz.QuizQuestion;
import fr.zabricraft.delta.tokens.Equation;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.utils.Operation;
import fr.zabricraft.delta.utils.TokenParser;

public class QuizFragment extends Fragment {

    private Quiz quiz;
    private boolean checked = false;

    private TextView header;
    private LinearLayout stack;
    private Button button;

    public static QuizFragment create(Quiz quiz) {
        Bundle args = new Bundle();
        args.putSerializable("quiz", quiz);

        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view
        ScrollView scrollView = new ScrollView(getActivity());

        // Size of dp
        int dp4 = IntExtension.dpToPixel(4, getResources());
        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Create the main layout
        LinearLayout main = new LinearLayout(getActivity());
        main.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(main);

        // Create the header
        header = new TextView(getActivity());
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerParams.setMargins(dp16, dp16, dp16, dp16);
        header.setLayoutParams(headerParams);
        header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        header.setTextColor(getResources().getColor(android.R.color.black));
        header.setTypeface(header.getTypeface(), Typeface.BOLD);
        header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        main.addView(header);

        // Create the stack
        stack = new LinearLayout(getActivity());
        stack.setOrientation(LinearLayout.VERTICAL);
        main.addView(stack);

        // Create the button
        button = new Button(getActivity());
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(dp16, dp16, dp16, dp16);
        button.setLayoutParams(buttonParams);
        button.setText(R.string.check);
        button.setTextColor(getResources().getColor(android.R.color.white));
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(getResources().getColor(R.color.colorPrimary));
        buttonBackground.setCornerRadius(dp8);
        button.setBackground(buttonBackground);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (checked) {
                    // Dismiss view
                    Intent result = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, result);
                    getActivity().finish();
                } else {
                    // Get fields
                    for (int i = 0; i < stack.getChildCount(); i++) {
                        // Extract field if exists
                        if (stack.getChildAt(i) instanceof LinearLayout && quiz.elements.get(i) instanceof QuizQuestion) {
                            // Get views
                            LinearLayout subview = (LinearLayout) stack.getChildAt(i);
                            TextView label = (TextView) subview.getChildAt(0);
                            EditText field = (EditText) subview.getChildAt(1);
                            QuizQuestion question = (QuizQuestion) quiz.elements.get(i);

                            // Check the question
                            Token answer = new TokenParser(field.getText().toString()).execute();
                            Equation condition = new Equation(answer, question.correct, Operation.equals);

                            if (condition.isTrue(new HashMap<>())) {
                                // Answer is correct
                                label.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                field.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                            } else {
                                // Answer is not correct
                                label.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                                field.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            }

                            // Disable the field
                            field.setEnabled(false);
                        }
                    }

                    // Set as checked
                    checked = true;
                    button.setText(R.string.close);
                }
            }
        });
        main.addView(button);

        // Load quiz
        Object object = getArguments().getSerializable("quiz");
        if (object instanceof Quiz) {
            // Set header
            quiz = (Quiz) object;
            header.setText(quiz.text);

            // Iterate elements
            for (int i = 0; i < quiz.elements.size(); i++) {
                QuizElement element = quiz.elements.get(i);

                if (element instanceof QuizQuestion) {
                    QuizQuestion question = (QuizQuestion) element;

                    // Create an horizontal LinearLayout
                    LinearLayout horizontal = new LinearLayout(getActivity());
                    horizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    horizontal.setOrientation(LinearLayout.HORIZONTAL);

                    TextView label = new TextView(getActivity());
                    LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    labelParams.setMargins(dp16, dp8, dp4, dp8);
                    labelParams.gravity = Gravity.CENTER;
                    label.setLayoutParams(labelParams);
                    label.setText(StringExtension.attributedMath(question.text + " ="));
                    label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    label.setTextColor(getResources().getColor(android.R.color.black));

                    EditText field = new EditText(getActivity());
                    LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    fieldParams.setMargins(0, 0, dp16, 0);
                    fieldParams.gravity = Gravity.CENTER;
                    field.setLayoutParams(fieldParams);
                    field.setSingleLine();

                    horizontal.addView(label);
                    horizontal.addView(field);
                    stack.addView(horizontal);
                } else if (element instanceof QuizParagraph) {
                    QuizParagraph paragraph = (QuizParagraph) element;

                    TextView label = new TextView(getActivity());
                    LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    labelParams.setMargins(dp16, dp8, dp16, dp8);
                    labelParams.gravity = Gravity.CENTER;
                    label.setLayoutParams(labelParams);
                    label.setText(StringExtension.attributedMath(paragraph.text));
                    label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    label.setTextColor(getResources().getColor(android.R.color.black));

                    stack.addView(label);
                }
            }
        }

        return scrollView;
    }

}
