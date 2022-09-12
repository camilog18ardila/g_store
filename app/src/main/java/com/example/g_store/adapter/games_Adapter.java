package com.example.g_store.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g_store.R;


public class games_Adapter extends RecyclerView.ViewHolder {
    public ImageView imagenlista;
    public TextView juegonombre,juegoprecio,juegocategoria;
    public View v;
    //TextView juegonombre,juegoprecio, juegocategoria;
    public games_Adapter(@NonNull View itemView) {
        super(itemView);
        imagenlista=itemView.findViewById(R.id.ivimagenjuegolista);
        juegonombre = itemView.findViewById(R.id.tvnombrejuegolista);
        juegoprecio = itemView.findViewById(R.id.tvpreciojuegolista);
        juegocategoria = itemView.findViewById(R.id.tvcategoriajuegolista);
        v=itemView;//esta linea
    }
}
