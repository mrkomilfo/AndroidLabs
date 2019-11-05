package com.example.lab2;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

    public interface MainFragmentListener {
        void onNumberClick(Character n);
        void onDotClick();
        void onCeClick();
        void onCClick();
        void onOperatorClick(Character o);
        void onSignClick();
        void onEqualClick();
    }

    MainFragmentListener mainFragmentListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mainFragmentListener = (MainFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, null);

         View.OnClickListener numberListener = new View.OnClickListener() {
             public void onClick(View v) {
                 mainFragmentListener.onNumberClick(((Button)v).getText().charAt(0));
             }
         };

         v.findViewById(R.id.button0).setOnClickListener(numberListener);
         v.findViewById(R.id.button1).setOnClickListener(numberListener);
         v.findViewById(R.id.button2).setOnClickListener(numberListener);
         v.findViewById(R.id.button3).setOnClickListener(numberListener);
         v.findViewById(R.id.button4).setOnClickListener(numberListener);
         v.findViewById(R.id.button5).setOnClickListener(numberListener);
         v.findViewById(R.id.button6).setOnClickListener(numberListener);
         v.findViewById(R.id.button7).setOnClickListener(numberListener);
         v.findViewById(R.id.button8).setOnClickListener(numberListener);
         v.findViewById(R.id.button9).setOnClickListener(numberListener);

         View.OnClickListener operatorListener = new View.OnClickListener() {
             public void onClick(View v) {
                 mainFragmentListener.onOperatorClick(((String)((Button)v).getText()).charAt(0));
             }
         };

         v.findViewById(R.id.buttonPlus).setOnClickListener(operatorListener);
         v.findViewById(R.id.buttonMinus).setOnClickListener(operatorListener);
         v.findViewById(R.id.buttonMultiply).setOnClickListener(operatorListener);
         v.findViewById(R.id.buttonDiv).setOnClickListener(operatorListener);

         v.findViewById(R.id.buttonDot).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 mainFragmentListener.onDotClick();
             }
         });

         v.findViewById(R.id.buttonC).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 mainFragmentListener.onCClick();
             }
         });

         v.findViewById(R.id.buttonCe).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 mainFragmentListener.onCeClick();
             }
         });

         v.findViewById(R.id.buttonSign).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 mainFragmentListener.onSignClick();
             }
         });

         v.findViewById(R.id.buttonEqual).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 mainFragmentListener.onEqualClick();
             }
         });

         return v;
    }
}
