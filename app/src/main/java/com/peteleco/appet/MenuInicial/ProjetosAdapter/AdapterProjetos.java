package com.peteleco.appet.MenuInicial.ProjetosAdapter;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.R;

import java.util.List;

public class AdapterProjetos extends RecyclerView.Adapter<AdapterProjetos.MyViewHolder> {

    private List<ModeloProjetos> listProjeto;

    public AdapterProjetos(List<ModeloProjetos> listProjeto) {
        this.listProjeto = listProjeto;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nomeProjeto;
        ProgressBar progressBarProjeto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeProjeto = itemView.findViewById(R.id.textViewNomeProjeto);
            progressBarProjeto = itemView.findViewById(R.id.progressBarProjeto);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_projetos, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModeloProjetos projetos = listProjeto.get(position);

        holder.nomeProjeto.setText(projetos.getNomeProjeto());
//      holder.progressBarProjeto

    }

    @Override
    public int getItemCount() {
        return listProjeto.size();
    }

}
