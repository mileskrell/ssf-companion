package org.cpsscifair.ssfcompanion;

import android.content.res.Resources;

import java.math.BigDecimal;

/**
 * Represents some amount of a {@link Unit}
 */
class Quantity {

    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal THOUSAND = new BigDecimal("1000");

    private Resources res;
    private Unit unit;
    private String amount;

    Quantity(Resources res, Unit unit, String amount) {
        this.res = res;
        this.unit = unit;
        this.amount = amount;
    }

    public String toString() {
        return amount + " " + unit.toAbbr(res);
    }

    //////////////////////////////////////////////////////////////// CONVERSION

    Quantity convertTo(Unit targetUnit) {
        BigDecimal amount = new BigDecimal(this.amount);
        String result = null;

        switch (unit.getType()) {
            // Split into multiple methods for easier readability
            case DISTANCE:
                result = convertDistance(amount, targetUnit).toString();
                break;
            case MASS:
                result = convertMass(amount, targetUnit).toString();
                break;
            case VOLUME:
                result = convertVolume(amount, targetUnit).toString();
                break;
        }

        return new Quantity(res, targetUnit, result);
    }

    private BigDecimal convertDistance(BigDecimal amount, Unit targetUnit) {
        BigDecimal amountMeters = BigDecimal.ZERO;
        switch (unit) {
//            case INCHES: amountMeters = amount.; break;
//            case FEET: amountMeters = amount.; break;
//            case YARDS: amountMeters = amount.; break;
//            case MILES: amountMeters = amount.; break;
            case MILLIMETERS: amountMeters = amount.divide(THOUSAND); break;
            case CENTIMETERS: amountMeters = amount.divide(HUNDRED); break;
            case METERS: amountMeters = amount; break;
            case KILOMETERS: amountMeters = amount.multiply(THOUSAND); break;
        }
        switch (targetUnit) {
//            case INCHES: return amountMeters.;
//            case FEET: return amountMeters.;
//            case YARDS: return amountMeters.;
//            case MILES: return amountMeters.;
            case MILLIMETERS: return amountMeters.multiply(THOUSAND);
            case CENTIMETERS: return amountMeters.multiply(HUNDRED);
            case METERS: return amountMeters;
            case KILOMETERS: return amountMeters.divide(THOUSAND);
        }
        return new BigDecimal(111);
    }

    private BigDecimal convertMass(BigDecimal amount, Unit targetUnit) {
        BigDecimal amountGrams = BigDecimal.ZERO;
        switch (unit) {
//            case OUNCES: amountGrams = amount.; break;
//            case POUNDS: amountGrams = amount.; break;
            case MILLIGRAMS: amountGrams = amount.divide(THOUSAND); break;
            case GRAMS: amountGrams = amount; break;
            case KILOGRAMS: amountGrams = amount.multiply(THOUSAND); break;
        }
        switch (targetUnit) {
//            case OUNCES: return amountGrams.;
//            case POUNDS: return amountGrams.;
            case MILLIGRAMS: return amountGrams.multiply(THOUSAND);
            case GRAMS: return amountGrams;
            case KILOGRAMS: return amountGrams.divide(THOUSAND);
        }
        return new BigDecimal(222);
    }

    private BigDecimal convertVolume(BigDecimal amount, Unit targetUnit) {
        BigDecimal amountLiters = BigDecimal.ZERO;
        switch (unit) {
//            case FLUID_OUNCES: amountLiters = amount.; break;
//            case PINTS: amountLiters = amount.; break;
//            case QUARTS: amountLiters = amount.; break;
//            case GALLONS: amountLiters = amount.; break;
            case MILLILITERS: amountLiters = amount.divide(THOUSAND); break;
            case LITERS: amountLiters = amount; break;
        }
        switch (targetUnit) {
//            case FLUID_OUNCES: return amountLiters.;
//            case PINTS: return amountLiters.;
//            case QUARTS: return amountLiters.;
//            case GALLONS: return amountLiters.;
            case MILLILITERS: return amountLiters.multiply(THOUSAND);
            case LITERS: return amountLiters;
        }
        return new BigDecimal(333);
    }
}
