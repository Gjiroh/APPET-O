package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peteleco.appet.R;

import java.util.List;

public class AdapterMembros extends RecyclerView.Adapter<AdapterMembros.MyViewHolder> {
    private List<String> listaMembros;
    private final static String TAG = "AdapterMembros";

    public AdapterMembros(List<String> listaMembros) {
        this.listaMembros = listaMembros;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_alterar_membros, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nomeMembro.setText(listaMembros.get(position));
    }

    @Override
    public int getItemCount() {
        return listaMembros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nomeMembro;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeMembro = itemView.findViewById(R.id.textViewNomeMembro);
            checkBox = itemView.findViewById(R.id.checkBoxMembro);

        }
    }


}
