package com.samsung.itschool.adapterexample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView lv; // не забудьте привязать переменную (findViewById)
    TextView name, author, duration, year, _id;
    SimpleCursorAdapter adapter; // объявлен в классе, чтобы был доступен вл всех методах
    Button add, sortByName, sortByAuthor, sortByYear, sortByDuration;
    DBHelperWithLoader helper;
    SQLiteDatabase songsDB;
    Cursor songs;
    Spinner categories;

    Boolean sortAuthor = false, sortName = false, sortYear = false, sortDuration = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.listSongs);

        name = findViewById(R.id.textName);
        author = findViewById(R.id.textAuthor);
        year = findViewById(R.id.textYear);
        duration = findViewById(R.id.textDuration);

        add = findViewById(R.id.btnAdd);
        sortByName = findViewById(R.id.btnName);
        sortByAuthor = findViewById(R.id.btnAuthor);
        sortByYear = findViewById(R.id.btnYear);
        sortByDuration = findViewById(R.id.btnDuration);

        helper = new DBHelperWithLoader(this);
        songsDB = helper.getWritableDatabase();

        songs = songsDB.rawQuery("SELECT * FROM databasa", null);

        String[] database_fields = songs.getColumnNames();
        for (int i = 0; i < database_fields.length; i++) {
            System.out.println(database_fields[i]);
        }


        // int[] - ссылки на id элементов разметки playlist_item
        int[] views = { R.id._id, R.id.author_, R.id.name_, R.id.year_, R.id.duration_};

        // этот адаптер отображает в ListView перечень полей (столбцов)
        adapter = new SimpleCursorAdapter(this, R.layout.song_item, songs, database_fields, views, 0 );
        lv.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                System.out.println(_id);

                values.put("название", name.getText().toString());
                values.put("автор", author.getText().toString());
                values.put("год", year.getText().toString());
                values.put("длительность", duration.getText().toString());

                songsDB.insert("databasa", null, values);
                songs = songsDB.rawQuery("SELECT * FROM databasa", null);

                adapter.swapCursor(songs);
                adapter.notifyDataSetChanged();
            }
        });

        sortByAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortAuthor) {
                    songs = songsDB.rawQuery("SELECT * FROM databasa ORDER BY автор DESC", null);
                } else {
                    songs = songsDB.rawQuery("SELECT * FROM databasa ORDER BY автор ASC", null);
                }
                adapter.swapCursor(songs);
                sortAuthor = !sortAuthor;
            }
        });

        sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortName) {
                    songs = songsDB.rawQuery("SELECT * FROM databasa ORDER BY название DESC", null);
                } else {
                    songs = songsDB.rawQuery("SELECT * FROM databasa ORDER BY название ASC", null);
                }
                adapter.swapCursor(songs);
                sortName = !sortName;
            }
        });

        sortByYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortYear) {
                    songs = songsDB.rawQuery("SELECT * FROM databasa ORDER BY год DESC", null);
                } else {
                    songs = songsDB.rawQuery("SELECT * FROM databasa ORDER BY год ASC", null);
                }
                adapter.swapCursor(songs);
                sortYear = !sortYear;
            }
        });

        sortByDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortDuration) {
                    songs = songsDB.rawQuery("SELECT * FROM databasa ORDER BY длительность DESC", null);
                } else {
                    songs = songsDB.rawQuery("SELECT * FROM databasa ORDER BY длительность ASC", null);
                }
                adapter.swapCursor(songs);
                sortDuration = !sortDuration;
            }
        });
    }
}
