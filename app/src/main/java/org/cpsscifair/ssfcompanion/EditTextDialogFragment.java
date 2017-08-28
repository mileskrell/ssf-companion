package org.cpsscifair.ssfcompanion;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class EditTextDialogFragment extends DialogFragment {

    public EditTextDialogFragment() {
        // Required empty public constructor
    }

    public static EditTextDialogFragment newInstance() {
        return new EditTextDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(R.layout.dialog_fragment);
        setCancelable(false);

        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) getDialog().findViewById(R.id.dialog_fragment_edit_text);

                ChecklistFragment checklistFragment = (ChecklistFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(NavDrawerActivity.CHECKLIST_FRAGMENT);

                checklistFragment.addItem(editText.getText().toString());
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
                EditText editText = (EditText) ((AlertDialog) dialog).findViewById(R.id.dialog_fragment_edit_text);
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                positiveButton.setEnabled(false);

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
}
