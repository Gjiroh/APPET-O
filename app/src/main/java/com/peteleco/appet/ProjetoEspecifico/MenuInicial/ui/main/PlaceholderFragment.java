package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.RecyclerItemClickListener;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.RecyclerViewTarefas.AdapterTarefas;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.Tarefa;
import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private String TAG="PlaceholderFragment";
    private String nomeProjeto, status;
    private bancoDados bancoDados;
    private List<String> nomeTarefa;
    private int index;
    private boolean verify1, verify2, verify3, verify4, estaTODO;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public Context context;

    Tarefa tarefa;
    List<Tarefa> listaTarefa = new ArrayList<>();

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tarefa = new Tarefa();

        context = getActivity().getApplicationContext();
        bancoDados = new bancoDados(getActivity().getApplicationContext());
        PageViewModel pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Activity",0);
        nomeProjeto = preferences.getString("nomeProjeto",null);

        verify1 = false;
        verify2 = false;
        verify3 = false;
        verify4 = false;

        nomeTarefa = new ArrayList<>();
        carregarListaTarefas("DONE");
        carregarListaTarefas("DOING");
        carregarListaTarefas("TO DO");
        carregarListaTarefas("IDEIA");
    }
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {

        // TODO: Criar função para quando for selecionado a checkBox de uma tarefa

        View root = inflater.inflate(R.layout.fragment_projeto_especifico, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewTarefas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());

        recyclerView.setLayoutManager(layoutManager);

        AdapterTarefas adapter = new AdapterTarefas(listaTarefa);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                this.getActivity().getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        // Evento de click
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity().getApplicationContext(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("O que deseja fazer com a tarefa?")
                                .setTitle("Alerta");
                        if (estaTODO) {
                            builder.setPositiveButton("MOVER PARA DOING", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO: movimentar tarefa do TODO para doing
                                    String nomeTarefa = listaTarefa.get(position).nome;
                                    String descricao = listaTarefa.get(position).descricao;
                                    String prazo = listaTarefa.get(position).prazo;
                                    List<String> nomeResponsavel = listaTarefa.get(position).responsavel;
                                    try {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                                                "testeProjetos/"+nomeProjeto);
                                        reference.child("TO DO/"+nomeTarefa).removeValue();

                                        reference.child("DOING").child(nomeTarefa+"/descricao").setValue(descricao);
                                        reference.child("DOING").child(nomeTarefa+"/prazo").setValue(prazo);
                                        reference.child("DOING").child(nomeTarefa+"/responsavel").setValue(nomeResponsavel);
                                        Toast.makeText(getActivity(), "Tarefa movida com sucesso", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), "Erro ao mover taefa", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        builder.setNeutralButton("DETALHES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setNegativeButton("NADA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onLongItemClick(final View view, final int position) {
                        SharedPreferences preferences = getActivity().getSharedPreferences("Dados",0);
                        boolean coordenador = preferences.getBoolean("Projeto:", false);
                        boolean dev = preferences.getBoolean("nomeLogadoDev", false);
                        if(dev || coordenador){
                            // TODO: colocar um metodo para somento coordenador poder remover tarefa
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Voce tem certeza que excluir essa tarefa?")
                                    .setTitle("Alerta");
                            builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    excluirTarefa(status, nomeTarefa.get(position));

                                }
                            });
                            builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        switch (index){
            // TODO: Achar um meio de forma que seja possível carregar e mostrar tarefas ao iniciar
            //  a activity e sem ter a necessidade de passar por outras tabs antes de mostrar...
            case 1:
                //bancoDados.loadNomeTarefas(nomeProjeto, "DONE");
                ler_dados_Firebase("DONE",verify1);
                verify1 = true;
                break;
            case 2:
                //bancoDados.loadNomeTarefas(nomeProjeto, "DOING");
                ler_dados_Firebase("DOING",verify2);
                verify2 = true;
                Log.i(TAG, "Caso 2");
                break;
            case 3:
                //bancoDados.loadNomeTarefas(nomeProjeto, "TO DO");
                ler_dados_Firebase("TO DO",verify3);
                verify3 = true;
                estaTODO = true;
                Log.i(TAG, "Caso 3");
                break;
            case 4:
                //bancoDados.loadNomeTarefas(nomeProjeto, "IDEIA");
                ler_dados_Firebase("IDEIA",verify4);
                verify4 = true;
                Log.i(TAG, "Caso 4");
                break;
            default:
                Log.i(TAG,"entrou no defaut");
        }

        return root;
    }

    private void ler_dados_Firebase(final String status, final boolean test){

        // TODO: verificar em qual tarefa e alterar no layout a visibilidade da check box

        final List<String> responsavel = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        this.status = status;

        DatabaseReference teste = database.getReference("testeProjetos/"+nomeProjeto+"/"+status);
        teste.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (!test) {
                        PlaceholderFragment.this.nomeTarefa.add(child.getKey());
                        String descricao = dataSnapshot.child(child.getKey()).child("descricao").getValue().toString();

                        String prazo = dataSnapshot.child(child.getKey()).child("prazo").getValue().toString();

                        long aux = dataSnapshot.child(child.getKey()+"/"+"responsavel").getChildrenCount();
                        for (int a = 0; a < aux; a++){
                            responsavel.add(dataSnapshot.child(child.getKey()).child("responsavel").child(String.valueOf(a)).getValue().toString());

                        }
                        tarefa.setNome(child.getKey());
                        tarefa.setDescricao(descricao);
                        tarefa.setPrazo(prazo);
                        tarefa.setResponsavel(responsavel);
                        listaTarefa.add(tarefa);
                        // TODO: verificar motivo de estar passando apenas uma tarefa para duas posições
                        //  na listaTarefa
                        Log.i(TAG, " Tarefa: " + listaTarefa);
                        if ((long) i + 1 == dataSnapshot.getChildrenCount()){
                            //Log.i(TAG, "Numero de childs: " + dataSnapshot.getChildrenCount());
                        }
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void delay ( int seconds ) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Log.i(TAG,"Delay aplicado");
            }
        }, seconds*1000);
        try {
            Log.i(TAG, "Antes Sleep");
            Thread.sleep(100);
            Log.i(TAG, "Depois Sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void excluirTarefa(String tab, String nomeTarefa) {
        DatabaseReference projetoRef = FirebaseDatabase.getInstance().getReference(
                "testeProjetos/"+this.nomeProjeto+"/"+tab+"/"+nomeTarefa);
        projetoRef.removeValue();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    private void carregarListaTarefas (final String status) {
        DatabaseReference teste = FirebaseDatabase.getInstance().getReference("testeProjetos/" + nomeProjeto + "/" + status);
        teste.addValueEventListener(new ValueEventListener() {
            List<String> aux = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    aux.add(child.getKey());
                    getActivity().getSharedPreferences("Dados", 0)
                            .edit().putStringSet("listaTarefas" + status, new HashSet<>(aux)).apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}