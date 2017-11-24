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
            String[] unitsDistance = res.getStringArray(R.array.units_distance);
            String[] unitsMass = res.getStringArray(R.array.units_mass);
            String[] unitsTemperature = res.getStringArray(R.array.units_temperature);
            String[] unitsVolume = res.getStringArray(R.array.units_volume);

            if (unitString.equals(unitsDistance[0])) {return INCHES;}
            if (unitString.equals(unitsDistance[1])) {return FEET;}
            if (unitString.equals(unitsDistance[2])) {return YARDS;}
            if (unitString.equals(unitsDistance[3])) {return MILES;}
            if (unitString.equals(unitsDistance[4])) {return MILLIMETERS;}
            if (unitString.equals(unitsDistance[5])) {return CENTIMETERS;}
            if (unitString.equals(unitsDistance[6])) {return METERS;}
            if (unitString.equals(unitsDistance[7])) {return KILOMETERS;}
            if (unitString.equals(unitsMass[0])) {return OUNCES;}
            if (unitString.equals(unitsMass[1])) {return POUNDS;}
            if (unitString.equals(unitsMass[2])) {return MILLIGRAMS;}
            if (unitString.equals(unitsMass[3])) {return GRAMS;}
            if (unitString.equals(unitsMass[4])) {return KILOGRAMS;}
            if (unitString.equals(unitsTemperature[0])) {return DEGREES_FAHRENHEIT;}
            if (unitString.equals(unitsTemperature[1])) {return DEGREES_CELSIUS;}
            if (unitString.equals(unitsTemperature[2])) {return DEGREES_KELVIN;}
            if (unitString.equals(unitsVolume[0])) {return TEASPOONS;}
            if (unitString.equals(unitsVolume[1])) {return TABLESPOONS;}
            if (unitString.equals(unitsVolume[2])) {return FLUID_OUNCES;}
            if (unitString.equals(unitsVolume[3])) {return CUPS;}
            if (unitString.equals(unitsVolume[4])) {return PINTS;}
            if (unitString.equals(unitsVolume[5])) {return QUARTS;}
            if (unitString.equals(unitsVolume[6])) {return GALLONS;}
            if (unitString.equals(unitsVolume[7])) {return MILLILITERS;}
            if (unitString.equals(unitsVolume[8])) {return LITERS;}
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
        String[] unitsDistance = res.getStringArray(R.array.units_distance);
        String[] unitsMass = res.getStringArray(R.array.units_mass);
        String[] unitsTemperature = res.getStringArray(R.array.units_temperature);
        String[] unitsVolume = res.getStringArray(R.array.units_volume);

        switch(this) {
            case INCHES: return unitsDistance[0];
            case FEET: return unitsDistance[1];
            case YARDS: return unitsDistance[2];
            case MILES: return unitsDistance[3];
            case MILLIMETERS: return unitsDistance[4];
            case CENTIMETERS: return unitsDistance[5];
            case METERS: return unitsDistance[6];
            case KILOMETERS: return unitsDistance[7];
            case OUNCES: return unitsMass[0];
            case POUNDS: return unitsMass[1];
            case MILLIGRAMS: return unitsMass[2];
            case GRAMS: return unitsMass[3];
            case KILOGRAMS: return unitsMass[4];
            case DEGREES_FAHRENHEIT: return unitsTemperature[0];
            case DEGREES_CELSIUS: return unitsTemperature[1];
            case DEGREES_KELVIN: return unitsTemperature[2];
            case TEASPOONS: return unitsVolume[0];
            case TABLESPOONS: return unitsVolume[1];
            case FLUID_OUNCES: return unitsVolume[2];
            case CUPS: return unitsVolume[3];
            case PINTS: return unitsVolume[4];
            case QUARTS: return unitsVolume[5];
            case GALLONS: return unitsVolume[6];
            case MILLILITERS: return unitsVolume[7];
            case LITERS: return unitsVolume[8];
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
