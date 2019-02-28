package walkwithme.mc.dal.com.walkwithme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WalkListAdapter extends ArrayAdapter<Walk> {


    private Context mContext;
    int mResource;

    public WalkListAdapter(Context context, int resource, ArrayList<Walk> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the Walk information
        String name = getItem(position).getEventName();
        String datetime = getItem(position).getEventDatetime();
        String location = getItem(position).getEventLocation();

        //Create a Walk object with the information
        Walk walk = new Walk(name,datetime,location);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textEventName = (TextView) convertView.findViewById(R.id.walkTitle);
        TextView textEventDateTime = (TextView) convertView.findViewById(R.id.walkDatetime);
        TextView textEventLocation = (TextView) convertView.findViewById(R.id.walkLocation);

        textEventName.setText(name);
        textEventDateTime.setText(datetime);
        textEventLocation.setText(location);

        return convertView;
    }
}
