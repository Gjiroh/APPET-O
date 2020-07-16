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
    public CheckBox checkTarefa;

    private final static String TAG = "AdapterTarefas";

    public AdapterTarefas (List<Tarefa> listaTarefas) {

        this.tarefaList = listaTarefas;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nomeTarefa, descricao, prazo, responsavel;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkTarefa = itemView.findViewById(R.id.checkBoxTarefa);


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
        Log.i(TAG, "Resp: "+tarefa.getResponsavel());
        checkTarefa.setText(
                ""+tarefa.getNome()+"\nDescrição: "+tarefa.getDescricao()+
                        "\nResponsável: "+tarefa.getResponsavel()+
                        "\nPrazo: "+tarefa.getPrazo()
        );
    }


    @Override
    public int getItemCount() {
        return tarefaList.size();
    }

}
