package com.example.g_store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class gameinfo extends AppCompatActivity {
    ImageView imageninfogame;
    TextView nombreinfogame,categoriainfogame,precioinfogame,descripcioninfogame;
    Button botoncomprar;
    EditText txtemial;
    String correo;

    DatabaseReference databaseReference, dataref, datahistorial;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameinfo);

        imageninfogame = findViewById(R.id.imageViewgame_single);
        nombreinfogame = findViewById(R.id.tvnombregame_single);
        categoriainfogame = findViewById(R.id.tvcatgoriagame_single);
        precioinfogame = findViewById(R.id.tvgameprice_single);
        descripcioninfogame = findViewById(R.id.tvdescripciongame_single);
        txtemial = findViewById(R.id.correohistorial);
        botoncomprar = findViewById(R.id.btnBuy);
        db = FirebaseFirestore.getInstance();

        datahistorial= FirebaseDatabase.getInstance().getReference().child("history");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("games");
        String gamekey = getIntent().getStringExtra("gamekey");
        String Email= getIntent().getStringExtra("Email");
        txtemial.setText(Email);
        dataref = FirebaseDatabase.getInstance().getReference().child("games").child(gamekey);
        databaseReference.child(gamekey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String gamecategorie = snapshot.child("gamecategorie").getValue().toString();
                    String gamedescription = snapshot.child("gamedescription").getValue().toString();
                    String gameimageurl = snapshot.child("gameimageurl").getValue().toString();
                    String gamename = snapshot.child("gamename").getValue().toString();
                    String gameprice = snapshot.child("gameprice").getValue().toString();
                    Picasso.get().load(gameimageurl).into(imageninfogame);
                    nombreinfogame.setText(gamename);
                    precioinfogame.setText(gameprice);
                    descripcioninfogame.setText(gamedescription);
                    categoriainfogame.setText(gamecategorie);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void confirmarcomprar2(View v){
        String codigo = txtemial.getText().toString() + nombreinfogame.getText().toString().trim();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"history", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();
        String fechacompra= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String cod = codigo;
        String email = txtemial.getText().toString();
        String namegame = nombreinfogame.getText().toString();
        String pre = precioinfogame.getText().toString();
        ContentValues registrohistory = new ContentValues();
        registrohistory.put("codigo", cod);
        registrohistory.put("gamename",namegame);
        registrohistory.put("precio",pre);
        registrohistory.put("email", email);
        registrohistory.put("fechacompra",fechacompra);

        Cursor fila = base.rawQuery("select * from history where codigo='" + cod+"'", null);
        if (fila.moveToFirst()){
            Toast.makeText(this, getString(R.string.juego_comprado),
                    Toast.LENGTH_SHORT).show();
        } else {

            //actulizarsaldo(pre);
            Task<DocumentSnapshot> user = db.collection("users")
                    .document(txtemial.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    Double saldo = documentSnapshot.getDouble("virtualM");
                                    if (saldo>0){
                                        Double vmoney = Double.parseDouble(pre);
                                        if (saldo>vmoney){
                                            base.insert("history", null, registrohistory);
                                            base.close();
                                            Toast.makeText(gameinfo.this, getString(R.string.cargar_datos), Toast.LENGTH_SHORT).show();
                                            Double saldofinal = saldo - vmoney;
                                            actulizarsaldo(saldofinal);
                                        }else{
                                            Toast.makeText(gameinfo.this, getString(R.string.saldo_insuficiente), Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(gameinfo.this, getString(R.string.NO_SALDO), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
        }

    }

    public void confirmarcomprar(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = getLayoutInflater().from(this).inflate(
                R.layout.alertdialogo,(ConstraintLayout)findViewById(R.id.dialogo)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.titulo)).setText(getString(R.string.JUEGO_PARGAR));
        ((TextView) view.findViewById(R.id.mesanje)).setText(getString(R.string.JUEGO_DESCUENTO));
        ((TextView) view.findViewById(R.id.nombredeljuegoinfo)).setText(nombreinfogame.getText().toString());
        ((TextView) view.findViewById(R.id.preciodeljuego)).setText(precioinfogame.getText().toString());
        ((Button) view.findViewById(R.id.buttonnyes)).setText("COMPRAR");
        ((Button) view.findViewById(R.id.buttonaction)).setText("No");
        ((ImageView) view.findViewById(R.id.icono)).setImageResource(R.drawable.ic_done);

        final AlertDialog alertDialog= builder.create();
        view.findViewById(R.id.buttonnyes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                confirmarcomprar2(view);
            }
        });
        view.findViewById(R.id.buttonaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtgamecategoria, txtgamenombre,txtgameprecio, txtdescripcion;
        ImageView imageninfogame;
        View v;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgamenombre = itemView.findViewById(R.id.tvnombregame_info);
            txtgamecategoria = itemView.findViewById(R.id.tvcatgoriagame_info);
            txtgameprecio = itemView.findViewById(R.id.tvgameprice_info);
            txtdescripcion = itemView.findViewById(R.id.tvdescripciongame_info);
            imageninfogame = itemView.findViewById(R.id.imageViewinfogame);
            v=itemView;
        }
    }
    public void actulizarsaldo(Double saldofinal){
        DocumentReference user = db.collection("users").document(txtemial.getText().toString());
        user.update("virtualM", saldofinal);
        Intent intent = new Intent(gameinfo.this, MenuInicio.class);
        startActivity(intent);
    }
    private boolean validarcompra(){
        boolean validar=true;
        return validar;
    }


}