package com.clockwork53.taxcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private NumberFormat Currency_Format = NumberFormat.getCurrencyInstance(Locale.getDefault());
    private EditText Current_Cost_Input;
    private EditText Current_Tax_Input;
    private TextView Total_Cost_View;
    private TextView Total_Cost_wTax_View;
    private BigDecimal Total_Cost,Total_Cost_wTax;
    private String Symbols_to_Remove_From_Input;

    private class Cost_Input_Watcher implements TextWatcher {
        private EditText editTextInstance;
        private String Current_Input_Value = "";
        public Cost_Input_Watcher(EditText e) {
            editTextInstance = e;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().equals(Current_Input_Value)){
                editTextInstance.removeTextChangedListener(this);

                String cleanString = s.toString().replaceAll(Symbols_to_Remove_From_Input, "");

                BigDecimal parsed = new BigDecimal(cleanString);
                String formatted = NumberFormat.getCurrencyInstance().format((parsed.divide(new BigDecimal("100.0"))));

                Current_Input_Value = formatted;
                editTextInstance.setText(formatted);
                editTextInstance.setSelection(formatted.length());
                editTextInstance.addTextChangedListener(this);
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        public void afterTextChanged(Editable s) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Total_Cost_View = findViewById(R.id.Total_Cost_View);
        Total_Cost_wTax_View = findViewById(R.id.Total_Cost_View_wTax);
        Current_Cost_Input = findViewById(R.id.Current_Cost_Input);
        Current_Tax_Input = findViewById(R.id.Current_Tax_Input);
        Current_Cost_Input.setTextLocale(Locale.getDefault());
        Current_Tax_Input.setTextLocale(Locale.getDefault());
        Symbols_to_Remove_From_Input = String.format("[%s,\\s]",NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
        Current_Cost_Input.addTextChangedListener(new Cost_Input_Watcher(Current_Cost_Input));
        //Current_Tax_Input.addTextChangedListener(new Cost_Input_Watcher(Current_Tax_Input));

        findViewById(R.id.Clear_All).performClick();
    }

    public void onAddClick(View view) {
        BigDecimal Current_Cost = new BigDecimal(Current_Cost_Input.getText().toString().replaceAll(Symbols_to_Remove_From_Input,""));
        Total_Cost = Total_Cost.add(Current_Cost);
        Total_Cost_View.setText(Currency_Format.format(Total_Cost));

        BigDecimal Current_Tax = new BigDecimal(Current_Tax_Input.getText().toString()).divide(new BigDecimal("100.0"));
        BigDecimal Current_Cost_wTax = Current_Cost.add(Current_Cost.multiply(Current_Tax));
        Total_Cost_wTax = Total_Cost_wTax.add(Current_Cost_wTax);
        Total_Cost_wTax_View.setText(Currency_Format.format(Total_Cost_wTax));

    }
    public void onClearAllClick(View view) {
        Total_Cost = BigDecimal.ZERO;
        Total_Cost_wTax = BigDecimal.ZERO;
        Current_Cost_Input.setText(BigDecimal.ZERO.toString());
        Current_Tax_Input.setText(BigDecimal.ZERO.toString());
        Total_Cost_View.setText(Currency_Format.format(BigDecimal.ZERO));
        Total_Cost_wTax_View.setText(Currency_Format.format(BigDecimal.ZERO));
    }
}