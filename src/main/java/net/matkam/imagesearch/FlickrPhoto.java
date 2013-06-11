package net.matkam.imagesearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mathew Kamkar
 */
public class FlickrPhoto {
    public static final String PROP_ID = "id";
    public static final String PROP_OWNER = "owner";
    public static final String PROP_SECRET = "secret";
    public static final String PROP_SERVER = "server";
    public static final String PROP_FARM = "farm";
    public static final String PROP_TITLE = "title";

    private String id; //Required
    private String owner;
    private String secret; //Required
    private String server; //Required
    private int farm = -1; //Required
    private String title;

    private String localImageLocation;

    public FlickrPhoto(JSONObject photoJson) {
        id = photoJson.optString(PROP_ID);
        owner = photoJson.optString(PROP_OWNER);
        secret = photoJson.optString(PROP_SECRET);
        server = photoJson.optString(PROP_SERVER);
        farm = photoJson.optInt(PROP_FARM, -1);
        title = photoJson.optString(PROP_TITLE);
    }

    public boolean isValid() {
        return  id!=null && !id.isEmpty() &&
                secret!=null && !secret.isEmpty() &&
                server!=null && !server.isEmpty() &&
                farm != -1;
    }

    // Takes int so that we don't waste space keeping old image searches
    public void download(HttpClient httpClient, int i) throws IOException {
        HttpGet getPhoto = new HttpGet(generateDownloadUrl());
        HttpEntity photoEntity = httpClient.execute(getPhoto).getEntity();
        InputStream in = photoEntity.getContent();

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/imagesearch");
        dir.mkdirs();
        File file = new File(dir, i+".jpg");

        FileOutputStream f = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = in.read(buffer)) > 0) {
            f.write(buffer, 0, len1);
        }
        f.close();

        localImageLocation = file.getAbsolutePath();
    }

    // Generates URL for a medium (240 px) sized image
    private String generateDownloadUrl() {
        return "http://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+"_m.jpg";
    }

    public Bitmap getLocalImage() {
        return BitmapFactory.decodeFile(localImageLocation);
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public int getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public String getLocalImageLocation() {
        return localImageLocation;
    }
}
