package org.cpsscifair.ssfcompanion;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChecklistFragment extends Fragment {

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
        final View v = inflater.inflate(R.layout.fragment_checklist, container, false);

        setHasOptionsMenu(true);

        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.checklist_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        checklistAdapter = new ChecklistAdapter(getContext(), getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(checklistAdapter);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // This method is called each time the item is moved to a new position,
                // so it's just doing a series of consecutive swaps.
                // E.g. Moving 1 to 4 really means: swap 1 and 2, 2 and 3, and then 3 and 4.
                swapItems(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                final int removedItemIndex = viewHolder.getAdapterPosition();
                final ChecklistItem removedItem = removeItem(viewHolder.getAdapterPosition());

                Snackbar.make(v, R.string.item_removed, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addItem(removedItemIndex, removedItem);
                            }
                        })
                        .show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        final FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.checklist_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextDialogFragment.newInstance()
                        .show(getActivity().getSupportFragmentManager(), EditTextDialogFragment.DIALOG_FRAGMENT);
            }
        });

        return v;
    }

    void addItem(String text) {
        checklistAdapter.addItem(text);
    }

    void addItem(int index, ChecklistItem checklistItem) {
        checklistAdapter.addItem(index, checklistItem);
    }

    void editItem(int index, String text) {
        checklistAdapter.editItem(index, text);
    }

    ChecklistItem removeItem(int index) {
        return checklistAdapter.removeItem(index);
    }

    void swapItems(int fromIndex, int toIndex) {
        checklistAdapter.swapItems(fromIndex, toIndex);
    }
}
