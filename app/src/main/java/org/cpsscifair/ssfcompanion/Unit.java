package org.cpsscifair.ssfcompanion;

import android.content.res.Resources;

/**
 * Represents a unit of measurement
 */
enum Unit {
    EMPTY_UNIT,
    INCHES, FEET, YARDS, MILES, MILLIMETERS, CENTIMETERS, METERS, KILOMETERS,
    OUNCES, POUNDS, MILLIGRAMS, GRAMS, KILOGRAMS,
    DEGREES_FAHRENHEIT, DEGREES_CELSIUS, DEGREES_KELVIN,
    TEASPOONS, TABLESPOONS, FLUID_OUNCES, CUPS, PINTS, QUARTS, GALLONS, MILLILITERS, LITERS;

    enum UnitType {
        EMPTY_TYPE, DISTANCE, MASS, TEMPERATURE, VOLUME
    }

    static Unit fromString(Resources res, String unitString) {
        if (unitString != null) {
            // Can't use a switch without using constants
            if (unitString.equals(res.getString(R.string.inches))) {return INCHES;}
            if (unitString.equals(res.getString(R.string.feet))) {return FEET;}
            if (unitString.equals(res.getString(R.string.yards))) {return YARDS;}
            if (unitString.equals(res.getString(R.string.miles))) {return MILES;}
            if (unitString.equals(res.getString(R.string.millimeters))) {return MILLIMETERS;}
            if (unitString.equals(res.getString(R.string.centimeters))) {return CENTIMETERS;}
            if (unitString.equals(res.getString(R.string.meters))) {return METERS;}
            if (unitString.equals(res.getString(R.string.kilometers))) {return KILOMETERS;}
            if (unitString.equals(res.getString(R.string.ounces))) {return OUNCES;}
            if (unitString.equals(res.getString(R.string.pounds))) {return POUNDS;}
            if (unitString.equals(res.getString(R.string.milligrams))) {return MILLIGRAMS;}
            if (unitString.equals(res.getString(R.string.grams))) {return GRAMS;}
            if (unitString.equals(res.getString(R.string.kilograms))) {return KILOGRAMS;}
            if (unitString.equals(res.getString(R.string.degrees_fahrenheit))) {return DEGREES_FAHRENHEIT;}
            if (unitString.equals(res.getString(R.string.degrees_celsius))) {return DEGREES_CELSIUS;}
            if (unitString.equals(res.getString(R.string.degrees_kelvin))) {return DEGREES_KELVIN;}
            if (unitString.equals(res.getString(R.string.teaspoons))) {return TEASPOONS;}
            if (unitString.equals(res.getString(R.string.tablespoons))) {return TABLESPOONS;}
            if (unitString.equals(res.getString(R.string.fluid_ounces))) {return FLUID_OUNCES;}
            if (unitString.equals(res.getString(R.string.cups))) {return CUPS;}
            if (unitString.equals(res.getString(R.string.pints))) {return PINTS;}
            if (unitString.equals(res.getString(R.string.quarts))) {return QUARTS;}
            if (unitString.equals(res.getString(R.string.gallons))) {return GALLONS;}
            if (unitString.equals(res.getString(R.string.milliliters))) {return MILLILITERS;}
            if (unitString.equals(res.getString(R.string.liters))) {return LITERS;}
        }
        return EMPTY_UNIT;
    }

    UnitType getType() {
        switch (this) {
            case INCHES: case FEET: case YARDS: case MILES:
            case MILLIMETERS: case CENTIMETERS: case METERS: case KILOMETERS:
                return UnitType.DISTANCE;
            case OUNCES: case POUNDS:
            case MILLIGRAMS: case GRAMS: case KILOGRAMS:
                return UnitType.MASS;
            case DEGREES_FAHRENHEIT: case DEGREES_CELSIUS: case DEGREES_KELVIN:
                return UnitType.TEMPERATURE;
            case TEASPOONS: case TABLESPOONS: case FLUID_OUNCES: case CUPS:
            case PINTS: case QUARTS: case GALLONS:
            case MILLILITERS: case LITERS:
                return UnitType.VOLUME;
            default:
                return UnitType.EMPTY_TYPE;
        }
    }

