package org.cpsscifair.ssfcompanion;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import org.cpsscifair.ssfcompanion.databinding.FragmentUnitConverterBinding;

// We're not using Spinners because they don't support submenus.

// TODO: 9/16/17 Save instance state

public class UnitConverterFragment extends Fragment {

    private FragmentUnitConverterBinding binding;

    // Enum to represent the different menus for the second Button
    private enum SecondaryMenuType {
        DISTANCE, MASS, VOLUME
    }

    private SecondaryMenuType oldSecondaryMenuType;

    // Enum to represent the different units for each field
    private enum Unit {
        INCHES, FEET, YARDS, MILES, MILLIMETERS, CENTIMETERS, METERS, KILOMETERS,
        OUNCES, POUNDS, MILLIGRAMS, GRAMS, KILOGRAMS,
        FLUID_OUNCES, PINTS, QUARTS, GALLONS, MILLILITERS, LITERS
    }

    private Unit unitPrimary, unitSecondary;

    private PopupMenu popupMenuPrimary, popupMenuSecondary;

    // The current text contained within editTextInput
    private String inputString ="";

    public UnitConverterFragment() {
        // Required empty public constructor
    }

    public static UnitConverterFragment newInstance() {
        return new UnitConverterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_unit_converter, container, false);
        View v = binding.getRoot();

        // Set up the primary PopupMenu

        popupMenuPrimary = new PopupMenu(getContext(), binding.buttonPrimary);
        popupMenuPrimary.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_primary_id_distance:
                    case R.id.menu_primary_id_mass:
                    case R.id.menu_primary_id_volume:
                        // These are just categories, so clicking them
                        // should just open their respective submenus
                        break;
                    default:
                        // If we're here, it means that the user has
                        // selected an actual item from the primary menu
                        unitPrimary = getUnitFromString(item.getTitle().toString());
                        binding.buttonPrimary.setText(item.getTitle());

                        SecondaryMenuType newSecondaryMenuType = null;

                        // Depending on the type of item they selected,
                        // set the secondary Button's menu
                        switch (item.getGroupId()) {
                            case R.id.menu_primary_group_distance:
                                newSecondaryMenuType = SecondaryMenuType.DISTANCE;
                                setSecondaryButtonMenu(SecondaryMenuType.DISTANCE);
                                break;
                            case R.id.menu_primary_group_mass:
                                newSecondaryMenuType = SecondaryMenuType.MASS;
                                setSecondaryButtonMenu(SecondaryMenuType.MASS);
                                break;
                            case R.id.menu_primary_group_volume:
                                newSecondaryMenuType = SecondaryMenuType.VOLUME;
                                setSecondaryButtonMenu(SecondaryMenuType.VOLUME);
                                break;
                        }

                        if (oldSecondaryMenuType != null
                                && oldSecondaryMenuType.equals(newSecondaryMenuType)) {
                            // Same kind of unit as before, so we don't need to clear anything
                            // TODO: 9/16/17 Update the output here

                        } else {
                            // Different kind of unit, so we need to clear things
                            oldSecondaryMenuType = newSecondaryMenuType;

                            // Only matters if *not* first time
                            unitSecondary = null;

                            // Set the secondary Button's text to "Select second unit"
                            binding.buttonSecondary.setText(R.string.select_second_unit);

                            // Make secondary Button visible (this
                            // only matters the first time around)
                            binding.buttonSecondary.setVisibility(View.VISIBLE);

                            // Hide output LinearLayout
                            binding.linearLayoutOutput.setVisibility(View.GONE);
                        }

                        // Set the input hint.
                        // In a language like German, this wouldn't be lower case,
                        // but it should work fine for most languages.
                        binding.editTextInput.setHint(getString(R.string.enter_units,
                                binding.buttonPrimary.getText().toString().toLowerCase()));

