package org.cpsscifair.ssfcompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

public class AdviseAStudentFragment extends Fragment {

    public AdviseAStudentFragment() {
        // Required empty public constructor
    }

    public static AdviseAStudentFragment newInstance() {
        return new AdviseAStudentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_advise_a_student, container, false);

        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        Button getHelpButton = (Button) v.findViewById(R.id.get_help_button);

        getHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getHelpIntent = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("mailto:" + getString(R.string.advise_a_student_email_address)));

                getHelpIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.advise_a_student_email_subject));

                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_button_beginning:
                        getHelpIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.beginning_condition));
                        break;
                    case R.id.radio_button_intermediate:
                        getHelpIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.intermediate_condition));
                        break;
                    case R.id.radio_button_advanced:
                        getHelpIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.advanced_condition));
                        break;
                    default:
                        // Show Snackbar and return, skipping the rest of the OnClickListener
                        Snackbar.make(v, R.string.please_select_an_option_first, Snackbar.LENGTH_SHORT).show();
                        return;
                }

                ((NavDrawerActivity) getActivity()).safelyInvokeIntent(getHelpIntent);
            }
        });

        return v;
    }
}
