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

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    String TAG="PlaceholderFragment";
    String nomeProjeto;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    int index;

    TextView txtView;

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
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            index = index-1;
            Log.i("teste", "Index: "+ index);
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

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewTarefas);
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
            case 0:
                ler_dados_Firebase("DONE");
                Log.i("teste", "Caso 0");
                break;
            case 1:
                ler_dados_Firebase("DOING");
                Log.i("teste", "Caso 1");
                break;
            case 2:
                ler_dados_Firebase("TO DO");
                Log.i("teste", "Caso 2");
                break;
            case 3:
                ler_dados_Firebase("IDEAS");
                Log.i("teste", "Caso 3");
                break;
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

        Log.i("teste", "lerDados Status: "+status);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        Log.i("teste", "PlaceHolder nomeProjeto:"+nomeProjeto);
        DatabaseReference teste = database.getReference("testeProjetos/"+nomeProjeto+"/"+status);
        teste.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    List<String> list = new ArrayList<>();

                    list.add(child.getKey());
                    Log.i("teste", "ProjetosActivityLISTAGEMTarefa: "+ list.toString());


                    nomeTarefa[i] = list.get(i);
                    tarefa = dataSnapshot.child(nomeTarefa[i]).getValue(Tarefa.class);
                    listaTarefa.add(tarefa);
                    //txtView.setText(" Descrição " +tarefa.getDescricao() + "\n Responsável é " +
                    //        tarefa.getResponsavel() + "\n Prazo é " + tarefa.getPrazo());

                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*listaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTarefas = (String) dataSnapshot.getValue();
                assert listaTarefas != null;
                vetorTarefas[0] = listaTarefas.split(",");
                int numeroTarefas = vetorTarefas[0].length;
                for (int i = 0; i < numeroTarefas; i++){
                    Log.d(TAG, "As tarefas são: " + vetorTarefas[0][i]);

                    String nomeTarefa = vetorTarefas[0][i];
                    DatabaseReference myRef = database.getReference("projetos/"+nomeProjeto+"/" +status+ "/" + nomeTarefa);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.

                            tarefa = dataSnapshot.getValue(Tarefa.class);
                            txtView.setText(" Descrição " +tarefa.getDescricao() + "\n Responsável é " +
                                    tarefa.getResponsavel() + "\n Prazo é " + tarefa.getPrazo());
                            Log.i("teste", "Entrou no segundo onDataChange");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                Log.d(TAG, "lista das tarefa é " + dataSnapshot.getValue() );
                Log.d(TAG, "tipo da variável é : "  + dataSnapshot.getValue().getClass());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });*/

        /*nomeTarefa = "tarefa2";
        DatabaseReference myRef = database.getReference("projetos/"+nomeProjeto+"/" +status+ "/" + nomeTarefa);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                /*
                tarefa = dataSnapshot.getValue(Tarefa.class);
                txtView.setText(" Descrição " +tarefa.getDescricao() + "\n responsável é " +
                        tarefa.getResponsavel() + "\n prazo é " + tarefa.getPrazo());
                 */
            /*}

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
        return tarefa;
    }
}