    String toUpperCaseString(Resources res) {
        switch(this) {
            case INCHES: return res.getString(R.string.inches);
            case FEET: return res.getString(R.string.feet);
            case YARDS: return res.getString(R.string.yards);
            case MILES: return res.getString(R.string.miles);
            case MILLIMETERS: return res.getString(R.string.millimeters);
            case CENTIMETERS: return res.getString(R.string.centimeters);
            case METERS: return res.getString(R.string.meters);
            case KILOMETERS: return res.getString(R.string.kilometers);
            case OUNCES: return res.getString(R.string.ounces);
            case POUNDS: return res.getString(R.string.pounds);
            case MILLIGRAMS: return res.getString(R.string.milligrams);
            case GRAMS: return res.getString(R.string.grams);
            case KILOGRAMS: return res.getString(R.string.kilograms);
            case DEGREES_FAHRENHEIT: return res.getString(R.string.degrees_fahrenheit);
            case DEGREES_CELSIUS: return res.getString(R.string.degrees_celsius);
            case DEGREES_KELVIN: return res.getString(R.string.degrees_kelvin);
            case TEASPOONS: return res.getString(R.string.teaspoons);
            case TABLESPOONS: return res.getString(R.string.tablespoons);
            case FLUID_OUNCES: return res.getString(R.string.fluid_ounces);
            case CUPS: return res.getString(R.string.cups);
            case PINTS: return res.getString(R.string.pints);
            case QUARTS: return res.getString(R.string.quarts);
            case GALLONS: return res.getString(R.string.gallons);
            case MILLILITERS: return res.getString(R.string.milliliters);
            case LITERS: return res.getString(R.string.liters);
        }
        return null;
    }

    String toLowerCaseString(Resources res) {
        String upperCase = toUpperCaseString(res);
        return String.valueOf(upperCase.charAt(0)).toLowerCase() + upperCase.substring(1);
    }

    String toAbbr(Resources res) {
        switch(this) {
            case INCHES: return res.getString(R.string.inches_abbr);
            case FEET: return res.getString(R.string.feet_abbr);
            case YARDS: return res.getString(R.string.yards_abbr);
            case MILES: return res.getString(R.string.miles_abbr);
            case MILLIMETERS: return res.getString(R.string.millimeters_abbr);
            case CENTIMETERS: return res.getString(R.string.centimeters_abbr);
            case METERS: return res.getString(R.string.meters_abbr);
            case KILOMETERS: return res.getString(R.string.kilometers_abbr);
            case OUNCES: return res.getString(R.string.ounces_abbr);
            case POUNDS: return res.getString(R.string.pounds_abbr);
            case MILLIGRAMS: return res.getString(R.string.milligrams_abbr);
            case GRAMS: return res.getString(R.string.grams_abbr);
            case KILOGRAMS: return res.getString(R.string.kilograms_abbr);
            case DEGREES_FAHRENHEIT: return res.getString(R.string.degrees_fahrenheit_abbr);
            case DEGREES_CELSIUS: return res.getString(R.string.degrees_celsius_abbr);
            case DEGREES_KELVIN: return res.getString(R.string.degrees_kelvin_abbr);
            case TEASPOONS: return res.getString(R.string.teaspoons_abbr);
            case TABLESPOONS: return res.getString(R.string.tablespoons_abbr);
            case FLUID_OUNCES: return res.getString(R.string.fluid_ounces_abbr);
            case CUPS: return res.getString(R.string.cups_abbr);
            case PINTS: return res.getString(R.string.pints_abbr);
            case QUARTS: return res.getString(R.string.quarts_abbr);
            case GALLONS: return res.getString(R.string.gallons_abbr);
            case MILLILITERS: return res.getString(R.string.milliliters_abbr);
            case LITERS: return res.getString(R.string.liters_abbr);
        }
        return null;
    }
}
