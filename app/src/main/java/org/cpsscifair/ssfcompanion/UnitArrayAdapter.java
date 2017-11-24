package org.cpsscifair.ssfcompanion;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * An ArrayAdapter subclass used by {@link UnitConverterFragment}
 */
class UnitArrayAdapter extends ArrayAdapter {

    private Context context;

    UnitArrayAdapter(@NonNull Context context, ArrayList<String> items, int adapterType) {
        super(context, android.R.layout.simple_spinner_dropdown_item, addTitleToList(context, items, adapterType));
        this.context = context;
    }

    private static List addTitleToList(Context context, ArrayList<String> items, int adapterType) {
        switch(adapterType) {
            case 1:
                items.add(0, context.getString(R.string.select_first_unit));
                break;
            case 2:
                items.add(0, context.getString(R.string.select_second_unit));
                break;
        }
        return items;
    }

    /** Controls how the non-dropdown view looks */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        if (position == 0) {
            view.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        return view;
    }

    /** Controls how the dropdown view looks */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView dropDownView = (TextView) super.getDropDownView(position, convertView, parent);
        if (position == 0) {
            dropDownView.setTextSize(22);
            dropDownView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            dropDownView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            dropDownView.setTextSize(14);
            dropDownView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            dropDownView.setTextColor(Color.BLACK);
        }
        return dropDownView;
    }

    /** Controls whether the dropdown view is clickable */
    @Override
    public boolean isEnabled(int position) {
        return position != 0;
    }
}
