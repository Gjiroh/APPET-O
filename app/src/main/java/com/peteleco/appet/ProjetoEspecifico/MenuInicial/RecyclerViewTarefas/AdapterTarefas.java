package com.peteleco.appet.ProjetoEspecifico.MenuInicial.RecyclerViewTarefas;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peteleco.appet.ProjetoEspecifico.MenuInicial.Tarefa;
import com.peteleco.appet.R;

import java.util.List;

public class AdapterTarefas extends RecyclerView.Adapter<AdapterTarefas.MyViewHolder> {

    private List<Tarefa> tarefaList;

    private final static String TAG = "AdapterTarefas";

    public AdapterTarefas (List<Tarefa> listaTarefas) {

        this.tarefaList = listaTarefas;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nomeTarefa, descricao, prazo, responsavel;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTarefa = itemView.findViewById(R.id.textViewNomeTarefa);
            descricao = itemView.findViewById(R.id.textViewDescrição);
            prazo = itemView.findViewById(R.id.textViewPrazo);
            responsavel = itemView.findViewById(R.id.textViewResponsavel);


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tarefas, parent, false);

        return new MyViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tarefa tarefa = this.tarefaList.get(position);

        holder.nomeTarefa.setText(tarefa.getNome());
        holder.descricao.setText("Descrição: "+tarefa.getDescricao());
        holder.prazo.setText("Prazo: "+tarefa.getPrazo());
        holder.responsavel.setText("Responsável: "+tarefa.getResponsavel());
    }


    @Override
    public int getItemCount() {
        return tarefaList.size();
    }

}
