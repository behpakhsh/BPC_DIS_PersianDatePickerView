package bpc.dis.persiandatepickerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private List<String> strings;
    private AdapterProperty adapterProperty;

    public SpinnerAdapter(Context context, int resourceId, int textViewId, List<String> strings, AdapterProperty adapterProperty) {
        super(context, resourceId, textViewId, strings);
        this.strings = strings;
        this.adapterProperty = adapterProperty;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return rowView(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return rowView(convertView, position);
    }

    @SuppressLint("InflateParams")
    private View rowView(View convertView, int position) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflate != null) {
                rowView = inflate.inflate(R.layout.dvp_persian_date_picker_adapter, null, false);
                viewHolder.txtTitle = rowView.findViewById(R.id.txt_title);
                rowView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        viewHolder.txtTitle.setText(strings.get(position));
        viewHolder.txtTitle.setTextColor(adapterProperty.getTextColor());
        viewHolder.txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, adapterProperty.getTextSize());
        viewHolder.txtTitle.setTypeface(adapterProperty.getTypeface(), adapterProperty.getTextStyle());
        return rowView;
    }

    private class ViewHolder {
        AppCompatTextView txtTitle;
    }

}