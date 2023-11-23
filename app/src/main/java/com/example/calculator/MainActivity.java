 package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;



 public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     TextView result, calculation;
     MaterialButton btnAc, btnOpBrack, btnClBrack;
     MaterialButton btnDiv, btnMul, btnPlus, btnMinus, btnEquals;
     MaterialButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
     MaterialButton btnDel, btnDot;
     MaterialButton btnCur;



     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         result = findViewById(R.id.result);
         calculation = findViewById(R.id.calculation);


         assignId(btnAc, R.id.button_Ac);
         assignId(btnOpBrack, R.id.button_left_bracket);
         assignId(btnClBrack, R.id.button_right_bracket);
         assignId(btn1, R.id.button_1);
         assignId(btn2, R.id.button_2);
         assignId(btn3, R.id.button_3);
         assignId(btn4, R.id.button_4);
         assignId(btn5, R.id.button_5);
         assignId(btn6, R.id.button_6);
         assignId(btn7, R.id.button_7);
         assignId(btn8, R.id.button_8);
         assignId(btn9, R.id.button_9);
         assignId(btn0, R.id.button_0);
         assignId(btnDot, R.id.button_dot);
         assignId(btnPlus, R.id.button_plus);
         assignId(btnMinus, R.id.button_minus);
         assignId(btnDiv, R.id.button_div);
         assignId(btnEquals, R.id.button_equals);
         assignId(btnMul, R.id.button_mult);
         assignId(btnCur, R.id.button_cur);
         assignId(btnDel, R.id.button_del);

     }

     void assignId(MaterialButton btn, int id) {
         btn = findViewById(id);
         btn.setOnClickListener(this);
     }

     @Override
     public void onClick(View view) {

         MaterialButton btn = (MaterialButton) view;
         String btnText = btn.getText().toString();
         String dataToCalculate = calculation.getText().toString();


         if (btnText.equals("0")){
             if (dataToCalculate.length() == 0){
                 return;
             }
         }
         if (btnText.equals("Currency")) {
             launchExchange();
             return;


         }
         if (btnText.equals("AC")) {
             calculation.setText("");
             result.setText("0");
             return;

         }
         if (btnText.equals("=")) {
             calculation.setText(result.getText());
             return;

         }
         if (btnText.equals("del")) {
             if(dataToCalculate.length() > 1) {

                 dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
             }else{
                 dataToCalculate = "0";
             }

         } else {
             dataToCalculate += btnText;

         }

         calculation.setText(dataToCalculate);

         String finalResult = getResult(dataToCalculate);

         if (!finalResult.equals("Err")) {
             result.setText(finalResult);
         }
     }

     String getResult(String data) {

         try {
             Context context = Context.enter();
             context.setOptimizationLevel(-1);
             Scriptable scriptable = context.initStandardObjects();
             String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
             if (finalResult.endsWith(".0")) {
                 finalResult = finalResult.replace(".0", "");
             }
             return finalResult;
         } catch (Exception e) {
             return "Err";
         }
     }

     public void launchExchange(){
         //launching a new activity for the exchange window

         Intent i = new Intent(this, ExchangeActivity.class);
         startActivity(i);

     }

}