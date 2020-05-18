package com.peteleco.appet.addNovoProjeto.RecyclerTeste.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;

import java.util.List;

public class AdapterTeste extends RecyclerView.Adapter<AdapterTeste.MyViewHolder> {

    private List<ModelTeste> listaColab;

    public AdapterTeste(List<ModelTeste> listaColab) {
        this.listaColab = listaColab;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox nomeColab;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeColab = itemView.findViewById(R.id.checkBoxNomeColab);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_teste, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelTeste colabs = listaColab.get(position);
        holder.nomeColab.setText(colabs.getNomeColab());

    }

    @Override
    public int getItemCount() {
        return listaColab.size();
    }


}
