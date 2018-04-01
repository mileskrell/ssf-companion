package org.cpsscifair.ssfcompanion;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class EditTextDialogFragment extends DialogFragment {

    public static final String DIALOG_FRAGMENT = "dialog_fragment";
    public static final String DIALOG_CURRENT_TEXT = "dialog_current_text";

    public static final String ITEM_INDEX = "item_index";
    public static final String OLD_ITEM_TEXT = "old_item_text";

    private int itemIndex;
    private String oldItemText;


    public EditTextDialogFragment() {
        // Required empty public constructor
    }

    public static EditTextDialogFragment newInstance() {
        return new EditTextDialogFragment();
    }

    public static EditTextDialogFragment newInstance(int itemIndex, String oldItemText) {

        EditTextDialogFragment editTextDialogFragment = new EditTextDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ITEM_INDEX, itemIndex);
        args.putString(OLD_ITEM_TEXT, oldItemText);
        editTextDialogFragment.setArguments(args);
        return editTextDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemIndex = getArguments().getInt(ITEM_INDEX);
            oldItemText = getArguments().getString(OLD_ITEM_TEXT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(R.layout.dialog_fragment_edit_text);
        setCancelable(false);

        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = getDialog().findViewById(R.id.dialog_fragment_edit_text);

                ChecklistFragment checklistFragment = (ChecklistFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(NavDrawerActivity.CHECKLIST_FRAGMENT);

                if (oldItemText == null) {
                    checklistFragment.addItem(editText.getText().toString());
                } else {
                    checklistFragment.editItem(itemIndex, editText.getText().toString());
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditTextDialogFragment.this.getDialog().cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                EditText editText = ((AlertDialog) dialog).findViewById(R.id.dialog_fragment_edit_text);
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                if (savedInstanceState != null) {
                    editText.setText(savedInstanceState.getString(DIALOG_CURRENT_TEXT));
                } else {
                    editText.setText(oldItemText);
                    // If we're adding an item and oldItemText is null, it just clears it
                }

                if (editText.getText().toString().isEmpty()) {
                    positiveButton.setEnabled(false);
                } else {
                    // If we're editing an item, move the cursor to the end of the EditText
                    editText.setSelection(editText.length());
                }

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                        } else {
                            positiveButton.setEnabled(true);
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText editText = getDialog().findViewById(R.id.dialog_fragment_edit_text);
        outState.putString(DIALOG_CURRENT_TEXT, editText.getText().toString());
    }
}
