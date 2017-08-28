package org.cpsscifair.ssfcompanion;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChecklistFragment extends Fragment {

    private static final String DIALOG_FRAGMENT = "dialog_fragment";

    private ChecklistAdapter checklistAdapter;

    public ChecklistFragment() {
        // Required empty public constructor
    }

    public static ChecklistFragment newInstance() {
        return new ChecklistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checklist, container, false);

        setHasOptionsMenu(true);

        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.checklist_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        checklistAdapter = new ChecklistAdapter(getContext());

        recyclerView.setAdapter(checklistAdapter);

        final FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.checklist_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextDialogFragment.newInstance()
                        .show(getActivity().getSupportFragmentManager(), DIALOG_FRAGMENT);
            }
        });

        return v;
    }

    void addItem(String item) {
        checklistAdapter.addItem(item);
    }
}
