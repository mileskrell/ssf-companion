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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ViewHolder> {

    private static final String CHECKLIST_ITEMS = "checklist_items";

    private ArrayList<ChecklistItem> checklistItems;
    private Context context;
    private FragmentManager supportFragmentManager;
    private Gson gson;

    ChecklistAdapter(Context context, FragmentManager supportFragmentManager) {

        gson = new Gson();
        Type arrayListType = new TypeToken<ArrayList<ChecklistItem>>(){}.getType();

        if (PreferenceManager.getDefaultSharedPreferences(context).contains(CHECKLIST_ITEMS)) {
            String checklistItemsJson = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(CHECKLIST_ITEMS, "");
            checklistItems = gson.fromJson(checklistItemsJson, arrayListType);
        } else {
            String defaultChecklistItemsJson = context.getResources()
                    .getString(R.string.default_checklist_items);
            checklistItems = gson.fromJson(defaultChecklistItemsJson, arrayListType);
        }

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
        final ChecklistItem item = checklistItems.get(position);

        if (item.isChecked()) {
            holder.checkBox.setChecked(true);
            // Add strike-through to TextView
            // See https://stackoverflow.com/a/9786629
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.checkBox.setChecked(false);
            // Remove strike-through from TextView
            // See https://stackoverflow.com/a/9786629
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Now, we add the text for the item
        holder.textView.setText(item.getText());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Add/remove strike-through
                // See https://stackoverflow.com/a/9786629
                if (isChecked) {
                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    item.setChecked(true);
                } else {
                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    item.setChecked(false);
                }

                saveItems();
            }
        });

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldItemText = checklistItems.get(holder.getAdapterPosition()).getText();

                EditTextDialogFragment.newInstance(holder.getAdapterPosition(), oldItemText)
                        .show(supportFragmentManager, EditTextDialogFragment.DIALOG_FRAGMENT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checklistItems.size();
    }

    private void saveItems() {
        String checklistItemsJson = gson.toJson(checklistItems);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(CHECKLIST_ITEMS, checklistItemsJson)
                .apply();
    }

    void addItem(String text) {
        checklistItems.add(new ChecklistItem(false, text));
        notifyItemInserted(checklistItems.size() - 1);

        saveItems();
    }

    void addItem(int index, ChecklistItem checklistItem) {
        checklistItems.add(index, checklistItem);
        notifyItemInserted(index);

        saveItems();
    }

    void editItem(int index, String text) {
        checklistItems.get(index).setText(text);
        notifyItemChanged(index);

        saveItems();
    }

    ChecklistItem removeItem(int index) {
        ChecklistItem removedItem = checklistItems.get(index);
        checklistItems.remove(index);
        notifyItemRemoved(index);

        saveItems();
        return removedItem;
    }

    void swapItems(int fromIndex, int toIndex) {
        Collections.swap(checklistItems, fromIndex, toIndex);
        notifyItemMoved(fromIndex, toIndex);

        saveItems();
    }
}
