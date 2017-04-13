package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prime.perspective.khmeradmin.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        Date date = stringToDate(currentItem.getDate());

        final TextView DateTextView = (TextView) row.findViewById(R.id.dateTextView);
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
        DateTextView.setText(dateFormat.format(date));

        final TextView TimeTextView = (TextView) row.findViewById(R.id.timeTextView);
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        TimeTextView.setText(timeFormat.format(date));

        return row;
    }

    private Date stringToDate(String stringDate){
        //ex. 2017-02-28T01:50:52.363Z

        // Format for input
        String dateTime = stringDate.replace("T", " ").replace("Z", "");

        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date date = null;
        try {
            date = dateParser.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
