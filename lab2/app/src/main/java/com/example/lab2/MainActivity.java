package com.example.lab2;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener, ExtraFragment.ExtraFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        primary = findViewById(R.id.textViewPrimary);
        primary.setText("0");
    }

    private boolean scientific = false;
    private boolean result = true;
    TextView primary;
    int leftBracket = 0;
    int rightBracket = 0;

    private boolean last(String str, String signs)
    {
        return signs.contains(str.charAt(str.length()-1)+"");
    }

    private boolean last(String str, char min, char max)
    {
        return str.charAt(str.length()-1) >= min && str.charAt(str.length()-1) <= max;
    }

    public static String calculate(String str)
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
                while (i < str.length() && (str.charAt(i) >= '0' && str.charAt(i) <= '9' || str.charAt(i) == '.')){
                    number += str.charAt(i);
                    i++;
                }
                output.add(new Pair<>(number, 'n'));
                i--;
            }
            else if (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
            {
                stack.push(new Pair<>(Character.toString(str.charAt(i)), 'u'));
            }
            else if (str.charAt(i) == '(')
            {
                stack.push(new Pair<>("(", '('));
            }
            else if (str.charAt(i) == ')')
            {
                while (stack.peek().second.toString().charAt(0) != '(')
                {
                    output.add(stack.pop());
                }
                stack.pop();
            }
            else if (str.charAt(i) == '-' && (i == 0 || str.charAt(i-1) == '('))
            {
                stack.push(new Pair<>("-", 'u'));
            }
            else {
                if (str.charAt(i) == '^') {
                    while (!stack.empty() && (stack.peek().second.toString().charAt(0) == 'h' ||
                            stack.peek().second.toString().charAt(0) == 'u'))
                    {
                        output.add(stack.pop());
                    }
                    stack.push(new Pair<>("^", 'h'));
                }
                else if (str.charAt(i) == '*' || str.charAt(i) == '/') {
                    while (!stack.empty() && (stack.peek().second.toString().charAt(0) == 'm' ||
                            stack.peek().second.toString().charAt(0) == 'h' ||
                            stack.peek().second.toString().charAt(0) == 'u'))
                    {
                        output.add(stack.pop());
                    }
                    stack.push(new Pair<>(Character.toString(str.charAt(i)), 'm'));
                }
                else if (str.charAt(i) == '+' || str.charAt(i) == '-') {
                    while (!stack.empty() && (stack.peek().second.toString().charAt(0) != '('))
                    {
                        output.add(stack.pop());
                    }
                    stack.push(new Pair<>(Character.toString(str.charAt(i)), 'l'));
                }
            }
        }
        while (!stack.empty()){
            output.add(stack.pop());
        }
        Stack<Double> result = new Stack<>();
        for (Pair lex: output) {
            if (lex.second.toString().charAt(0) == 'n')
            {
                result.push(Double.parseDouble(lex.first.toString()));
            }
            else try {
                if (lex.second.toString().charAt(0) == 'u') {
                    if (lex.first.toString().charAt(0) == 's') {
                        result.push(Math.sin(result.pop()));
                    }
                    else if (lex.first.toString().charAt(0) == 'c') {
                        result.push(Math.cos(result.pop()));
                    }
                    else if (lex.first.toString().charAt(0) == 't') {
                        result.push(Math.tan(result.pop()));
                    }
                    else if (lex.first.toString().charAt(0) == 'l') {
                        result.push(Math.log(result.pop()));
                    }
                    else if (lex.first.toString().charAt(0) == 'q') {
                        result.push(Math.sqrt(result.pop()));
                    }
                    else if (lex.first.toString().charAt(0) == '-') {
                        result.push(-(result.pop()));
                    }
                    else if (lex.first.toString().charAt(0) == '+') {
                        result.push(result.pop());
                    }
                }
                else {
                    if (lex.first.toString().charAt(0) == '+') {
                        result.push(result.pop() + result.pop());
                    }
                    else if (lex.first.toString().charAt(0) == '-') {
                        Double b = result.pop(), a = result.pop();
                        result.push(a - b);
                    }
                    else if (lex.first.toString().charAt(0) == '*') {
                        result.push(result.pop() * result.pop());
                    }
                    else if (lex.first.toString().charAt(0) == '/') {
                        Double b = result.pop(), a = result.pop();
                        result.push(a / b);
                    }
                    else if (lex.first.toString().charAt(0) == '^') {
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

    private void clear(){
        leftBracket=0;
        rightBracket=0;
        primary.setText("");
    }

    private boolean dotIsRelevant(String prim){
        for (int i = prim.length()-1; i >= 0; i--)
        {
            if (prim.charAt(i)=='.'){
                return false;
            }
            else if (prim.charAt(i)<'0'||prim.charAt(i)>'9'){
                return true;
            }
        }
        return true;
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

    @Override
    public void onNumberClick(Character n) {
        if (result) {
            primary.setText(n + "");
            result = false;
        }
        else {
            String prim = (String)primary.getText();
            if (last(prim, "0") && dotIsRelevant(prim))
            {
                return;
            }
            else if(last(prim, ")")){
                prim += '*';
            }
            prim += n;
            primary.setText(prim);
        }
    }

    @Override
    public void onDotClick(){
        String prim = (String) primary.getText();
        if (result){
            primary.setText("0.");
            result = false;
        }
        else if (last(prim, '0', '9')){
            if (dotIsRelevant(prim)) {
                prim += '.';
                primary.setText(prim);
            }
        }
        else {
            if (last(prim, ")")){
                prim+="*";
            }
            prim+="0.";
            primary.setText(prim);
        }
    }

    @Override
    public void onOperatorClick(Character o){
        String prim = (String)primary.getText();
        if (result){
            if (!last(prim, '0', '9')){
                return;
            }
            result = false;
        }
        if (last(prim, '0', '9') || last(prim, ")")){
            prim += o;
            primary.setText(prim);
        }
        else if(last(prim, "(") && o == '-'){
            prim += o;
            primary.setText(prim);
        }
        else if (last(prim, "+-*/^.")){
            prim = prim.substring(0, prim.length()-1) + o;
            primary.setText(prim);
        }
    }

    @Override
    public void onPrefixOperatorClick(String o){
        String prim = (String) primary.getText();
        if (result){
            prim = o + '(';
            result = false;
        }
        else if (last(prim, '0', '9') || last(prim, ")")){
            prim += '*' + o + '(';
        }
        else if (last(prim, ".")){
            prim = prim.substring(0, prim.length()-1) + '*' + o + '(';
        }
        else if (last(prim, "+-*/^(")){
            prim += o + '(';
        }
        primary.setText(prim);
        leftBracket++;
    }

    @Override
    public void onBracketClick(Character b)
    {
        if (result){
            if (b == '(') {
                primary.setText("(");
                leftBracket++;
                result = false;
            }
        }
        else{
            String prim = (String) primary.getText();
            switch (b){
                case '(':{
                    if (last(prim, '0', '9') || last(prim, ")")){
                        prim += "*(";
                        primary.setText(prim);
                        leftBracket++;
                    }
                    else if (last(prim, ".")){
                        prim = prim.substring(0, prim.length()-1) + "*(";
                        primary.setText(prim);
                        leftBracket++;
                    }
                    else if (last(prim, "(")){
                        prim += '(';
                        primary.setText(prim);
                        leftBracket++;
                    }
                    break;
                }
                case ')':{
                    if (leftBracket <= rightBracket) {
                        return;
                    }
                    else if (last(prim, "+-*/^(")){
                        return;
                    }
                    else if (last(prim, ".")){
                        prim = prim.substring(0, prim.length()-1) + ')';
                        primary.setText(prim);
                        rightBracket++;
                    }
                    else if (last(prim, '0', '9') || last(prim, ")")){
                        prim += ')';
                        primary.setText(prim);
                        rightBracket++;
                    }
                }
            }
        }
    }

    @Override
    public void onSignClick(){
        String prim = (String)primary.getText();
        if (result){
            if (!last(prim, '0', '9')){
                return;
            }
            result = false;
        }
        prim = "-(" + prim + ')';
        primary.setText(prim);
        leftBracket++;
        rightBracket++;
    }

    @Override
    public void onEqualClick(){
        if (result){
            return;
        }
        else if (leftBracket != rightBracket){
            primary.setText("Ошибка");
        }
        else if (last((String) primary.getText(), "+-*/^(")){
            primary.setText("Ошибка");
        }
        else {
            primary.setText(calculate((String) primary.getText()));
        }
        leftBracket = 0;
        rightBracket = 0;
        result = true;
    }

    @Override
    public void onCClick(){
        primary.setText("0");
        leftBracket = 0;
        rightBracket = 0;
        result = true;
    }

    @Override
    public void onCeClick(){
        String prim = (String) primary.getText();
        if (result){
            primary.setText("0");
        }
        else if (prim.length() == 1){
            primary.setText("0");
            result = true;
            leftBracket = 0;
            rightBracket = 0;
        }
        else {
            if (last(prim, "("))
            {
                do{
                    prim = prim.substring(0, prim.length()-1);
                }
                while (prim.length()>0 && last(prim, 'a', 'z'));
                leftBracket--;
                if (prim.length() > 0)
                    primary.setText(prim);
                else{
                    primary.setText("0");
                    result = true;
                }
            }
            else if (last(prim, ")")){
                prim = prim.substring(0, prim.length()-1);
                primary.setText(prim);
                rightBracket--;
            }
            else{
                prim = prim.substring(0, prim.length()-1);
                primary.setText(prim);
            }
        }
    }
}
