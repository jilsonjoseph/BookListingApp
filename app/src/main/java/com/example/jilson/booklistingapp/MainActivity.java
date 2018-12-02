package com.example.jilson.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    private static final String LOG_TAG = MainActivity.class.getName();
    private String query = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getLoaderManager().initLoader(1, null, MainActivity.this);


        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        if(!connected()){
            TextView textView = (TextView)findViewById(R.id.empty_view);
            textView.setText(R.string.no_internet_msg);
        }else{
            final EditText editText = findViewById(R.id.edit_text);

            Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(connected()){
                        progressBar.setVisibility(View.VISIBLE);
                        Log.v(LOG_TAG,"Button clicked");
                        String text = editText.getText().toString();
                        if(!text.isEmpty()){
                            query =text;
                            getLoaderManager().restartLoader(1,null,MainActivity.this);
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getBaseContext(),"Enter a query then press search",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        TextView textView = (TextView)findViewById(R.id.empty_view);
                        textView.setText(R.string.no_internet_msg);
                        Toast.makeText(getBaseContext(),"Please connect to Internet",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


    }

    private boolean connected(){
        Context context = this;
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void updateUi(List<Book> books){

        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        // Find a reference to the {@link ListView} in the layout
        ListView booksListView = (ListView) findViewById(R.id.list);

        TextView textView = (TextView) findViewById(R.id.empty_view);
        textView.setText("No book Found!");
        booksListView.setEmptyView(textView);


        // Create a new {@link ArrayAdapter} of books
        final BookAdapter bookAdapter = new BookAdapter(this,books);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        booksListView.setAdapter(bookAdapter);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG,"In onCreateLoader method");
        return new BookLoaderClass(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        Log.v(LOG_TAG,"In onLoadFinished method");
        if(books == null) {
            Log.v(LOG_TAG," null is returned to onLoad finish method");
            return;
        }
        updateUi(books);

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

    }
}
