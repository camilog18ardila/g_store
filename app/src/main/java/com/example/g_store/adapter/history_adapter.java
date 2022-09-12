package com.example.g_store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g_store.R;

import java.util.ArrayList;

public class history_adapter extends RecyclerView.Adapter<history_adapter.holder>{
    private Context context;
    private ArrayList  gamename,precio,fecha;

    public history_adapter(Context context, ArrayList gamename, ArrayList precio, ArrayList fecha) {
        this.context = context;
        this.gamename = gamename;
        this.precio = precio;
        this.fecha=fecha;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_history, parent,false);
        return new holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.namegame.setText(String.valueOf(gamename.get(position)));
        holder.precio.setText(String.valueOf(precio.get(position)));
        holder.fechacompra.setText(String.valueOf(fecha.get(position)));

    }

    @Override
    public int getItemCount() {
        return gamename.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView namegame, precio, fechacompra;
        public holder(@NonNull View itemView) {
            super(itemView);
            namegame=itemView.findViewById(R.id.tvnombrejuegohistoria);
            precio= itemView.findViewById(R.id.tvpreciojuegohistoria);
            fechacompra= itemView.findViewById(R.id.tvfechacompra);
        }
    }
}

