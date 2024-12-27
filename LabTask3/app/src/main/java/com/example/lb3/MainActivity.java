package com.example.lb3;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.lb3.R;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Boolean isNew = true;
    String operator = null;
    String prevNum;
    String previousOperator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
    }
    public void numberClick(View view) {
        String number = editText.getText().toString();
        if (isNew) {
            number = "";
            isNew = false;
        }
        if (number.length() >= 12) {
            return;
        }
        if (view.getId() == R.id.button1) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "1";
        } else if (view.getId() == R.id.button2) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "2";
        } else if (view.getId() == R.id.button3) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "3";
        } else if (view.getId() == R.id.button4) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "4";
        } else if (view.getId() == R.id.button5) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "5";
        } else if (view.getId() == R.id.button6) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "6";
        } else if (view.getId() == R.id.button7) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "7";
        } else if (view.getId() == R.id.button8) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "8";
        } else if (view.getId() == R.id.button9) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number += "9";
        } else if (view.getId() == R.id.buttonNull) {
            if (NullIsFirst(number) && number.length() == 1) {
                number = "0";
            } else {
                number += "0";
            }
        } else if (view.getId() == R.id.buttonPoint) {
            if (PointExist(number)) {
                return;
            }
            if (NullIsFirst(number)) {
                number = "0.";
            } else {
                number += ".";
            }
        }

        editText.setText(number);
    }
    public void operationClick(View view) {
        isNew = true;
        prevNum = editText.getText().toString();

        if (view.getId() == R.id.buttonMinus) {
            operator = "-";
        } else if (view.getId() == R.id.buttonPlus) {
            operator = "+";
        } else if (view.getId() == R.id.buttonMultiply) {
            operator = "*";
        } else if (view.getId() == R.id.buttonDivide) {
            operator = "/";
        }
        previousOperator = operator;
    }
    public void clickResult(View view) {
        String newNumber = editText.getText().toString();
        Double result = 0.0;
        try {
            Double firstNumber = Double.parseDouble(prevNum);
            Double secondNumber = Double.parseDouble(newNumber);
            if(previousOperator != null) {
                switch (previousOperator) {
                    case "-":
                        result = firstNumber - secondNumber;
                        break;
                    case "+":
                        result = firstNumber + secondNumber;
                        break;
                    case "*":
                        result = firstNumber * secondNumber;
                        break;
                    case "/":
                        if (secondNumber == 0) {
                            editText.setText("Error");
                            return;
                        }
                        result = firstNumber / secondNumber;
                        break;
                }
            }
            prevNum = Double.toString(result);
            operator = "";
            isNew = true;
            DecimalFormat decimalFormat = new DecimalFormat("#.#######");
            String formattedResult = decimalFormat.format(result);
            if (formattedResult.length() <= 17) {
                editText.setText(formattedResult);
                prevNum = formattedResult;
                operator = "";
                isNew = true;
            }
            else{
                editText.setText("Превелике число!");
            }
        } catch (NumberFormatException e) {
            editText.setText("Error");
        }
    }
    public void clickAC(View view){
        editText.setText("0");
        isNew = true;
    }
    private boolean PointExist(String number){
        return number.indexOf(".") != -1;
    }
    private boolean NullIsFirst(String number){
        if (number.equals("") || number.charAt(0) == '0') { return  true; }
        return false;
    }
}