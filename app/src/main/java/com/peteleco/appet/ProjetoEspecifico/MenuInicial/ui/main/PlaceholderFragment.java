package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModeloProjetoEspecificoActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.RecyclerViewTarefas.AdapterTarefas;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.Tarefa;
import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    String TAG="PlaceholderFragment";
    String nomeProjeto;
    bancoDados bancoDados;
    private int index;
    private boolean verify1, verify2, verify3, verify4;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

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

        context = getActivity().getApplicationContext();
        bancoDados = new bancoDados(getActivity().getApplicationContext());
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
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

    }
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
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

        switch (index){
            // TODO: Achar um meio de forma que seja possível carregar e mostrar tarefas ao iniciar
            //  a activity e sem ter a necessidade de passar por outras tabs antes de mostrar...
            case 1:
                //bancoDados.loadNomeTarefas(nomeProjeto, "DONE");
                ler_dados_Firebase("DONE",verify1);
                Log.i(TAG, "Verify: " + verify1);
                verify1 = true;
                break;
            case 2:
                //bancoDados.loadNomeTarefas(nomeProjeto, "DOING");
                ler_dados_Firebase("DOING",verify2);
                Log.i(TAG, "Verify2: " + verify2);
                verify2 = true;
                Log.i(TAG, "Caso 2");
                break;
            case 3:
                //bancoDados.loadNomeTarefas(nomeProjeto, "TO DO");
                ler_dados_Firebase("TO DO",verify3);
                Log.i(TAG, "Verify3: " + verify3);
                verify3 = true;
                Log.i(TAG, "Caso 3");
                break;
            case 4:
                //bancoDados.loadNomeTarefas(nomeProjeto, "IDEIA");
                ler_dados_Firebase("IDEIA",verify4);
                Log.i(TAG, "Verify4: " + verify4);
                verify4 = true;
                Log.i(TAG, "Caso 4");
                break;
            default:
                Log.i(TAG,"entrou no defaut");
        }

        return root;
    }

    private Tarefa ler_dados_Firebase(final String status, final boolean test){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference teste = database.getReference("testeProjetos/"+nomeProjeto+"/"+status);
        teste.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (!test) {
                        Log.i(TAG, "i = "+i);

                        tarefa = dataSnapshot.child(child.getKey()).getValue(Tarefa.class);
                        listaTarefa.add(tarefa);
                        if ((long) i + 1 == dataSnapshot.getChildrenCount()){
                            Log.i(TAG, "Numero de childs: " + dataSnapshot.getChildrenCount());
                        }
                    }
                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return tarefa;
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
}