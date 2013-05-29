package net.matkam.imagesearch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mathew Kamkar
 */
public class ImageSearchAdapter extends ArrayAdapter<FlickrPhoto> {
    private Context mContext;
    private int mResource;

    public ImageSearchAdapter(Context context) {
        super(context, R.layout.image_row);

        this.mContext = context;
        this.mResource = R.layout.image_row;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        FlickrPhoto flickrPhoto = getItem(i);

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mResource, null);
        }

        ImageView image = (ImageView) view.findViewById(R.id.image);
        image.setImageBitmap(flickrPhoto.getLocalImage());

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(flickrPhoto.getTitle());

        view.setOnClickListener(getOnClickViewImage(flickrPhoto.getLocalImageLocation()));

        return view;
    }

    private View.OnClickListener getOnClickViewImage(final String imageLocation) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageViewer = new Intent(mContext, ImageViewer.class);
                imageViewer.putExtra(ImageViewer.EXTRA_IMAGE_LOCATION, imageLocation);
                mContext.startActivity(imageViewer);
            }
        };
    }
}
