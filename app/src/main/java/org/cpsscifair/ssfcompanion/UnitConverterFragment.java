package org.cpsscifair.ssfcompanion;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.cpsscifair.ssfcompanion.databinding.FragmentUnitConverterBinding;

// We're not using Spinners because they don't support submenus.

// TODO: 10/6/2017 Enable split-screen mode.
// It's currently disabled because it causes problems if it's entered/exited while a PopupMenu is open.

public class UnitConverterFragment extends Fragment {

    // For saving instance state
    private static final String UNIT_STRING_PRIMARY = "unit_string_primary";
    private static final String UNIT_STRING_SECONDARY = "unit_string_secondary";
    private static final String HAS_SEEN_EDIT_TEXT_INPUT = "has_seen_edit_text_input";

    private FragmentUnitConverterBinding binding;

    private PopupMenu popupMenuPrimary, popupMenuSecondary;

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
            Unit recoveredUnitPrimary = Unit.fromString(getResources(), savedInstanceState.getString(UNIT_STRING_PRIMARY));
            Unit recoveredUnitSecondary = Unit.fromString(getResources(), savedInstanceState.getString(UNIT_STRING_SECONDARY));
            boolean recoveredHasSeenEditTextInput = savedInstanceState.getBoolean(HAS_SEEN_EDIT_TEXT_INPUT);

            if (! recoveredUnitSecondary.equals(Unit.EMPTY_UNIT)) {
                // We have a primary AND secondary unit
                onChangePrimaryUnit(recoveredUnitPrimary);
                onChangeSecondaryUnit(recoveredUnitSecondary);
            } else if (! recoveredUnitPrimary.equals(Unit.EMPTY_UNIT)) {
                // We have a primary unit, but no secondary unit
                onChangePrimaryUnit(recoveredUnitPrimary);
            } else {
                // We have no units; we don't have to set up anything new
            }

