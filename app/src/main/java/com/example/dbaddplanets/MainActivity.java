package com.example.dbaddplanets;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbhelp;
    private String planeta;
    protected int count=4;
    private MyDB md;
    private SQLiteDatabase db;
    private ListView llista;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPlanet();
                } catch (Exception e) {
                    Log.d("patata", "La entrada ja existeix");
                }
            }
        });

        this.dbhelp = new MyDatabaseHelper(this);
        this.db = dbhelp.getWritableDatabase();
        this.md = new MyDB(this);
        llista = (ListView) this.findViewById(R.id.list_view);
        try {
            md.createRecords("1", "Mercuri/ 3.7");
            md.createRecords("2", "Venus/ 8.87");
            md.createRecords("3", "Terra/ 9.8");
            md.createRecords("4", "Mart/ 3.71");
        } catch (Exception e) {
            Log.d("patata", "La entrada ja existeix");
        }
        count=md.getCount();

        updateListView();

        llista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                md.deletePlanet(String.valueOf(position));
                updateListView();
                return false;
            }
        });
    }

    public void createPlanet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add planet");
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.popup, null);
        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText2);
        builder.setView(textEntryView);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                planeta = input1.getText().toString();
                planeta+=("/ ");
                planeta+=(input2.getText().toString());
                md.createRecords(String.valueOf(++count), planeta);

                updateListView();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void updateListView(){
        Cursor c = md.selectRecords();
        startManagingCursor(c);
        String[] from = new String[]{"name"};
        int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to);
        llista.setAdapter(mAdapter);
    }

}
