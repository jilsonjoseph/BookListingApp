package com.example.jilson.booklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jilson on 31-08-2018.
 */

final class QueryUtils {

    // Tag for Log
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String REQUEST_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    //Function to fetch list of books
    static List<Book> fetchBooksList(String query){
        Log.v(LOG_TAG," fetchBooksList method in QueryUtils class");
        List<Book> books = new ArrayList<Book>();
        if(query.isEmpty()){
            Log.v(LOG_TAG,"query is empty ");
            return books;
        }

        // Creating the request url using the query
        String requestUrl = REQUEST_API_URL+query;

        // Creating URL object
        URL url = createURL(requestUrl);
        if(url == null){
            return null;
        }

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error in making HttpRequest", e);
        }

        books = extractBooks(jsonResponse);
        return books;

    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //checking if the request was successfull
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();

    }

    //Creates a URL
    private static URL createURL(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error in creating URL",e);
        }
        return url;
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Book> extractBooks(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if(jsonObject.has("items")){
                JSONArray items = jsonObject.getJSONArray("items");
                for(int i =0; !items.isNull(i); i++){
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    StringBuilder authors = new StringBuilder();

                    /** For some item author field is not available
                     * If author field is not available in JSON response Nil is set in place of author
                     */
                    if(volumeInfo.has("authors")) {
                        JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                        for(int j =0; !authorsArray.isNull(j);j++){
                            if(j>0)
                                authors.append(", ");
                            authors.append(authorsArray.getString(j));
                        }
                    }else{
                        authors.append("Nil");
                    }
                    Book book = new Book(title,authors.toString());
                    books.add(book);
                }
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }

}
