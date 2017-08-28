package org.cpsscifair.ssfcompanion;

import android.content.Context;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ViewHolder> {

    private static final String CHECKLIST_ITEMS = "checklist_items";

    private ArrayList<String> checklistItems;
    private Context context;
    private FragmentManager supportFragmentManager;

    ChecklistAdapter(Context context, FragmentManager supportFragmentManager) {

        String[] defaultItemsArray = context.getResources().getStringArray(R.array.default_checklist_items);
        HashSet<String> defaultItemsHashSet = new HashSet<>(Arrays.asList(defaultItemsArray));

        HashSet<String> checklistItemsHashSet
                = new HashSet<>(PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(CHECKLIST_ITEMS, defaultItemsHashSet));

        checklistItems = new ArrayList<>(checklistItemsHashSet);

        sortItems();
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checklist_item_check_box);
            textView = (TextView) itemView.findViewById(R.id.checklist_item_text_view);
        }
    }

    @Override
    public ChecklistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checklist_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChecklistAdapter.ViewHolder holder, int position) {
        // First, we figure out whether this item should be checked
        String item = checklistItems.get(position);
        int firstCommaPosition = getFirstCommaPosition(item);

        if (item.charAt(firstCommaPosition + 2) == '1') {
            holder.checkBox.setChecked(true);
            // Add strike-through to TextView
            // See https://stackoverflow.com/a/9786629
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // Now, we add the text for the item
        item = item.substring(firstCommaPosition + 5);
        holder.textView.setText(item);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Add/remove strike-through
                // See https://stackoverflow.com/a/9786629
                if (isChecked) {
                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                // Save new checkbox state

                String changedItem = checklistItems.get(holder.getAdapterPosition());

                int firstCommaPosition = getFirstCommaPosition(changedItem);

                // Includes everything before the boolean
                String finalItem = changedItem.substring(0, firstCommaPosition + 2);

                if (isChecked) {
                    finalItem += "1" + changedItem.substring(finalItem.length() + 1);
                } else {
                    finalItem += "0" + changedItem.substring(finalItem.length() + 1);
                }

                checklistItems.set(holder.getAdapterPosition(), finalItem);

                saveItems();
            }
        });

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldItem = checklistItems.get(holder.getAdapterPosition());

                EditTextDialogFragment.newInstance(oldItem)
                        .show(supportFragmentManager, EditTextDialogFragment.DIALOG_FRAGMENT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checklistItems.size();
    }

    private static int getFirstCommaPosition(String itemContainingComma) {
        int commaPosition = 0;
        // Increment until we get to the first comma
        while (itemContainingComma.charAt(commaPosition) != ',') {
            commaPosition ++;
        }
        return commaPosition;
    }

    static int getSecondCommaPosition(String itemContainingCommas) {
        int commaPosition = getFirstCommaPosition(itemContainingCommas) + 1;
        // Increment until we get to the second comma
        while (itemContainingCommas.charAt(commaPosition) != ',') {
            commaPosition ++;
        }
        return commaPosition;
    }

    private void sortItems() {
        int x = 1;
        ArrayList<String> sortedItems = new ArrayList<>();

        for (int i = 0; i < checklistItems.size(); i ++) {
            for (String item : checklistItems) {
                if (item.substring(0, getFirstCommaPosition(item)).equals(String.valueOf(x))) {
                    sortedItems.add(item);
                    break;
                }
            }
            x ++;
        }

        checklistItems = sortedItems;
    }

    private void saveItems() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(CHECKLIST_ITEMS, new HashSet<>(checklistItems))
                .apply();
    }

    void addItem(String itemText) {
        checklistItems.add((checklistItems.size() + 1) + ", 0, " + itemText);
        notifyItemInserted(checklistItems.size() + 1);

        saveItems();
    }

    void editItem(String oldItem, String newItemText) {

        String oldItemMinusText = oldItem.substring(0, getSecondCommaPosition(oldItem) + 2);

        String newItem = oldItemMinusText + newItemText;

        checklistItems.set(checklistItems.indexOf(oldItem), newItem);
        notifyItemChanged(checklistItems.indexOf(newItem));

        saveItems();
    }
}
