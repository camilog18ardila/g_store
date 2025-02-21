package com.example.g_store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Gamesedit extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE = 101;
    EditText txtnombregamesubir,txtnombrecategoriasubir,txtdescripciongamesubir,txtpreciogamesubir;
    ImageView imgensubir;
    TextView  txtprogress;
    ProgressBar progresbar;
    Button btnsubir;
    Uri imagenuri;
    boolean isImagenadd=false;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamesedit);
        imgensubir = findViewById(R.id.ImagenAdd);
        txtnombregamesubir = findViewById(R.id.nombrejuego);
        txtnombrecategoriasubir = findViewById(R.id.categoriajuego);
        txtdescripciongamesubir = findViewById(R.id.descripcionjuego);
        txtpreciogamesubir = findViewById(R.id.preciojuego);
        txtprogress = findViewById(R.id.textViewProgress);
        progresbar= findViewById(R.id.progressBar);
        btnsubir= findViewById(R.id.btnsubir);

        txtprogress.setVisibility(View.GONE);
        progresbar.setVisibility(View.GONE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("games");
        storageReference= FirebaseStorage.getInstance().getReference().child("gamesImage");


        imgensubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        btnsubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nombrejuego= txtnombregamesubir.getText().toString();
                final String categoriajuego= txtnombrecategoriasubir.getText().toString();
                final Double preciojuego= Double.parseDouble(txtpreciogamesubir.getText().toString());
                final String descripcionjuego= txtdescripciongamesubir.getText().toString();

                if (isImagenadd!=false && nombrejuego!=null && categoriajuego!=null && preciojuego!=null && descripcionjuego!=null){
                    subirdatos(nombrejuego,categoriajuego,preciojuego,descripcionjuego);
                }
            }
        });

    }

    private void subirdatos(final String nombrejuego, final String categoriajuego, final Double preciojuego, final String descripcionjuego) {
        txtprogress.setVisibility(View.VISIBLE);
        progresbar.setVisibility(View.VISIBLE);

        final String key = databaseReference.push().getKey();
        storageReference.child(key+".jpg").putFile(imagenuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("gamename", nombrejuego);
                        hashMap.put("gamecategorie", categoriajuego);
                        hashMap.put("gameprice", preciojuego);
                        hashMap.put("gamedescription", descripcionjuego);
                        hashMap.put("gameimageurl", uri.toString());

                        databaseReference.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {
<<<<<<< HEAD
                                Toast.makeText(Gamesedit.this, "Se subio correctamente", Toast.LENGTH_SHORT).show();
=======
                                Toast.makeText(Gamesedit.this, "SE SUBIÓ CORRECTAMENTE", Toast.LENGTH_SHORT).show();
>>>>>>> baf7bf3 (PRUEBA)
                            }
                        });

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress= (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progresbar.setProgress((int) progress);
                txtprogress.setText(progress+" %");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null){
            imagenuri=data.getData();
            isImagenadd =   true;
            imgensubir.setImageURI(imagenuri);
        }
    }

    /**public void agregargame(View vie) {
        games game = new games();
        game.setGamename(txtnombregame.getText().toString());
        game.setCategorie(txtnombregame.getText().toString());
        game.setDescription(txtdescripciongame.getText().toString());
        game.setPrice(Double.parseDouble(txtpreciogame.getText().toString()));
    


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("games");

        reference.push().setValue(game);
    }**/
}