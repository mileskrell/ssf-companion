package org.cpsscifair.ssfcompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UnitConverterFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_unit_converter, container, false);
    }
}
