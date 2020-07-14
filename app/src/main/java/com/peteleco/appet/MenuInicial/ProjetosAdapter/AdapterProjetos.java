package com.peteleco.appet.MenuInicial.ProjetosAdapter;

import android.content.Context;
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
import com.peteleco.appet.bancoDados;

import java.util.List;

public class AdapterProjetos extends RecyclerView.Adapter<AdapterProjetos.MyViewHolder> {

    private List<ModeloProjetos> listProjeto;
    private ProgressBar progressProjeto;
    private Context context;

    public AdapterProjetos(List<ModeloProjetos> listProjeto, Context context) {
        this.listProjeto = listProjeto;
        this.context = context;
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
        progressProjeto = holder.progressBarProjeto;
        bancoDados dados = new bancoDados(context);
        dados.progressoProjeto(projetos.getNomeProjeto(), progressProjeto);
    }

    @Override
    public int getItemCount() {
        return listProjeto.size();
    }

}
