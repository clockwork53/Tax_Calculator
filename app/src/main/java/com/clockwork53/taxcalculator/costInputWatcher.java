package com.clockwork53.taxcalculator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class costInputWatcher implements TextWatcher {

    private String Symbols_to_Remove_From_Input = String.format("[%s,\\s]", NumberFormat
            .getCurrencyInstance().getCurrency().getSymbol());
    private EditText editTextInstance;
    private String Current_Input_Value = "";


    costInputWatcher(EditText e) {
        editTextInstance = e;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(Current_Input_Value)) {
            editTextInstance.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll(Symbols_to_Remove_From_Input, "");

            BigDecimal parsed = new BigDecimal(cleanString);
            String formatted = NumberFormat.getCurrencyInstance().format((parsed.divide(new BigDecimal("100.0"), BigDecimal.ROUND_HALF_UP)));

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
