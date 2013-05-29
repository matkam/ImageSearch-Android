package net.matkam.imagesearch;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mathew Kamkar
 */
public class ImageSearchFragment extends ListFragment
        implements TextView.OnEditorActionListener, LoaderManager.LoaderCallbacks<List<FlickrPhoto>> {

//    private static final String SEARCH_STRING = "search_string";

    ImageSearchAdapter mAdapter;
    String mSearchString;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        setHasOptionsMenu(true);

        mAdapter = new ImageSearchAdapter(getActivity());
        setListAdapter(mAdapter);

        setListShown(false);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View imageSearch =  inflater.inflate(R.layout.image_search, container, false);

        EditText editText = (EditText) imageSearch.findViewById(R.id.editText);
        editText.setOnEditorActionListener(this);

        return imageSearch;
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
//        String searchString = bundle==null ? null : bundle.getString(SEARCH_STRING);

        return new FlickrSearchLoader(getActivity(), mSearchString);
    }

    @Override
    public void onLoadFinished(Loader<List<FlickrPhoto>> listLoader, List<FlickrPhoto> flickrPhotos) {
        mAdapter.clear();
        mAdapter.addAll(flickrPhotos);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<FlickrPhoto>> cursorLoader) {
        mAdapter.clear();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        final String searchString = textView.getText().toString();

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //Start search
            mSearchString = !TextUtils.isEmpty(searchString) ? searchString : null;
            getLoaderManager().restartLoader(0, null, this);
            setListShown(false);
        }

        return false;
    }
}
