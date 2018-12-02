package com.example.jilson.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jilson on 01-09-2018.
 */

final class BookLoaderClass extends AsyncTaskLoader<List<Book>> {

    private String query;

    private static final String LOG_TAG = BookLoaderClass.class.getName();

    public BookLoaderClass(Context context, String query) {
        super(context);
        if(query == null)
            Log.v(LOG_TAG,"query is null in BookLoaderClass constructor");
        else
            Log.v(LOG_TAG,"query is "+query);
        this.query = query;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG," onStartLoading method ");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        Log.v(LOG_TAG," loadInBackground method");

        if(query != null && query.equals("") ) {
            Log.v(LOG_TAG," query is null or empty in loadInBackground method");
            return null;
        }
        List<Book> books = QueryUtils.fetchBooksList(query);
        return books;
    }
}
