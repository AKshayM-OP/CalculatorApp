package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView resultText;
    private TextView inputExpression;
    private GridLayout grid;

    private String currentInput = "";
    private String operator = "";
    private double firstOperand = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = findViewById(R.id.result);
        inputExpression = findViewById(R.id.inputExpression);
        grid = findViewById(R.id.gridLayout);

        if (grid != null) {
            for (int i = 0; i < grid.getChildCount(); i++) {
                View child = grid.getChildAt(i);
                if (child instanceof Button) {
                    child.setOnClickListener(v -> {
                        Button b = (Button) v;
                        onButtonClick(b.getText().toString());
                    });
                }
            }
        }
        
        resetCalculator();
    }

    private void onButtonClick(String text) {
        if (text.matches("[0-9]")) {
            currentInput += text;
            updateDisplay();
        } else if (text.matches("[+\\-*/]")) {
            if (!currentInput.isEmpty()) {
                if (!Double.isNaN(firstOperand)) {
                    firstOperand = calculate(firstOperand, Double.parseDouble(currentInput), operator);
                } else {
                    firstOperand = Double.parseDouble(currentInput);
                }
                operator = text;
                currentInput = "";
                updateDisplay();
            } else if (!Double.isNaN(firstOperand)) {
                operator = text;
                updateDisplay();
            }
        } else if ("=".equals(text)) {
            if (!Double.isNaN(firstOperand) && !currentInput.isEmpty()) {
                double result = calculate(firstOperand, Double.parseDouble(currentInput), operator);
                String resultStr = formatResult(result);
                

                inputExpression.setText(resultStr);
                resultText.setText("");
                

                currentInput = resultStr;
                firstOperand = Double.NaN;
                operator = "";
            }
        } else if ("C".equals(text)) {
            resetCalculator();
        }
    }

    private void updateDisplay() {
        if (Double.isNaN(firstOperand)) {
            inputExpression.setText(currentInput.isEmpty() ? "0" : currentInput);
            resultText.setText("");
        } else {
            String expression = formatResult(firstOperand) + " " + operator + " " + currentInput;
            inputExpression.setText(expression);
            
            if (!currentInput.isEmpty()) {
                double interResult = calculate(firstOperand, Double.parseDouble(currentInput), operator);
                resultText.setText(formatResult(interResult));
            } else {
                resultText.setText("");
            }
        }
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return b != 0 ? a / b : 0;
            default: return b;
        }
    }

    private String formatResult(double d) {
        if (d == (long) d)
            return String.format(Locale.getDefault(), "%d", (long) d);
        else
            return String.format(Locale.getDefault(), "%s", d);
    }

    private void resetCalculator() {
        currentInput = "";
        firstOperand = Double.NaN;
        operator = "";
        resultText.setText("");
        inputExpression.setText("0");
    }
}
