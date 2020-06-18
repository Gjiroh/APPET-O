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
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.Tarefa;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;

import java.util.ArrayList;
import java.util.Arrays;
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
        final List<String> listaIDs = new ArrayList<>();
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
                    listaIDs.add(child.getKey());
                    listaNome.add(dataSnapshot.child(child.getKey()+"/nome").getValue().toString());
                    listaCPF.add(dataSnapshot.child(child.getKey()+"/cpf").getValue().toString());
                    listaGRR.add(dataSnapshot.child(child.getKey()+"/grr").getValue().toString());
                    listaTelefone.add(dataSnapshot.child(child.getKey()+"/telefone").getValue().toString());
                    listaEmail.add(dataSnapshot.child(child.getKey()+"/email").getValue().toString());

                    Set<String> setIDs = new HashSet<>(listaIDs);
                    Set<String> setNome = new HashSet<>(listaNome);
                    Set<String> setCPF = new HashSet<>(listaCPF);
                    Set<String> setGRR = new HashSet<>(listaGRR);
                    Set<String> setTelefone = new HashSet<>(listaTelefone);
                    Set<String> setEmail = new HashSet<>(listaEmail);

                    preferences.edit().putStringSet("ids", setIDs).apply();
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

    public void loadNomeLogado (final String email) {

        DatabaseReference user = reference.getDatabase().getReference("users");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    if (email.equals(child.child("email").getValue().toString())){
                        preferences.edit().putString("nomeLogadoUI", child.getKey()).apply();
                        preferences.edit().putString("nomeLogado", child.child("nome").getValue().toString()).apply();
                        preferences.edit().putString("nomeLogadoGRR", child.child("grr").getValue().toString()).apply();
                        preferences.edit().putString("nomeLogadoCPF", child.child("cpf").getValue().toString()).apply();
                        preferences.edit().putString("nomeLogadoEmail", child.child("email").getValue().toString()).apply();
                        preferences.edit().putString("nomeLogadoTelefone", child.child("telefone").getValue().toString()).apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public String getInfoNomeLogado ( String infokey ) {
        // infoKey é a chave que ficou salvo no SharedePreferences,
        // ver keys utilizadas no método "loadNomeLogado"
        return preferences.getString(infokey, null);
    }

    public void salvarDadoEspecificoBD (String referenciaBD, String dado) {
        String userUI = this.getInfoNomeLogado("nomeLogadoUI");

        if (referenciaBD.equals("nome") || referenciaBD.equals("email") || referenciaBD.equals("grr") ||
                referenciaBD.equals("cpf") || referenciaBD.equals("telefone")) {
            try {
                reference.child("users").child(userUI).child(referenciaBD).setValue(dado);
                Toast.makeText(context, "Dado salvo com sucesso", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(context, "Erro ao salvar dado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void salvarDadosBD (User user) {
        String userUI = this.getInfoNomeLogado("nomeLogadoUI");

        try {
            reference.child("users").child(userUI).setValue(user);
            Toast.makeText(context, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Erro ao salvar dados", Toast.LENGTH_SHORT).show();
        }
    }

    private void setProjetosDoUser (final String nomeDoProjeto) {
        final String userUI = getInfoNomeLogado("nomeLogadoUI");
        final List<String> listaProjetos = new ArrayList<>();
        DatabaseReference membros = reference.getDatabase().getReference("testeProjetos/"+nomeDoProjeto+"/membros");
        membros.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    if (userUI.equals(child.getKey())){
                        Set<String> set = preferences.getStringSet("nomeLogadoProjeto", null);
                        if (set != null) {
                            listaProjetos.addAll(set);
                            listaProjetos.add(nomeDoProjeto);
                        } else {
                            listaProjetos.add(0, nomeDoProjeto);
                        }
                        set = new HashSet<>(listaProjetos);
                        preferences.edit().putStringSet("nomeLogadoProjeto", set).apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verificaProjetosUser () {
        DatabaseReference projetos = reference.getDatabase().getReference("testeProjetos");

        projetos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    setProjetosDoUser(child.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro setProjetosDoUser: " + databaseError.getMessage());
            }
        });
    }

    public List<String> getUserProjects () {
        Set<String> set = preferences.getStringSet("nomeLogadoProjeto", null);
        List<String> listaProjetos = new ArrayList<>(set.size());
        listaProjetos.addAll(set);
        Collections.sort(listaProjetos);

        return listaProjetos;
    }

    public void isCoordenador (final String projeto) {
        final String userUI = getInfoNomeLogado("nomeLogadoUI");
        DatabaseReference teste = reference.child("testeProjetos/"+projeto+"/membros/"+userUI);
        try {
            teste.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String aux = dataSnapshot.getValue().toString().toLowerCase();
                    if (aux.equals("coordenador")) {
                        preferences.edit().putBoolean("Projeto:"+projeto,true).apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            });
        }catch (Exception e){
            Log.e(TAG, "Erro ao utilizar addValueEventListener");
            Log.e(TAG, e.getMessage());
        }
    }

    public void getInfoMembro (String idUnica, String info) {
        try {
            reference.child("users/"+idUnica+"/"+info).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Set<String> set = preferences.getStringSet("nomeMembroPE", null);
                    List<String> aux = new ArrayList<>();
                    if (set == null){
                        aux.add(0, dataSnapshot.getValue().toString());
                    } else {
                        aux.addAll(set);
                        try {
                            aux.add(dataSnapshot.getValue().toString());
                        } catch (Exception e) {
                            Log.e(TAG, "Referencia invalida ou nula");
                        }
                        Collections.sort(aux);
                    }
                    set = new HashSet<>(aux);
                    preferences.edit().putStringSet("nomeMembroPE",  set).apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }

    public void membrosProjeto (String projeto) {

        reference.child("testeProjetos/"+projeto+"/membros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    getInfoMembro(child.getKey(), "nome");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro ao recuperar IDs em membrosProjeto");
            }
        });


    }

    public String getMembroID (String ref, String info) {
        String aux = ref.toLowerCase();
        List<String> listIDs = new ArrayList<>(preferences.getStringSet("ids", null));
        Log.i(TAG, "Lista IDs: " + listIDs);
        if (aux.equals("cpf") || aux.equals("nome") || aux.equals("grr") || aux.equals("email")
                || aux.equals("telefone")) {
            Log.i(TAG, "ref usada foi: " + ref + " e Info: "+ info);
            for (int i = 0; i < listIDs.size(); i++ ){
                Log.i(TAG, "listIDs.get(i): " + listIDs.get(i));

                try {
                    // TODO: tem um problema aqui, a listIDs não passa da primeira posição (em outras palavras é
                    //  sempre verdadeira). Tentar fazer meio que uma verificação da referencia ou
                    //  utilizar outro meio/método para recuperar a ID Unica do usuário
                    DatabaseReference teste = reference.getDatabase().getReference("users/"+listIDs.get(i)+"/"+ref+"/"+info);
                    String refDB = reference.getDatabase().getReference("users/"+listIDs.get(i)+"/"+ref+"/"+info).getKey();
                    Log.i(TAG, "referenciaDB: " + refDB);
                    if (refDB.equals(info)){
                        return reference.child("users/"+listIDs.get(i)).getKey();
                    } else {
                        Log.i(TAG,"Informação colocada mão deu match ou não existe");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.i(TAG, "Erro ao referencia no banco de dados");
                }
            }
        } else {
            Log.i(TAG, "Ref colocada não bate com o banco de dados");
        }
        return null;
    }

    // TODO: Criar método para adicionar/remover usuarios em um projeto específico pelo coordenador de projeto


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
