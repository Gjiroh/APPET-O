package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

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
        bancoDados = new bancoDados(getActivity().getApplicationContext());
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Activity",0);
        nomeProjeto = preferences.getString("nomeProjeto",null);

    }
    @Override
    public View onCreateView(
            // TODO: Tem algum bug ao selecionar os casos no Switch!!
            //  no não está entrando no caso 4 e buga o resto depois

            @NonNull LayoutInflater inflater, ViewGroup container,
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
        //txtView = root.findViewById(R.id.section_label);
        switch (index){
            // TODO: ver o bug quando seleciona o index = 1, repete o acesso ao index
            case 1:
                //bancoDados.loadNomeTarefas(nomeProjeto, "DONE");
                ler_dados_Firebase("DONE");
                Log.i(TAG, "Caso 1");
                break;
            case 2:
                //bancoDados.loadNomeTarefas(nomeProjeto, "DOING");
                ler_dados_Firebase("DOING");
                Log.i(TAG, "Caso 2");
                break;
            case 3:
                //bancoDados.loadNomeTarefas(nomeProjeto, "TO DO");
                ler_dados_Firebase("TO DO");
                Log.i(TAG, "Caso 3");
                break;
            case 4:
                //bancoDados.loadNomeTarefas(nomeProjeto, "IDEIA");
                ler_dados_Firebase("IDEIA");
                Log.i(TAG, "Caso 4");
                break;
            default:
                Log.i(TAG,"entrou no defaut");
        }
//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private Tarefa ler_dados_Firebase(final String status){
        final String[] nomeTarefa = new String[1];

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference teste = database.getReference("testeProjetos/"+nomeProjeto+"/"+status);
        teste.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    List<String> list = new ArrayList<>();
                    list.add(child.getKey());
                    nomeTarefa[i] = list.get(i);
                    tarefa = dataSnapshot.child(nomeTarefa[i]).getValue(Tarefa.class);
                    listaTarefa.add(tarefa);

                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return tarefa;
    }
}