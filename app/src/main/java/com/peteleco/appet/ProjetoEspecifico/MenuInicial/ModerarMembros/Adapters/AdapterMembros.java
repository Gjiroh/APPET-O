package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

import java.util.ArrayList;
import java.util.List;

public class AdapterMembros extends RecyclerView.Adapter<AdapterMembros.MyViewHolder> {
    private List<String> listaMembros;
    public List<String> listaMembrosSelec;
    private final static String TAG = "AdapterMembros";


    public AdapterMembros(List<String> listaMembros) {
        this.listaMembros = listaMembros;
        listaMembrosSelec = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nomeMembro.setText(listaMembros.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    listaMembrosSelec.add(holder.nomeMembro.getText().toString());
                    Log.i(TAG,"Membro add a lista: "+holder.nomeMembro.getText().toString());
                } else {
                    if (listaMembrosSelec.size() == 1) {
                        listaMembrosSelec.clear();
                    } else {
                        listaMembrosSelec.remove(position);
                        Log.i(TAG,"Membro removido da lista: "+holder.nomeMembro.getText().toString());
                    }
                    Log.i(TAG, "Lista atualizada: "+ listaMembrosSelec);
                }

            }
        });
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