                        // Set the input unit abbreviation
                        binding.unitDisplayInput.setText(getAbbrFromUnit(unitPrimary));
                }
                return true;
            }
        });
        popupMenuPrimary.inflate(R.menu.popup_menu_primary);

        // Set an OnClickListener for each Button

        binding.buttonPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenuPrimary.show();
            }
        });

        binding.buttonSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenuSecondary.show();
            }
        });

        // Add the TextWatcher for the first EditText

        binding.editTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputString = s.toString();
                if (inputString.isEmpty()) {
                    binding.unitDisplayInput.setVisibility(View.GONE);
                    binding.linearLayoutOutput.setVisibility(View.GONE);
                } else {
                    binding.unitDisplayInput.setText(getAbbrFromUnit(unitPrimary));
                    binding.unitDisplayInput.setVisibility(View.VISIBLE);

                    // TODO: 9/16/17 Do conversion here (user selected unit, then entered input)

                    if (unitSecondary != null) {
                        binding.unitDisplayOutput.setText(getAbbrFromUnit(unitSecondary));
                        binding.linearLayoutOutput.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return v;
    }

    private void setSecondaryButtonMenu(SecondaryMenuType secondaryMenuType) {
        popupMenuSecondary = new PopupMenu(getContext(), binding.buttonSecondary);

        switch (secondaryMenuType) {
            case DISTANCE:
                popupMenuSecondary.inflate(R.menu.popup_menu_distance);
                break;
            case MASS:
                popupMenuSecondary.inflate(R.menu.popup_menu_mass);
                break;
            case VOLUME:
                popupMenuSecondary.inflate(R.menu.popup_menu_volume);
                break;
        }

        popupMenuSecondary.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                unitSecondary = getUnitFromString(item.getTitle().toString());
                binding.buttonSecondary.setText(item.getTitle());

                binding.unitDisplayOutput.setText(getAbbrFromUnit(unitSecondary));

                binding.linearLayoutInput.setVisibility(View.VISIBLE);

                // Display output
                // TODO: 9/16/17 Do conversion here (user entered input, then selected unit)
                if (! inputString.isEmpty()) {
                    binding.linearLayoutOutput.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

    private Unit getUnitFromString(String unitString) {
        // Can't use a switch without using constants
        if (unitString.equals(getString(R.string.inches))) {return Unit.INCHES;}
        if (unitString.equals(getString(R.string.feet))) {return Unit.FEET;}
        if (unitString.equals(getString(R.string.yards))) {return Unit.YARDS;}
        if (unitString.equals(getString(R.string.miles))) {return Unit.MILES;}
        if (unitString.equals(getString(R.string.millimeters))) {return Unit.MILLIMETERS;}
        if (unitString.equals(getString(R.string.centimeters))) {return Unit.CENTIMETERS;}
        if (unitString.equals(getString(R.string.meters))) {return Unit.METERS;}
        if (unitString.equals(getString(R.string.kilometers))) {return Unit.KILOMETERS;}
        if (unitString.equals(getString(R.string.ounces))) {return Unit.OUNCES;}
        if (unitString.equals(getString(R.string.pounds))) {return Unit.POUNDS;}
        if (unitString.equals(getString(R.string.milligrams))) {return Unit.MILLIGRAMS;}
        if (unitString.equals(getString(R.string.grams))) {return Unit.GRAMS;}
        if (unitString.equals(getString(R.string.kilograms))) {return Unit.KILOGRAMS;}
        if (unitString.equals(getString(R.string.fluid_ounces))) {return Unit.FLUID_OUNCES;}
        if (unitString.equals(getString(R.string.pints))) {return Unit.PINTS;}
        if (unitString.equals(getString(R.string.quarts))) {return Unit.QUARTS;}
        if (unitString.equals(getString(R.string.gallons))) {return Unit.GALLONS;}
        if (unitString.equals(getString(R.string.milliliters))) {return Unit.MILLILITERS;}
        if (unitString.equals(getString(R.string.liters))) {return Unit.LITERS;}
        else {return null;}
    }

    private String getAbbrFromUnit(Unit unit) {
        switch (unit) {
            case INCHES: return getString(R.string.inches_abbr);
            case FEET: return getString(R.string.feet_abbr);
            case YARDS: return getString(R.string.yards_abbr);
            case MILES: return getString(R.string.miles_abbr);
            case MILLIMETERS: return getString(R.string.millimeters_abbr);
            case CENTIMETERS: return getString(R.string.centimeters_abbr);
            case METERS: return getString(R.string.meters_abbr);
            case KILOMETERS: return getString(R.string.kilometers_abbr);
            case OUNCES: return getString(R.string.ounces_abbr);
            case POUNDS: return getString(R.string.pounds_abbr);
            case MILLIGRAMS: return getString(R.string.milligrams_abbr);
            case GRAMS: return getString(R.string.grams_abbr);
            case KILOGRAMS: return getString(R.string.kilograms_abbr);
            case FLUID_OUNCES: return getString(R.string.fluid_ounces_abbr);
            case PINTS: return getString(R.string.pints_abbr);
            case QUARTS: return getString(R.string.quarts_abbr);
            case GALLONS: return getString(R.string.gallons_abbr);
            case MILLILITERS: return getString(R.string.milliliters_abbr);
            case LITERS: return getString(R.string.liters_abbr);
            default: return null;
        }
    }
}
