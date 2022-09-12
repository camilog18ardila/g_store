package com.example.g_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.MeasureUnit;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g_store.adapter.games_Adapter;
import com.example.g_store.model.games;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class MenuInicio extends AppCompatActivity {
    EditText buscador;
    Timer timer;
    FloatingActionButton btnhistorial;
    TextView tvname_menuinicio,tvVirtualM,txcorreo;
    String name, loggedEmail;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference mStrotagerefe;
    RecyclerView rvlistajuegos;
    FirebaseRecyclerOptions<games> options;
    FirebaseRecyclerAdapter<games, games_Adapter> adapter;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);

        tvname_menuinicio = findViewById(R.id.tvname_menuinicio);
        tvVirtualM = findViewById(R.id.tvVirtualM);
        buscador= findViewById(R.id.buscador);
        btnhistorial= findViewById(R.id.btnhistorial);
        rvlistajuegos= findViewById(R.id.rvlistajuegos);
        txcorreo= findViewById(R.id.txcorreo);
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        String dato=bundle.getString("Email");
        txcorreo.setText(dato);
        actulizarsaldoacto();

        db = FirebaseFirestore.getInstance();
        mStrotagerefe = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("games");

        rvlistajuegos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvlistajuegos.setHasFixedSize(true);
        if (mAuth.getCurrentUser() != null) {
            loggedEmail = mAuth.getCurrentUser().getEmail();
        } else {
            Toast.makeText(MenuInicio.this, "Error = No users Found !", Toast.LENGTH_SHORT).show();
        }

        db.collection("users")
                .document(loggedEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                name = documentSnapshot.getString("FirstName");
                                 Double virtualm = documentSnapshot.getDouble("virtualM");
                                 String vMoney = Double.toString(virtualm);
                                tvname_menuinicio.setText(name);
                                tvVirtualM.setText(vMoney);

                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MenuInicio.this, "Error = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        btnhistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuInicio.this, History.class);
                intent.putExtra("correohistorial",txcorreo.getText().toString());
                startActivity(intent);
            }
        });

        cargardatos("");
        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString()!=null){
                    cargardatos(editable.toString());
                }else{
                    cargardatos("");
                }

            }
        });

    }

    private void cargardatos(String data) {
        Query query= databaseReference.orderByChild("gamecategorie").startAt(data).endAt(data+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<games>().setQuery(query, games.class).build();
        adapter= new FirebaseRecyclerAdapter<games, games_Adapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull games_Adapter holder, int position, @NonNull games model) {
                holder.juegonombre.setText(model.getGamename());
                holder.juegoprecio.setText(String.valueOf(model.getGameprice()));
                holder.juegocategoria.setText(model.getGamecategorie());
                Picasso.get().load(model.getGameimageurl()).into(holder.imagenlista);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MenuInicio.this, gameinfo.class);
                        intent.putExtra("gamekey", getRef(position).getKey());
                        intent.putExtra("Email", txcorreo.getText().toString());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public games_Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_game, parent, false);
                return new games_Adapter(v);
            }
        };
        adapter.startListening();
        rvlistajuegos.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuopciones,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id==R.id.btncerrarapp){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }if (id==R.id.btnagregar){
            Intent intent = new Intent(getApplicationContext(),Gamesedit.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void actulizarsaldoacto(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Double saldoactulizar= 1500000.0;
                DocumentReference user = db.collection("users").document(loggedEmail);
                user.update("virtualM", saldoactulizar);
            }
        },10000);
    }

}
