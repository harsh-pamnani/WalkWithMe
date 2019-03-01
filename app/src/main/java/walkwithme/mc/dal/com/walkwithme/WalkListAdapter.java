package walkwithme.mc.dal.com.walkwithme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

public class WalkListAdapter extends ArrayAdapter<Walk> {


    private Context mContext;
    private int mResource;


    //ViewHolder class is to remove lag when loading the list. Instead of all list items being
    // loaded at the same time, now only the amount to fill the screen are loaded.
    static class ViewHolder{
        TextView name;
        TextView datetime;
        TextView location;
        ImageView image;
    }


    //Constructor
    public WalkListAdapter(Context context, int resource, ArrayList<Walk> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //create image loader
        setupImageLoader();

        //get the Walk information
        String name = getItem(position).getEventName();
        String datetime = getItem(position).getEventDatetime();
        String location = getItem(position).getEventLocation();
        String imgURL = getItem(position).getEventImageURL();

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
            viewholder.image = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(viewholder);

        }else{

            viewholder = (ViewHolder) convertView.getTag();
        }

        //create instance of image loader
        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = mContext.getResources().getIdentifier("@drawable/list_default_photo", null, mContext.getPackageName());

        //if image does not exist or cant be loaded, use default image
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image
        imageLoader.displayImage(imgURL, viewholder.image, options);

        viewholder.name.setText(name);
        viewholder.datetime.setText(datetime);
        viewholder.location.setText(location);

        return convertView;
    }


    /*
    Universal Image Loader
    Author: https://www.stacktips.com/tutorials/android/universal-image-loader-library-in-android
     */
    private void setupImageLoader(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }

}
