package com.example.g_store.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g_store.R;


public class gameinfo_adapter extends RecyclerView.ViewHolder{
    TextView txtgamecategoria, txtgamenombre,txtgameprecio, txtdescripcion;
    ImageView imageninfogame;
    View v;//esta linea

    public gameinfo_adapter(@NonNull View itemView) {
        super(itemView);
        txtgamenombre = itemView.findViewById(R.id.tvnombregame_info);
        txtgamecategoria = itemView.findViewById(R.id.tvcatgoriagame_info);
        txtgameprecio = itemView.findViewById(R.id.tvgameprice_info);
        txtdescripcion = itemView.findViewById(R.id.tvdescripciongame_info);
        imageninfogame = itemView.findViewById(R.id.imageViewinfogame);
        v=itemView;//esta linea
    }
}
