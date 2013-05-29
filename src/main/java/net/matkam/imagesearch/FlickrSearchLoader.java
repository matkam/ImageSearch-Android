package net.matkam.imagesearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mathew Kamkar
 */
public class FlickrSearchLoader extends AsyncTaskLoader<List<FlickrPhoto>> {

    private static final String BASE_URL = "http://api.flickr.com/services/rest/?method=flickr.photos.search";
    private static final String API_KEY = "&api_key=insert_your_api_key_here";
    private static final String PER_PAGE = "&per_page=20";
    private static final String NOJSONCALLBACK = "&nojsoncallback=1";
    private static final String FORMAT = "&format=json";
    private static final String TEXT = "&text=";

    private String searchUrl;

    public FlickrSearchLoader(Context context, String searchString) {
        super(context);

        String encodedSearchString = "";
        if(searchString!=null)
            try {
                encodedSearchString = URLEncoder.encode(searchString, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        searchUrl = BASE_URL+API_KEY+PER_PAGE+NOJSONCALLBACK+FORMAT+TEXT+encodedSearchString;
    }

    @Override
    public List<FlickrPhoto> loadInBackground() {
        List<FlickrPhoto> flickrPhotos = new ArrayList<FlickrPhoto>();

        try {
            HttpClient httpClient = new DefaultHttpClient();
            JSONObject flickrJson = getFlickrJson(httpClient);

            JSONObject photosJson = flickrJson.optJSONObject("photos");
            JSONArray photoArray = photosJson!=null ? photosJson.getJSONArray("photo") : new JSONArray();

            for(int i = 0; i < photoArray.length(); i++) {
                JSONObject photoJson = (JSONObject) photoArray.get(i);

                FlickrPhoto flickrPhoto = new FlickrPhoto(photoJson);

                if(flickrPhoto.isValid()) {
                    flickrPhoto.download(httpClient, i);
                    flickrPhotos.add(flickrPhoto);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flickrPhotos;
    }

    private JSONObject getFlickrJson(HttpClient httpClient) throws IOException, JSONException {
        HttpGet flickrGet = new HttpGet(searchUrl);

        HttpEntity httpEntity = httpClient.execute(flickrGet).getEntity();
        InputStream inputStream = httpEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
            sb.append(line + "\n");

        return new JSONObject(sb.toString());
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
