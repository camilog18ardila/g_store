package com.example.g_store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.example.g_store.adapter.history_adapter;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    RecyclerView rvhistorial;
    ArrayList<String> gamename,precio,fecha;
    AdminSQLiteOpenHelper db;
    history_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        rvhistorial = findViewById(R.id.rvhistorial);
        Bundle bundle = getIntent().getExtras();
        String correousuario=bundle.getString("correohistorial");
        db= new AdminSQLiteOpenHelper(this,"history", null, 1);
        gamename = new ArrayList<>();
        precio = new ArrayList<>();
        fecha = new ArrayList<>();
        adapter = new history_adapter(this,gamename,precio,fecha);
        rvhistorial.setAdapter(adapter);
        rvhistorial.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor = db.getData(correousuario);
        if (cursor.getCount()==0){
            Toast.makeText(this, "There's not ", Toast.LENGTH_SHORT).show();
            return;
        }else{
            while (cursor.moveToNext()){
                gamename.add(cursor.getString(1));
                precio.add(cursor.getString(2));
                fecha.add(cursor.getString(4));
        }

    }


    }
}