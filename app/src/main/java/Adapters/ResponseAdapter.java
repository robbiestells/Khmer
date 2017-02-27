package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.robbi.khmeradmin.R;

import models.Response;

/**
 * Created by robbi on 2/14/2017.
 */

public class ResponseAdapter extends ArrayAdapter<Response> {

    public ResponseAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Response currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.response_row, parent, false);
        }

        row.setTag(currentItem);

        final TextView UserIdTextView = (TextView) row.findViewById(R.id.UserIdText);
        UserIdTextView.setText(currentItem.getUserId());

        final TextView DateTextView = (TextView) row.findViewById(R.id.dateTextView);
        DateTextView.setText(currentItem.getDate().toString());

        return row;
    }
}
