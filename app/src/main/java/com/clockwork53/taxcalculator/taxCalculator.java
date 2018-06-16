package com.clockwork53.taxcalculator;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

class taxCalculator implements Parcelable {
    private final NumberFormat Currency_Format;
    private final String Symbols_to_Remove_From_Input;
    private BigDecimal totalCost;
    private BigDecimal currentCost;
    private BigDecimal currentTax;
    private BigDecimal totalCostWithTax;

    public static final Creator<taxCalculator> CREATOR = new Creator<taxCalculator>() {
        @Override
        public taxCalculator createFromParcel(Parcel in) {
            return new taxCalculator(in);
        }

        @Override
        public taxCalculator[] newArray(int size) {
            return new taxCalculator[size];
        }
    };

    /**
     * Default Constructor
     */
    taxCalculator() {
        Symbols_to_Remove_From_Input = String.format("[%s,\\s,%%]", NumberFormat.getCurrencyInstance()
                .getCurrency().getSymbol());
        Currency_Format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        reset();
    }

    /**
     * Constructor used for restoring state from a bundle
     *
     * @param in
     */
    taxCalculator(Parcel in) {
        Currency_Format = (NumberFormat) in.readValue(NumberFormat.class.getClassLoader());
        Symbols_to_Remove_From_Input = in.readString();
        totalCost = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
        currentCost = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
        currentTax = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
        totalCostWithTax = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeValue(Currency_Format);
        dest.writeString(Symbols_to_Remove_From_Input);
        dest.writeValue(totalCost);
        dest.writeValue(currentCost);
        dest.writeValue(currentTax);
        dest.writeValue(totalCostWithTax);
    }

    /**
     * Main body of class starts from here
     *
     * @return
     */
    public String getTotalCost() {
        return Currency_Format.format(totalCost);
    }

    public void reset() {
        totalCost = BigDecimal.ZERO;
        currentCost = BigDecimal.ZERO;
        currentTax = BigDecimal.ZERO;
        totalCostWithTax = BigDecimal.ZERO;
    }

    public void addCurrentToTotal(Editable a) {
        String temp = a.toString();
        temp = temp.replaceAll(Symbols_to_Remove_From_Input, "");
        try {
            currentCost = new BigDecimal(temp);
            totalCost = totalCost.add(currentCost);
        } catch (NumberFormatException e) {
            e.getMessage();
        }
    }

    public String addCurrentToTotalWithTax(Editable a) {
        String temp = a.toString();
        temp = temp.replaceAll(Symbols_to_Remove_From_Input, "");
        try {
            currentTax = new BigDecimal(temp);
            if (!currentTax.equals(new BigDecimal("100")))
                currentTax = currentTax.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_FLOOR);
            else
                currentTax = BigDecimal.ONE;
            BigDecimal currentCostWithTax = currentCost.add(currentCost.multiply(currentTax));
            totalCostWithTax = totalCostWithTax.add(currentCostWithTax);
        } catch (NumberFormatException e) {
            e.getMessage();
        }

        return Currency_Format.format(totalCostWithTax);
    }
}
