package com.peteleco.appet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.Tarefa;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class bancoDados {
    private DatabaseReference reference;
    private SharedPreferences preferences;
    private String TAG = "bancoDados.class";
    private Context context;

    public bancoDados(Context context) {
        reference = FirebaseDatabase.getInstance().getReference();
        preferences = context.getSharedPreferences("Dados", 0);
        this.context = context;
    }

    public void carregarUsuarios () {
        final List<String> listaNome = new ArrayList<>();
        final List<String> listaCPF = new ArrayList<>();
        final List<String> listaGRR = new ArrayList<>();
        final List<String> listaEmail = new ArrayList<>();
        final List<String> listaTelefone = new ArrayList<>();

        DatabaseReference users = reference.getDatabase().getReference("users");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    listaNome.add(dataSnapshot.child(child.getKey()+"/nome").getValue().toString());
                    listaCPF.add(dataSnapshot.child(child.getKey()+"/cpf").getValue().toString());
                    listaGRR.add(dataSnapshot.child(child.getKey()+"/grr").getValue().toString());
                    listaTelefone.add(dataSnapshot.child(child.getKey()+"/telefone").getValue().toString());
                    listaEmail.add(dataSnapshot.child(child.getKey()+"/email").getValue().toString());

                    Set<String> setNome = new HashSet<>(listaNome);
                    Set<String> setCPF = new HashSet<>(listaCPF);
                    Set<String> setGRR = new HashSet<>(listaGRR);
                    Set<String> setTelefone = new HashSet<>(listaTelefone);
                    Set<String> setEmail = new HashSet<>(listaEmail);

                    preferences.edit().putStringSet("nome", setNome).apply();
                    preferences.edit().putStringSet("grr", setGRR).apply();
                    preferences.edit().putStringSet("cpf", setCPF).apply();
                    preferences.edit().putStringSet("telefone", setTelefone).apply();
                    preferences.edit().putStringSet("email", setEmail).apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro ao carregar Informações");
            }
        });
    }

    public List<String> getInfos (String info) {

        List<String> listaInfo = new ArrayList<>();
        try {
            // Recuperando o nome dos membros que foram salvos
            Set<String> setInfo;

            if (info.toLowerCase().equals("nome")
                    || info.toLowerCase().equals("cpf")
                    || info.toLowerCase().equals("email")
                    || info.toLowerCase().equals("grr")
                    || info.toLowerCase().equals("telefone")){

                setInfo = preferences.getStringSet(info.toLowerCase(), null);

                // Converção do set<String> para uma List<String>
                assert setInfo != null;
                int n = setInfo.size();
                listaInfo = new ArrayList<>(n);
                listaInfo.addAll(setInfo);
            } else {
                Toast.makeText(context, "Houve algum erro ao procurar informação no banco de dados", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao listar Usuários");
        }


        return listaInfo;
    }

    public void carregarProjetos () {
        final List<String> listaProjetos = new ArrayList<>();
        DatabaseReference projects = reference.getDatabase().getReference("testeProjetos");

        projects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    listaProjetos.add(child.getKey());

                }
                Set<String> set = new HashSet<>(listaProjetos);
                preferences.edit().putStringSet("projetos", set).apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro ao carregar Projetos");
            }
        });
    }

    public List<String> listaProjetos () {
        carregarProjetos();
        List<String> listaProjetos = null;
        try {
            // Recuperando o nome dos membros que foram salvos
            Set<String> set = preferences.getStringSet("projetos", null);

            // Converção do set<String> para uma List<String>
            assert set != null;
            int n = set.size();
            listaProjetos = new ArrayList<>(n);
            listaProjetos.addAll(set);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao listar Projetos");
        }

        Collections.sort(listaProjetos);
        return listaProjetos;
    }

    public void getNomeLogado (String email) {

        reference

    }

    //TODO: tentar utilizar essa função para ler o a descricao/prazo/responsavel das tarefas e então mostrar ao usuáio
    /*public void loadNomeTarefas (final String nomeProjeto, final String status) {

        final Tarefa[] tarefa = {new Tarefa()};
        final List<String> list = new ArrayList<>();
        final List<String> listaProjetos = listaProjetos();

        for (int a = 0; a < 4; a++){

            //final String[] status = {"DONE", "DOING", "TO DO", "IDEIA"};

            for (int i = 0; i < listaProjetos.size(); i++){

                //final String nomeProjeto = listaProjetos.get(i);
                DatabaseReference projetoReference = reference.getDatabase().getReference(
                        "testeProjetos/" + nomeProjeto + "/" + status);

                //final int finalA = a;
                projetoReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            list.add(child.getKey());

                            tarefa[0] = dataSnapshot.child(child.getKey()).getValue(Tarefa.class);
                        }
                        Collections.sort(list);
                        HashSet<String> set = new HashSet<>(list);
                        preferences.edit().putStringSet(nomeProjeto.toLowerCase()+status, set).apply();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Não foi possível carregar os nomes das tarefas");
                    }
                });
            }
        }
    }*/
}
