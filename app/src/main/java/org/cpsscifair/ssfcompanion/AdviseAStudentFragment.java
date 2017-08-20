package org.cpsscifair.ssfcompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return inflater.inflate(R.layout.fragment_advise_a_student, container, false);
    }
}
