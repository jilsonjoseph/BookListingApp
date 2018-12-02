package com.example.jilson.booklistingapp;

/**
 * Created by Jilson on 31-08-2018.
 */

final class Book {

    // field to store the title of book
    private String title;

    // field that stores author names
    private String authors;

    Book(String title, String authors){
        this.title = title;
        this.authors = authors;
    }

    // getter method for book title
    public String getTitle() {
        return title;
    }

    // getter method for book authors
    public String getAuthors() {
        return authors;
    }


}
