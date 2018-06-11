package com.clockwork53.taxcalculator;

import android.text.Editable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

class taxCalculator {
    private final NumberFormat Currency_Format;
    private final String Symbols_to_Remove_From_Input;
    private BigDecimal totalCost;
    private BigDecimal currentCost;
    private BigDecimal currentTax;
    private BigDecimal totalCostWithTax;

    taxCalculator()
    {
        Symbols_to_Remove_From_Input = String.format("[%s,\\s,%%]", NumberFormat.getCurrencyInstance()
                .getCurrency().getSymbol());
        Currency_Format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        reset();
    }
    public String getTotalCost()
    {
        return Currency_Format.format(totalCost);
    }
    public void reset()
    {
        totalCost = BigDecimal.ZERO;
        currentCost =BigDecimal.ZERO;
        currentTax = BigDecimal.ZERO;
        totalCostWithTax = BigDecimal.ZERO;
    }

    public void addCurrentToTotal(Editable a)
    {
        String temp = a.toString();
        temp = temp.replaceAll(Symbols_to_Remove_From_Input, "");
        try {
            currentCost = new BigDecimal(temp);
            totalCost = totalCost.add(currentCost);
        } catch (NumberFormatException e) {
            e.getMessage();
        }
    }

    public String addCurrentToTotalWithTax(Editable a)
    {
        String temp = a.toString();
        temp = temp.replaceAll(Symbols_to_Remove_From_Input, "");
        try {
            currentTax = new BigDecimal(temp);
            if (!currentTax.equals(new BigDecimal("100")))
                currentTax = currentTax.divide(new BigDecimal("100"), BigDecimal.ROUND_FLOOR);
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
