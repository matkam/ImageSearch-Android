package net.matkam.imagesearch;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Mathew Kamkar
 */
public class ImageViewer extends Activity {
    public static final String EXTRA_IMAGE_LOCATION = "extra_image_location";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.image);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        if(getIntent().hasExtra(EXTRA_IMAGE_LOCATION))
            image.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra(EXTRA_IMAGE_LOCATION)));
    }
}