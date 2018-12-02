package com.example.jilson.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jilson on 31-08-2018.
 */

final class BookAdapter extends ArrayAdapter<Book> {

    // Constructor of BookAdapter class
    public BookAdapter(@NonNull Context context, @NonNull List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        // Inflating a new view object
        if(convertView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.books_list_item,parent,false);
        }

        // Getting refference to book object for the current position in ListView
        Book currentBook = getItem(position);

        /* Getting reference to the bookTitle text view in the books list item
           and setting value to title of the current book using getTitle member function of Book class
        */
        TextView bookTitle = (TextView)listItemView.findViewById(R.id.book_title);
        bookTitle.setText(currentBook.getTitle());


        /* Getting reference to the bookAuthors text view in the books list item
           and setting value to authors of the current book using getAuthors member function of Book class
        */
        TextView bookAuthors = (TextView)listItemView.findViewById(R.id.book_authors);
        bookAuthors.setText(currentBook.getAuthors());

        return listItemView;
    }
}
