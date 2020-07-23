package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
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
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.DetalheTarefas.DetalhesTarefaActivity;
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
    public boolean verify1, verify2, verify3, verify4, estaTODO, estaDOING, estaDONE, estaIDEA;
    private View root;

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
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Dados",0);
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
    public void onResume() {
        super.onResume();
        String reset = this.getActivity().getSharedPreferences("Dados",0)
                .getString("ReiniciarVerify", "");
        if (reset.equals("reiniciar")){
            this.getActivity().getSharedPreferences("Dados", 0)
                    .edit().putString("ReiniciarVerify", "").apply();
            showProgressBar();
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
            hideProgressBar();
        }
    }

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {

        // TODO: Criar função para quando for selecionado a checkBox de uma tarefa

        root = inflater.inflate(R.layout.fragment_projeto_especifico, container, false);
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
                            builder.setPositiveButton("COMEÇAR TAREFA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String nomeTarefa = listaTarefa.get(position).getNome();
                                    String descricao = listaTarefa.get(position).getDescricao();
                                    String prazo = listaTarefa.get(position).getPrazo();
                                    String nomeResponsavel = listaTarefa.get(position).getResponsavel();
                                    try {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                                                "testeProjetos/"+nomeProjeto);
                                        reference.child("TO DO/"+nomeTarefa).removeValue();

                                        reference.child("DOING").child(nomeTarefa+"/descricao").setValue(descricao);
                                        reference.child("DOING").child(nomeTarefa+"/prazo").setValue(prazo);
                                        reference.child("DOING").child(nomeTarefa+"/responsavel").setValue(nomeResponsavel);
                                        Toast.makeText(getActivity(), "Tarefa movida com sucesso", Toast.LENGTH_SHORT).show();
                                        Intent intent = getActivity().getIntent();
                                        getActivity().finish();
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), "Erro ao mover taefa", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else if (estaDOING){
                            builder.setPositiveButton("FINALIZAR TAREFA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String nomeTarefa = listaTarefa.get(position).getNome();
                                    String descricao = listaTarefa.get(position).getDescricao();
                                    String prazo = listaTarefa.get(position).getPrazo();
                                    String nomeResponsavel = listaTarefa.get(position).getResponsavel();
                                    try {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                                                "testeProjetos/"+nomeProjeto);
                                        reference.child("DOING/"+nomeTarefa).removeValue();

                                        reference.child("DONE").child(nomeTarefa+"/descricao").setValue(descricao);
                                        reference.child("DONE").child(nomeTarefa+"/prazo").setValue(prazo);
                                        reference.child("DONE").child(nomeTarefa+"/responsavel").setValue(nomeResponsavel);
                                        Toast.makeText(getActivity(), "Tarefa movida com sucesso", Toast.LENGTH_SHORT).show();
                                        Intent intentDOING = getActivity().getIntent();
                                        getActivity().finish();
                                        startActivity(intentDOING);
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
                                String nomeTarefa = listaTarefa.get(position).getNome();
                                String descricao = listaTarefa.get(position).getDescricao();
                                String prazo = listaTarefa.get(position).getPrazo();
                                String nomeResponsavel = listaTarefa.get(position).getResponsavel();
                                Intent intentDetalhes = new Intent(getActivity().getApplicationContext(), DetalhesTarefaActivity.class);
                                intentDetalhes.putExtra("nomeTarefa", nomeTarefa);
                                intentDetalhes.putExtra("descricao", descricao);
                                intentDetalhes.putExtra("prazo", prazo);
                                intentDetalhes.putExtra("nomeResponsavel", nomeResponsavel);
                                intentDetalhes.putExtra("nomeProjeto", nomeProjeto);
                                intentDetalhes.putExtra("status",
                                        getStatus(estaDONE, estaIDEA, estaDOING, estaTODO));

                                startActivity(intentDetalhes);
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
                        boolean coordenador = preferences.getBoolean("coordenador", false);
                        boolean dev = preferences.getBoolean("nomeLogadoDev", false);
                        if(dev || coordenador){

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

            case 1:
                //bancoDados.loadNomeTarefas(nomeProjeto, "DONE");
                ler_dados_Firebase("DONE",verify1);
                verify1 = true;
                estaDONE = true;
                break;
            case 2:
                //bancoDados.loadNomeTarefas(nomeProjeto, "DOING");
                ler_dados_Firebase("DOING",verify2);
                verify2 = true;
                estaDOING = true;
                break;
            case 3:
                //bancoDados.loadNomeTarefas(nomeProjeto, "TO DO");
                ler_dados_Firebase("TO DO",verify3);
                verify3 = true;
                estaTODO = true;
                break;
            case 4:
                //bancoDados.loadNomeTarefas(nomeProjeto, "IDEIA");
                ler_dados_Firebase("IDEIA",verify4);
                verify4 = true;
                estaIDEA = true;
                break;
            default:

        }
        return root;
    }

    private void ler_dados_Firebase(final String status, final boolean test){

        // TODO: verificar em qual tarefa e alterar no layout a visibilidade da check box

        if (!test){
            showProgressBar();
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        this.status = status;

        DatabaseReference teste = database.getReference("testeProjetos/"+nomeProjeto+"/"+status);
        teste.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (!test) {
                        String prazo = "", descricao = "", resp = "";
                        PlaceholderFragment.this.nomeTarefa.add(child.getKey());

                        try {
                            descricao = dataSnapshot.child(child.getKey()).child("descricao").getValue().toString();
                        } catch (Exception e){
                            e.printStackTrace();
                            //descricao = "Erro ao carregar";
                            //Toast.makeText(getActivity(), "Erro ao carregar descrição", Toast.LENGTH_SHORT).show();
                        }

                        try{
                            prazo = dataSnapshot.child(child.getKey()).child("prazo").getValue().toString();
                        }catch (Exception e){
                            e.printStackTrace();
                            //prazo = "Erro ao carregar";
                            //Toast.makeText(getActivity(), "Erro ao carregar prazo", Toast.LENGTH_SHORT).show();
                        }

                        try {
                                resp = dataSnapshot.child(child.getKey()).child("responsavel").getValue().toString();
                        } catch (Exception e){
                            e.printStackTrace();
                            //responsavel.add("Erro ao carregar");
                            //Toast.makeText(getActivity(), "Erro ao carregar responsáveis", Toast.LENGTH_SHORT).show();
                        }

                        Tarefa tarefa1 = new Tarefa();
                        tarefa1.setNome(child.getKey());
                        tarefa1.setDescricao(descricao);
                        tarefa1.setPrazo(prazo);
                        tarefa1.setResponsavel(resp);
                        listaTarefa.add(tarefa1);

                        if ((long) i + 1 == dataSnapshot.getChildrenCount()){
                            hideProgressBar();
                            // Verifica se é a ultima Child
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
        try {
            DatabaseReference projetoRef = FirebaseDatabase.getInstance().getReference(
                    "testeProjetos/"+this.nomeProjeto+"/"+tab+"/"+nomeTarefa);
            projetoRef.removeValue();
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void carregarListaTarefas (final String status) {
        DatabaseReference teste = FirebaseDatabase.getInstance().getReference("testeProjetos/" + nomeProjeto + "/" + status);
        teste.addValueEventListener(new ValueEventListener() {
            List<String> aux = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    aux.add(child.getKey());
                    try {
                        getActivity().getSharedPreferences("Dados", 0)
                                .edit().putStringSet("listaTarefas" + status, new HashSet<>(aux)).apply();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showProgressBar() {
        View dados_tarefas;
        dados_tarefas = root.findViewById(R.id.constraintLayoutTrafas);

        ProgressBar progressBar;
        progressBar = root.findViewById(R.id.progressBarTarefas);

        dados_tarefas.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        View dados_tarefas;
        dados_tarefas = root.findViewById(R.id.constraintLayoutTrafas);

        ProgressBar progressBar;
        progressBar = root.findViewById(R.id.progressBarTarefas);

        dados_tarefas.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private String getStatus(boolean estaDONE, boolean estaIDEA, boolean estaDOING, boolean estaTODO) {
        if (estaDOING){
            return "DOING";
        } else if (estaDONE){
            return "DONE";
        } else if (estaTODO) {
            return "TO DO";
        } else if (estaIDEA) {
            return "IDEIA";
        }
        return "";
    }

}