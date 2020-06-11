package com.peteleco.appet.ProjetoEspecifico.MenuInicial.RecyclerViewTarefas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peteleco.appet.MenuInicial.ProjetosAdapter.AdapterProjetos;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.Tarefa;
import com.peteleco.appet.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterTarefas extends RecyclerView.Adapter<AdapterTarefas.MyViewHolder> {

    private List<Tarefa> tarefaList;

    public AdapterTarefas (List<Tarefa> tarefaList) {
        this.tarefaList = tarefaList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nomeTarefa, descricao, prazo, responsavel;
        CheckBox tarefaFeita;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeTarefa = itemView.findViewById(R.id.textNomeTarefa);
            descricao = itemView.findViewById(R.id.textDescricao);
            prazo = itemView.findViewById(R.id.textPrazo);
            responsavel = itemView.findViewById(R.id.textResponsavel);
            tarefaFeita = itemView.findViewById(R.id.checkBox);


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tarefas, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tarefa tarefa = tarefaList.get(position);
        holder.nomeTarefa.setText(tarefa.getNome());
        holder.descricao.setText("Descrição: "+ tarefa.getDescricao());
        holder.prazo.setText("Prazo: "+tarefa.getPrazo());
        holder.responsavel.setText("Responsável: "+tarefa.getResponsavel());

    }


    @Override
    public int getItemCount() {
        return tarefaList.size();
    }

}
