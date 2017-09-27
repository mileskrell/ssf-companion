package org.cpsscifair.ssfcompanion;

import android.content.res.Resources;

import java.math.BigDecimal;

/**
 * Represents some amount of a {@link Unit}
 */
class Quantity {

    private static final int SCALE = 8;
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal THOUSAND = new BigDecimal("1000");

    // Multiply by these to convert to the base unit.
    // Divide by these to convert to the other unit.
    private static final BigDecimal METERS_PER_INCH = new BigDecimal("0.0254");
    private static final BigDecimal METERS_PER_FOOT = new BigDecimal("0.3048");
    private static final BigDecimal METERS_PER_YARD = new BigDecimal("0.9144");
    private static final BigDecimal METERS_PER_MILE = new BigDecimal("1609.344");
    private static final BigDecimal GRAMS_PER_OUNCE = new BigDecimal("28.349523125");
    private static final BigDecimal GRAMS_PER_POUND = new BigDecimal("453.59237");

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
                result = convertDistance(amount, targetUnit);
                break;
            case MASS:
                result = convertMass(amount, targetUnit);
                break;
            case VOLUME:
                result = convertVolume(amount, targetUnit);
                break;
        }

        return new Quantity(res, targetUnit, result);
    }

    private String convertDistance(BigDecimal amount, Unit targetUnit) {
        BigDecimal amountMeters = BigDecimal.ZERO;
        boolean inexact = false;
        switch (unit) {
            case INCHES:
                amountMeters = amount.multiply(METERS_PER_INCH);
                break;
            case FEET:
                amountMeters = amount.multiply(METERS_PER_FOOT);
                break;
            case YARDS:
                amountMeters = amount.multiply(METERS_PER_YARD);
                break;
            case MILES:
                amountMeters = amount.multiply(METERS_PER_MILE);
                break;
            case MILLIMETERS:
                amountMeters = amount.divide(THOUSAND);
                break;
            case CENTIMETERS:
                amountMeters = amount.divide(HUNDRED);
                break;
            case METERS:
                amountMeters = amount;
                break;
            case KILOMETERS:
                amountMeters = amount.multiply(THOUSAND);
                break;
        }
        BigDecimal amountFinal = BigDecimal.ZERO;
        switch (targetUnit) {
            case INCHES:
                try {
                    amountFinal = amountMeters.divide(METERS_PER_INCH);
                } catch (ArithmeticException e) {
                    amountFinal = amountMeters.divide(METERS_PER_INCH, SCALE, BigDecimal.ROUND_HALF_UP);
                    inexact = true;
                }
                break;
            case FEET:
                try {
                    amountFinal = amountMeters.divide(METERS_PER_FOOT);
                } catch (ArithmeticException e) {
                    amountFinal = amountMeters.divide(METERS_PER_FOOT, SCALE, BigDecimal.ROUND_HALF_UP);
                    inexact = true;
                }
                break;
            case YARDS:
                try {
                    amountFinal = amountMeters.divide(METERS_PER_YARD);
                } catch (ArithmeticException e) {
                    amountFinal = amountMeters.divide(METERS_PER_YARD, SCALE, BigDecimal.ROUND_HALF_UP);
                    inexact = true;
                }
                break;
            case MILES:
                try {
                    amountFinal = amountMeters.divide(METERS_PER_MILE);
                } catch (ArithmeticException e) {
                    amountFinal = amountMeters.divide(METERS_PER_MILE, SCALE, BigDecimal.ROUND_HALF_UP);
                    inexact = true;
                }
                break;
            case MILLIMETERS:
                amountFinal = amountMeters.multiply(THOUSAND);
                break;
            case CENTIMETERS:
                amountFinal = amountMeters.multiply(HUNDRED);
                break;
            case METERS:
                amountFinal = amountMeters;
                break;
            case KILOMETERS:
                amountFinal = amountMeters.divide(THOUSAND);
                break;
        }
        String result = amountFinal.stripTrailingZeros().toPlainString();
        if (inexact) {
            return "≈ " + result;
        } else {
            return "= " + result;
        }
    }

    private String convertMass(BigDecimal amount, Unit targetUnit) {
        BigDecimal amountGrams = BigDecimal.ZERO;
        boolean inexact = false;
        switch (unit) {
            case OUNCES:
                amountGrams = amount.multiply(GRAMS_PER_OUNCE);
                break;
            case POUNDS:
                amountGrams = amount.multiply(GRAMS_PER_POUND);
                break;
            case MILLIGRAMS:
                amountGrams = amount.divide(THOUSAND);
                break;
            case GRAMS:
                amountGrams = amount;
                break;
            case KILOGRAMS:
                amountGrams = amount.multiply(THOUSAND);
                break;
        }
        BigDecimal amountFinal = BigDecimal.ZERO;
        switch (targetUnit) {
            case OUNCES:
                try {
                    amountFinal = amountGrams.divide(GRAMS_PER_OUNCE);
                } catch (ArithmeticException e) {
                    amountFinal = amountGrams.divide(GRAMS_PER_OUNCE, SCALE, BigDecimal.ROUND_HALF_UP);
                    inexact = true;
                }
                break;
            case POUNDS:
                try {
                    amountFinal = amountGrams.divide(GRAMS_PER_POUND);
                } catch (ArithmeticException e) {
                    amountFinal = amountGrams.divide(GRAMS_PER_POUND, SCALE, BigDecimal.ROUND_HALF_UP);
                    inexact = true;
                }
                break;
            case MILLIGRAMS:
                amountFinal = amountGrams.multiply(THOUSAND);
                break;
            case GRAMS:
                amountFinal = amountGrams;
                break;
            case KILOGRAMS:
                amountFinal = amountGrams.divide(THOUSAND);
                break;
        }
        String result = amountFinal.stripTrailingZeros().toPlainString();
        if (inexact) {
            return "≈ " + result;
        } else {
            return "= " + result;
        }
    }

    private String convertVolume(BigDecimal amount, Unit targetUnit) {
        BigDecimal amountLiters = BigDecimal.ZERO;
        boolean inexact = false;
        switch (unit) {
//            case FLUID_OUNCES: amountLiters = amount.; break;
//            case PINTS: amountLiters = amount.; break;
//            case QUARTS: amountLiters = amount.; break;
//            case GALLONS: amountLiters = amount.; break;
            case MILLILITERS:
                amountLiters = amount.divide(THOUSAND);
                break;
            case LITERS:
                amountLiters = amount;
                break;
        }
        BigDecimal amountFinal = BigDecimal.ZERO;
        switch (targetUnit) {
//            case FLUID_OUNCES: return amountLiters.;
//            case PINTS: return amountLiters.;
//            case QUARTS: return amountLiters.;
//            case GALLONS: return amountLiters.;
            case MILLILITERS:
                amountFinal = amountLiters.multiply(THOUSAND);
                break;
            case LITERS:
                amountFinal = amountLiters;
                break;
        }
        String result = amountFinal.stripTrailingZeros().toPlainString();
        if (inexact) {
            return "≈ " + result;
        } else {
            return "= " + result;
        }
    }
}
