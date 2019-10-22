package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private boolean scientific = false;

    public static String Calculate(String str)
    {
        str = str.replace("sin", "s").
                replace("cos", "c").
                replace("tan", "t").
                replace("ln", "l").
                replace("sqrt", "q");

        ArrayList<Pair> output = new ArrayList<>();
        Stack<Pair> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9')
            {
                String number = "";
                while (i < str.length() && (str.charAt(i) >= '0' && str.charAt(i) <= '9' || str.charAt(i) == ',')){
                    number += str.charAt(i);
                    i++;
                }
                output.add(new Pair(number, 'n'));
                i--;
            }
            else if (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
            {
                stack.push(new Pair(Character.toString(str.charAt(i)), 'u'));
            }
            else if (str.charAt(i) == '(')
            {
                stack.push(new Pair("(", '('));
            }
            else if (str.charAt(i) == ')')
            {
                while (stack.peek().getSpecifier() != '(')
                {
                    output.add(stack.pop());
                }
                stack.pop();
            }
            else if (str.charAt(i) == '-' && (i == 0 || str.charAt(i-1) == '('))
            {
                stack.push(new Pair("-", 'u'));
            }
            else {
                if (str.charAt(i) == '^') {
                    while (!stack.empty() && (stack.peek().getSpecifier() == 'h' || stack.peek().getSpecifier() == 'u'))
                    {
                        output.add(stack.pop());
                    }
                    stack.push(new Pair("^", 'h'));
                }
                else if (str.charAt(i) == '*' || str.charAt(i) == '/') {
                    while (!stack.empty() && (stack.peek().getSpecifier() == 'm' ||stack.peek().getSpecifier() == 'h' || stack.peek().getSpecifier() == 'u'))
                    {
                        output.add(stack.pop());
                    }
                    stack.push(new Pair(Character.toString(str.charAt(i)), 'm'));
                }
                else if (str.charAt(i) == '+' || str.charAt(i) == '-') {
                    while (!stack.empty() && (stack.peek().getSpecifier() != '('))
                    {
                        output.add(stack.pop());
                    }
                    stack.push(new Pair(Character.toString(str.charAt(i)), 'l'));
                }
            }
        }
        while (!stack.empty()){
            output.add(stack.pop());
        }
        Stack<Double> result = new Stack<>();
        for (Pair lex: output) {
            if (lex.getSpecifier() == 'n')
            {
                result.push(Double.parseDouble(lex.getValue()));
            }
            else try {
                if (lex.getSpecifier() == 'u') {
                    if (lex.getValue().charAt(0) == 's') {
                        result.push(Math.sin(result.pop()));
                    }
                    else if (lex.getValue().charAt(0) == 'c') {
                        result.push(Math.cos(result.pop()));
                    }
                    else if (lex.getValue().charAt(0) == 't') {
                        result.push(Math.tan(result.pop()));
                    }
                    else if (lex.getValue().charAt(0) == 'l') {
                        result.push(Math.log(result.pop()));
                    }
                    else if (lex.getValue().charAt(0) == 'q') {
                        result.push(Math.sqrt(result.pop()));
                    }
                    else if (lex.getValue().charAt(0) == '-') {
                        result.push(-(result.pop()));
                    }
                    else if (lex.getValue().charAt(0) == '+') {
                        result.push(result.pop());
                    }
                }
                else {
                    if (lex.getValue().charAt(0) == '+') {
                        result.push(result.pop() + result.pop());
                    }
                    else if (lex.getValue().charAt(0) == '-') {
                        Double b = result.pop(), a = result.pop();
                        result.push(a - b);
                    }
                    else if (lex.getValue().charAt(0) == '*') {
                        result.push(result.pop() * result.pop());
                    }
                    else if (lex.getValue().charAt(0) == '/') {
                        Double b = result.pop(), a = result.pop();
                        result.push(a / b);
                    }
                    else if (lex.getValue().charAt(0) == '^') {
                        Double b = result.pop(), a = result.pop();
                        result.push(Math.pow(a, b));
                    }
                }
            } catch (Throwable ex){
                return "Ошибка";
            }
        }
        return result.pop().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onChmodClick(View view){
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        LinearLayout extraLayout = (LinearLayout) findViewById(R.id.extra_layout);

        if (!scientific){
            mainLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            5)
            );
            extraLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2)
            );
        }
        else {
            mainLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            7)
            );
            extraLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0)
            );
        }
        scientific = !scientific;
    }

    public boolean isScientific() {
        return scientific;
    }

    public void setScientific(boolean mode) {
        this.scientific = mode;
    }
}
