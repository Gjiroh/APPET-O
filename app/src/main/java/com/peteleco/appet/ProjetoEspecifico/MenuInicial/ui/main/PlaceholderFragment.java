package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModeloProjetoEspecificoActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.Tarefa;
import com.peteleco.appet.R;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    String listaTarefas;

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
        index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Activity",0);
        nomeProjeto = preferences.getString("nomeProjeto",null);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_projeto_especifico, container, false);

        txtView = root.findViewById(R.id.section_label);
        switch (index){
            case 1:
                ler_dados_Firebase("DONE");
                Log.i("teste", "Caso 1");
                break;
            case 2:
                ler_dados_Firebase("DOING");
                Log.i("teste", "Caso 2");
                break;
            case 3:
                ler_dados_Firebase("TO DO");
                Log.i("teste", "Caso 3");
                break;
            case 4:
                ler_dados_Firebase("IDEAS");
                Log.i("teste", "Caso 4");
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

    private Tarefa ler_dados_Firebase(String status){
        Log.i("teste", "lerDados Status: "+status);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Log.i("teste", "PlaceHolder nomeProjeto:"+nomeProjeto);
        DatabaseReference listaRef = database.getReference("projetos/"+nomeProjeto+"/"+status+"/lista");

        // TODO: Verificar o motivo que quando entra no caso 1 do switch, muda para o caso 2 sem fazer
        //  a leitura dos Logs ("As tarefas sao:... bla,bla")
        listaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTarefas = (String) dataSnapshot.getValue();
                assert listaTarefas != null;
                String[] vetorTarefas = listaTarefas.split(",");
                int numeroTarefas = vetorTarefas.length;
                for (int i = 0; i < numeroTarefas; i++){
                    Log.d(TAG, "As tarefas são: " + vetorTarefas[i]);
                }
                Log.d(TAG, "lista das tarefa é " + dataSnapshot.getValue() );
                Log.d(TAG, "tipo da variável é : "  + dataSnapshot.getValue().getClass());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        String nomeTarefa = "tarefa2";
        DatabaseReference myRef = database.getReference("projetos/APPET/" +status+ "/" + nomeTarefa);
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
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return tarefa;
    }
}