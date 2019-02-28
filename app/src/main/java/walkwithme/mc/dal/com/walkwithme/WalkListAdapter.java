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
    private int mResource;


    static class ViewHolder{
        TextView name;
        TextView datetime;
        TextView location;

    }




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

        //create a ViewHolder object
        ViewHolder viewholder;

        //ensure that not all values are loaded into the
        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            viewholder = new ViewHolder();

            viewholder.name = (TextView) convertView.findViewById(R.id.walkTitle);
            viewholder.datetime = (TextView) convertView.findViewById(R.id.walkDatetime);
            viewholder.location= (TextView) convertView.findViewById(R.id.walkLocation);

            convertView.setTag(viewholder);

        }else{

            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.name.setText(name);
        viewholder.datetime.setText(datetime);
        viewholder.location.setText(location);

        return convertView;
    }
}