            if (recoveredHasSeenEditTextInput) {
                // Make input field visible if user has seen it before
                binding.editTextInput.setVisibility(View.VISIBLE);
            }
        }

        // Set up the primary PopupMenu

        popupMenuPrimary = new PopupMenu(getContext(), binding.buttonPrimary);
        popupMenuPrimary.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_primary_id_distance:
                    case R.id.menu_primary_id_mass:
                    case R.id.menu_primary_id_temperature:
                    case R.id.menu_primary_id_volume:
                        // These are just categories, so clicking them
                        // should just open their respective submenus
                        break;
                    default:
                        // If we're here, it means that the user has
                        // selected an actual item from the primary menu
                        onChangePrimaryUnit(Unit.fromString(getResources(), item.getTitle().toString()));
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
                onChangeInput(s.toString());
            }
        });

        // Remove focus from EditText when user clicks IME "Done" button

        binding.editTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Remove focus
                    v.clearFocus();

                    // Close keyboard (otherwise it'll switch to a full keyboard)
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // When turning these back into units, they will become EMPTY_UNIT. No more null units!
        outState.putString(UNIT_STRING_PRIMARY, getUnitPrimary().toUpperCaseString(getResources()));
        outState.putString(UNIT_STRING_SECONDARY, getUnitSecondary().toUpperCaseString(getResources()));
        outState.putBoolean(HAS_SEEN_EDIT_TEXT_INPUT, getHasSeenEditTextInput());
    }

    private void onChangePrimaryUnit(Unit newUnitPrimary) {
        Unit.UnitType oldPrimaryUnitType = getUnitPrimary().getType();

        binding.buttonPrimary.setText(newUnitPrimary.toUpperCaseString(getResources()));

        if (getInputString().isEmpty()) {
            // Set the input hint.
            binding.editTextInput.setHint(getString(R.string.enter_units,
                    getUnitPrimary().toLowerCaseString(getResources())));
        }

        // Set the input unit abbreviation
        binding.unitDisplayInput.setText(newUnitPrimary.toAbbr(getResources()));

        if (newUnitPrimary.getType().equals(oldPrimaryUnitType)) {
            // Same unit type, so we need to convert
            if (isInputNumeric()) {
                Quantity primaryQuantity = new Quantity(getResources(), getUnitPrimary(), getInputString());
                String result = primaryQuantity.convertTo(getUnitSecondary()).toString();
                binding.textViewOutput.setText(result);
            }
        } else {
            // Unit types are different
            onChangeSecondaryUnit(Unit.EMPTY_UNIT);
        }
    }

    private void onChangeSecondaryUnit(Unit newUnitSecondary) {
        if (! newUnitSecondary.equals(Unit.EMPTY_UNIT)) {
            // Set secondary button text
            binding.buttonSecondary.setText(newUnitSecondary.toUpperCaseString(getResources()));

            // Make sure input field is visible (if this is the first
            // time this unit is being set, it won't be visible)
            binding.editTextInput.setVisibility(View.VISIBLE);

            if (isInputNumeric()) {
                Quantity primaryQuantity = new Quantity(getResources(), getUnitPrimary(), getInputString());
                String result = primaryQuantity.convertTo(getUnitSecondary()).toString();
                binding.textViewOutput.setText(result);
                binding.textViewOutput.setVisibility(View.VISIBLE);
            }
        } else {
            // Reset the secondary button

            // Set the secondary Button's text to "Select second unit"
            binding.buttonSecondary.setText(R.string.select_second_unit);

            // Make secondary Button visible (this
            // only matters the first time around)
            binding.buttonSecondary.setVisibility(View.VISIBLE);

            // Hide output LinearLayout
            binding.textViewOutput.setVisibility(View.GONE);

            // Set secondary button menu
            setSecondaryButtonMenu(getUnitPrimary().getType());
        }
    }

    private void onChangeInput(String newInput) {

        // Nothing else in this method should be run without a primary unit
        if (getUnitPrimary() == Unit.EMPTY_UNIT) {
            return;
        }

        if (newInput.isEmpty()) {
            binding.unitDisplayInput.setVisibility(View.GONE);
            binding.textViewOutput.setVisibility(View.GONE);
            // Set hint
            binding.editTextInput.setHint(getString(R.string.enter_units,
                    getUnitPrimary().toLowerCaseString(getResources())));
        } else {
            binding.unitDisplayInput.setText(getUnitPrimary().toAbbr(getResources()));
            binding.unitDisplayInput.setVisibility(View.VISIBLE);
            // Clear hint
            binding.editTextInput.setHint("");

            if (isInputNumeric() && ! getUnitSecondary().equals(Unit.EMPTY_UNIT)) {
                Quantity primaryQuantity = new Quantity(getResources(), getUnitPrimary(), getInputString());
                String result = primaryQuantity.convertTo(getUnitSecondary()).toString();
                binding.textViewOutput.setText(result);
                binding.textViewOutput.setVisibility(View.VISIBLE);
            } else {
                binding.textViewOutput.setVisibility(View.GONE);
            }
        }
    }

    private void setSecondaryButtonMenu(Unit.UnitType secondaryMenuType) {
        popupMenuSecondary = new PopupMenu(getContext(), binding.buttonSecondary);

        switch (secondaryMenuType) {
            case DISTANCE:
                popupMenuSecondary.inflate(R.menu.popup_menu_distance);
                break;
            case MASS:
                popupMenuSecondary.inflate(R.menu.popup_menu_mass);
                break;
            case TEMPERATURE:
                popupMenuSecondary.inflate(R.menu.popup_menu_temperature);
                break;
            case VOLUME:
                popupMenuSecondary.inflate(R.menu.popup_menu_volume);
                break;
        }

        popupMenuSecondary.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onChangeSecondaryUnit(Unit.fromString(getResources(), item.getTitle().toString()));
                return true;
            }
        });
    }

    private Unit getUnitPrimary() {
        return Unit.fromString(getResources(), binding.buttonPrimary.getText().toString());
    }

    private Unit getUnitSecondary() {
        return Unit.fromString(getResources(), binding.buttonSecondary.getText().toString());
    }

    private String getInputString() {
        return binding.editTextInput.getText().toString();
    }

    private boolean getHasSeenEditTextInput() {
        return binding.editTextInput.getVisibility() == View.VISIBLE;
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
}
