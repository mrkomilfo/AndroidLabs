package com.example.lab2;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ExtraFragment extends Fragment {

    public interface ExtraFragmentListener {
        void onOperatorClick(Character o);
        void onPrefixOperatorClick(String o);
        void onBracketClick(Character b);
    }

    ExtraFragmentListener extraFragmentListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            extraFragmentListener = (ExtraFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_extra, null);

        View.OnClickListener prefixOperatorsListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (((Button)v).getText().length()==1) {
                    extraFragmentListener.onPrefixOperatorClick("sqrt");
                }
                else {
                    extraFragmentListener.onPrefixOperatorClick((String) ((Button) v).getText());
                }
            }
        };

        v.findViewById(R.id.buttonSin).setOnClickListener(prefixOperatorsListener);
        v.findViewById(R.id.buttonCos).setOnClickListener(prefixOperatorsListener);
        v.findViewById(R.id.buttonTan).setOnClickListener(prefixOperatorsListener);
        v.findViewById(R.id.buttonSqrt).setOnClickListener(prefixOperatorsListener);
        v.findViewById(R.id.buttonLn).setOnClickListener(prefixOperatorsListener);

        View.OnClickListener bracketsListener = new View.OnClickListener() {
            public void onClick(View v) {
                extraFragmentListener.onBracketClick(((Button)v).getText().charAt(0));
            }
        };

        v.findViewById(R.id.buttonLeftBracket).setOnClickListener(bracketsListener);
        v.findViewById(R.id.buttonRightBracket).setOnClickListener(bracketsListener);

        v.findViewById(R.id.buttonPow).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                extraFragmentListener.onOperatorClick('^');
            }
        });

        return v;
    }
}
