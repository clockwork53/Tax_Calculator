package com.clockwork53.taxcalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final NumberFormat Currency_Format = NumberFormat.getCurrencyInstance(Locale.getDefault());
    private EditText Current_Cost_Input;
    private EditText Current_Tax_Input;
    private TextView Total_Cost_View;
    private TextView Total_Cost_wTax_View;
    private Boolean hasDoneClearAll = false;
    private taxCalculator tcInstance = new taxCalculator();


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save state to the savedInstanceState
        savedInstanceState.putString("Current_Cost_Input", Current_Cost_Input.getText().toString());
        savedInstanceState.putString("Current_Tax_Input", Current_Tax_Input.getText().toString());
        savedInstanceState.putString("Total_Cost_wTax_View", Total_Cost_wTax_View.getText().toString());
        savedInstanceState.putString("Total_Cost_View", Total_Cost_View.getText().toString());
        savedInstanceState.putBoolean("hasDoneClearAll", hasDoneClearAll);
        savedInstanceState.putParcelable("tcInstance", tcInstance);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state from savedInstanceState
        Current_Cost_Input.setText(savedInstanceState.getString("Current_Cost_Input"));
        Current_Tax_Input.setText(savedInstanceState.getString("Current_Tax_Input"));
        Total_Cost_View.setText(savedInstanceState.getString("Total_Cost_View"));
        Total_Cost_wTax_View.setText(savedInstanceState.getString("Total_Cost_wTax_View"));
        hasDoneClearAll = savedInstanceState.getBoolean("hasDoneClearAll");
        tcInstance = savedInstanceState.getParcelable("tcInstance");
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

        Current_Cost_Input.addTextChangedListener(new costInputWatcher(Current_Cost_Input));
        Current_Tax_Input.addTextChangedListener(new taxInputWatcher(Current_Tax_Input));

        if (!hasDoneClearAll) {
            findViewById(R.id.Clear_All).performClick();
            hasDoneClearAll = true;
        }
    }

    public void onAddClick(View view) {

        tcInstance.addCurrentToTotal(Current_Cost_Input.getText());

        Total_Cost_View.setText(tcInstance.getTotalCost());
        Total_Cost_wTax_View.setText(tcInstance.addCurrentToTotalWithTax(Current_Tax_Input.getText()));

    }

    @SuppressLint("SetTextI18n")
    public void onClearAllClick(View view) {
        tcInstance.reset();
        Current_Cost_Input.setText(BigDecimal.ZERO.toString());
        Current_Tax_Input.setText(BigDecimal.ZERO.toString());
        Total_Cost_View.setText(Currency_Format.format(BigDecimal.ZERO));
        Total_Cost_wTax_View.setText(Currency_Format.format(BigDecimal.ZERO));
    }
}