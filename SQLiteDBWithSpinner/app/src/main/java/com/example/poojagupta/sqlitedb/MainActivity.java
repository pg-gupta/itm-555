package com.example.poojagupta.sqlitedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author Pooja Gupta
 * Date: 04/02/2018
 * Lab: #8
 */
public class MainActivity extends AppCompatActivity {

    public static List<String> ids = new ArrayList<>();
    public static List<String> books = new ArrayList<>();
    public static List<String> authors = new ArrayList<>();
    public static List<String> ratings = new ArrayList<>();
    ListView booksList;
    Spinner spinner;

    public static void resetVariables() {
        ids = new ArrayList<>();
        books = new ArrayList<>();
        authors = new ArrayList<>();
        ratings = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("UserInfo", "\n*** Created by Pooja Gupta(A20413675) on April 2,2018 ***\n");

        SqlHelper db = null;
        try {
            db = new SqlHelper(this);

            // remove enteries if exist in the table
            db.deleteAllEnteries();

            // add books
            db.addBook(new Book("Professional Android 4 Application",
                    "Reto Meier", 4));
            db.addBook(new Book("Beginning Android 4 Application  Development",
                    "WeiMeng Lee", 3
            ));
            db.addBook(new Book("Programming Android", "Wallace Jackson", 2
            ));
            db.addBook(new Book("Hello, Android", "Wallace Jackson", 5));

            resetVariables();
            // get all books
            db.getAllBooks();

            // bind data to book adapter
            /*BookListAdapter bookAdapter = new BookListAdapter(this, ids, books, authors, ratings);
            booksList = (ListView) findViewById(R.id.booksList);
            booksList.setAdapter(bookAdapter);*/

            spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, books);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    SqlHelper finalDb = null;
                    try {
                        finalDb = new SqlHelper(getApplicationContext());
                        finalDb.getBook(books.get(position).toString());

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    } finally {
                        finalDb.close();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (IOException e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        } finally {
            db.close(); // close the database when done
        }

    }
}
