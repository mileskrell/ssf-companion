package org.cpsscifair.ssfcompanion;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import org.cpsscifair.ssfcompanion.databinding.FragmentUnitConverterBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class UnitConverterFragment extends Fragment {

    // For saving instance state
    private static final String UNIT_TYPE = "unit_type";
    private static final String PRIMARY_UNIT = "primary_unit";
    private static final String SECONDARY_UNIT = "secondary_unit";

    private FragmentUnitConverterBinding binding;

    private Unit.UnitType currentUnitType = Unit.UnitType.EMPTY_TYPE;
    private Unit currentPrimaryUnit = Unit.EMPTY_UNIT;
    private Unit currentSecondaryUnit = Unit.EMPTY_UNIT;

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

        if (savedInstanceState != null) {
            // Get the stored variables
            Unit.UnitType recoveredUnitType = Unit.UnitType.values()[savedInstanceState.getInt(UNIT_TYPE)];
            Unit recoveredPrimaryUnit = Unit.values()[savedInstanceState.getInt(PRIMARY_UNIT)];
            Unit recoveredSecondaryUnit = Unit.values()[savedInstanceState.getInt(SECONDARY_UNIT)];

            if (recoveredUnitType != Unit.UnitType.EMPTY_TYPE) {
                onChangeUnitType(recoveredUnitType);
                onChangePrimaryUnit(recoveredPrimaryUnit);
                onChangeSecondaryUnit(recoveredSecondaryUnit);
            }
        }

        binding.textViewDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeUnitType(Unit.UnitType.DISTANCE);
            }
        });
        binding.textViewMass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeUnitType(Unit.UnitType.MASS);
            }
        });
        binding.textViewTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeUnitType(Unit.UnitType.TEMPERATURE);
            }
        });
        binding.textViewVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeUnitType(Unit.UnitType.VOLUME);
            }
        });

        // Can be called by onChangeUnitType changing the primary adapter,
        // OR by the user actually tapping an item
        binding.spinnerPrimary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Right after instance state is restored, this gets called with a null view
                if (view != null) {
                    String selectedPrimaryUnitString = ((TextView) view).getText().toString();
                    onChangePrimaryUnit(Unit.fromString(getResources(), selectedPrimaryUnitString));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Can be called when onChangeUnitType changes the secondary adapter,
        // OR when the user actually taps an item
        binding.spinnerSecondary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Right after instance state is restored, this gets called with a null view
                if (view != null) {
                    String selectedSecondaryUnit = ((TextView) view).getText().toString();
                    onChangeSecondaryUnit(Unit.fromString(getResources(), selectedSecondaryUnit));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                onChangeInput(s.toString());
            }
        });

        // Remove focus from EditText when user clicks IME "Done" button

        binding.editTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocusAndHideKeyboard();
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(UNIT_TYPE, currentUnitType.ordinal());
        outState.putInt(PRIMARY_UNIT, currentPrimaryUnit.ordinal());
        outState.putInt(SECONDARY_UNIT, currentSecondaryUnit.ordinal());
    }

    private void onChangeUnitType(Unit.UnitType newUnitType) {
        if (currentUnitType != newUnitType) {
            tintOnly(newUnitType);
            currentUnitType = newUnitType;

            // Hide everything below the primary spinner (only matters after the first time)
            binding.spinnerSecondary.setVisibility(View.INVISIBLE);
            binding.editTextInput.setVisibility(View.INVISIBLE);
            binding.unitDisplayInput.setVisibility(View.INVISIBLE);
            binding.textViewOutput.setVisibility(View.INVISIBLE);
            // Also reset user input and hide keyboard
            binding.editTextInput.setText("");
            clearFocusAndHideKeyboard();

            // Changing a spinner's adapter counts as selecting whatever the new item is.
            // And those listeners call the respective onChange methods, setting the units to empty.
            binding.spinnerPrimary.setAdapter(new UnitArrayAdapter(getContext(), getListFromUnitType(newUnitType), 1));
            binding.spinnerSecondary.setAdapter(new UnitArrayAdapter(getContext(), getListFromUnitType(newUnitType), 2));

            // only matters if this is the first time a unit type was selected
            binding.spinnerPrimary.setVisibility(View.VISIBLE);
        }
    }

    private void onChangePrimaryUnit(Unit newUnitPrimary) {
        currentPrimaryUnit = newUnitPrimary;

        if (currentPrimaryUnit != Unit.EMPTY_UNIT) {
            if (getInputString().isEmpty()) {
                // Set the input hint (it won't be visible yet unless both units have been set)
                binding.editTextInput.setHint(getString(R.string.enter_units,
                        currentPrimaryUnit.toLowerCaseString(getResources())));
            }

            // Set the input unit abbreviation
            binding.unitDisplayInput.setText(newUnitPrimary.toAbbr(getResources()));

            if (currentSecondaryUnit == Unit.EMPTY_UNIT) {
                // The user hasn't selected a secondary unit yet, so make sure they can do that
                binding.spinnerSecondary.setVisibility(View.VISIBLE);
            } else {
                // We can try to convert
                if (isInputNumeric()) {
                    Quantity primaryQuantity = new Quantity(getResources(), currentPrimaryUnit, getInputString());
                    String result = primaryQuantity.convertTo(currentSecondaryUnit).toString();
                    binding.textViewOutput.setText(result);
                }
            }
        } else {
            // This method was only called because the primary adapter was just set
        }
    }

    private void onChangeSecondaryUnit(Unit newUnitSecondary) {
        currentSecondaryUnit = newUnitSecondary;

        if (currentSecondaryUnit != Unit.EMPTY_UNIT) {
            // Make sure input field is visible (if this is the first
            // time the secondary unit is being set, it won't be visible yet)
            binding.editTextInput.setVisibility(View.VISIBLE);

            if (isInputNumeric()) {
                Quantity primaryQuantity = new Quantity(getResources(), currentPrimaryUnit, getInputString());
                String result = primaryQuantity.convertTo(currentSecondaryUnit).toString();
                binding.textViewOutput.setText(result);
                binding.textViewOutput.setVisibility(View.VISIBLE);
            }
        } else {
            // This method was only called because the secondary adapter was just set
        }
    }

    private void onChangeInput(String newInput) {
        if (newInput.isEmpty()) {
            binding.unitDisplayInput.setVisibility(View.INVISIBLE);
            binding.textViewOutput.setVisibility(View.INVISIBLE);
            if (currentPrimaryUnit != Unit.EMPTY_UNIT) {
                // That check is needed because the first time the text is changed
                // is when it's cleared after the user selects a unit type.
                binding.editTextInput.setHint(getString(R.string.enter_units,
                        currentPrimaryUnit.toLowerCaseString(getResources())));
            }
        } else {
            binding.unitDisplayInput.setText(currentPrimaryUnit.toAbbr(getResources()));
            binding.unitDisplayInput.setVisibility(View.VISIBLE);
            // Clear hint
            binding.editTextInput.setHint("");

            if (isInputNumeric()) {
                Quantity primaryQuantity = new Quantity(getResources(), currentPrimaryUnit, getInputString());
                String result = primaryQuantity.convertTo(currentSecondaryUnit).toString();
                binding.textViewOutput.setText(result);
                binding.textViewOutput.setVisibility(View.VISIBLE);
            } else {
                binding.textViewOutput.setVisibility(View.INVISIBLE);
            }
        }
    }

    private ArrayList<String> getListFromUnitType(Unit.UnitType unitType) {
        switch (unitType) {
            case DISTANCE:
                return new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.units_distance)));
            case MASS:
                return new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.units_mass)));
            case TEMPERATURE:
                return new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.units_temperature)));
            case VOLUME:
                return new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.units_volume)));
        }
        return null;
    }

    private String getInputString() {
        return binding.editTextInput.getText().toString();
    }

    private boolean isInputNumeric() {
        switch (getInputString()) {
            case "":
                // We get here when the input is empty and the
                // user switches to another compatible unit.
                // Clearing input to make it empty gets caught by the TextWatcher instead.
            case "-":
            case ".":
            case "-.":
                return false;
            default:
                return true;
        }
    }

    private void clearFocusAndHideKeyboard() {
        // Remove focus
        binding.editTextInput.clearFocus();

        // Close keyboard (otherwise it just switches to a full keyboard)
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.editTextInput.getWindowToken(), 0);
    }

    /** Tints the icon of the specified unit type and untints the others */
    private void tintOnly(Unit.UnitType unitType) {
        switch (unitType) {
            case DISTANCE:
                tintTextView(binding.textViewDistance);
                untintTextViews(binding.textViewMass, binding.textViewTemperature, binding.textViewVolume);
                break;
            case MASS:
                tintTextView(binding.textViewMass);
                untintTextViews(binding.textViewDistance, binding.textViewTemperature, binding.textViewVolume);
                break;
            case TEMPERATURE:
                tintTextView(binding.textViewTemperature);
                untintTextViews(binding.textViewDistance, binding.textViewMass, binding.textViewVolume);
                break;
            case VOLUME:
                tintTextView(binding.textViewVolume);
                untintTextViews(binding.textViewDistance, binding.textViewMass, binding.textViewTemperature);
                break;
        }
    }

    /** Helper method for {@link #tintOnly(Unit.UnitType)} */
    private void tintTextView(TextView textView) {
        textView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        DrawableCompat.setTint(textView.getCompoundDrawables()[1].mutate(), getResources().getColor(R.color.colorPrimary));
    }

    /** Helper method for {@link #tintOnly(Unit.UnitType)} */
    private void untintTextViews(TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTextColor(Color.BLACK);
            DrawableCompat.setTint(textView.getCompoundDrawables()[1].mutate(), Color.BLACK);
        }
    }
}
