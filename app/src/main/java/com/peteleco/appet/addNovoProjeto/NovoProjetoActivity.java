package com.peteleco.appet.addNovoProjeto;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.Adapter.AdapterTeste;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;
import com.peteleco.appet.bancoDados;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NovoProjetoActivity extends AppCompatActivity {

    private EditText nomeProjetoNovo, descricaoProjetoNovo;
    private Button btCriaProjeto;
    private RecyclerView mostrarColabs;
    private List<ModelTeste> listaColabs = new ArrayList<>();
    public final static String TAG = "teste";
    private DatabaseReference projetobd = FirebaseDatabase.getInstance().getReference("testeProjetos");
    private bancoDados bancoDados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_projeto);
        mostrarColabs = findViewById(R.id.RecyclerTeste);
        nomeProjetoNovo = findViewById(R.id.editTextNomeProjeto);
        descricaoProjetoNovo = findViewById(R.id.editTextDescrição);
        bancoDados = new bancoDados(getBaseContext());

        getSupportActionBar().setTitle("Novo Projeto");

        //Listar colaboradores
        Colaboradores();

        //Adapter
        final AdapterTeste adapterTeste = new AdapterTeste(listaColabs);

        // Configurando o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mostrarColabs.setLayoutManager(layoutManager);
        mostrarColabs.setHasFixedSize(true);
        mostrarColabs.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        mostrarColabs.setAdapter(adapterTeste);

        // Evento de clique no botao Criar Projeto
        btCriaProjeto = findViewById(R.id.buttonCriarProjeto);
        btCriaProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeProjeto = nomeProjetoNovo.getText().toString();
                String descricaoProjeto = descricaoProjetoNovo.getText().toString();
                List<String> listaColabSelec= adapterTeste.listaColabSelec;
                if (validarDados(nomeProjeto, descricaoProjeto, listaColabSelec)){
                    try {
                        adicionarProjeto(listaColabSelec, nomeProjeto, descricaoProjeto);
                        finish();
                    }catch (Exception e){
                        Log.e("error", e.toString());
                        Toast.makeText(NovoProjetoActivity.this, "Erro ao criar o projeto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void Colaboradores() {

        try {
            // Recuperando o nome dos membros que foram salvos
            List<String> listaNomes;
            listaNomes = bancoDados.getInfos("nome");
            Collections.sort(listaNomes);

            // Adicionando o nome dos membros para serem mostrados na tela do usuário
            for (int i = 0; i < listaNomes.size(); i++){
                ModelTeste Colab = new ModelTeste(listaNomes.get(i));
                this.listaColabs.add(Colab);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro: " + e.getMessage());
        }

    }

    public boolean validarDados (String nome, String descricao, List<String> listaColabSelec){
        boolean valiDados = false;
        boolean existente = false;
        List<String> projetos = bancoDados.listaProjetos();
        for (int i = 0; i < projetos.size(); i ++) {
            if (nome.equals(projetos.get(i))){
                existente = true;
            }
        }
        if(nome.isEmpty()){
            nomeProjetoNovo.requestFocus();
            nomeProjetoNovo.setError("Insira um nome para o projeto");
        } else if (existente) {
            nomeProjetoNovo.requestFocus();
            nomeProjetoNovo.setError("Esse projeto ja existe");
        }else { valiDados = true; }

        if (descricao.isEmpty()){
            descricaoProjetoNovo.requestFocus();
            descricaoProjetoNovo.setError("Insira uma breve descrição");
        } else { valiDados = true; }

        if (listaColabSelec.isEmpty()){
            Toast.makeText(this, "Selecione ao menos um integrante do projeto", Toast.LENGTH_SHORT).show();
        } else { valiDados = true; }

        return valiDados;
    }

    public void adicionarProjeto (final List<String> listaColabSelec, final String nomeProjeto, final String descricaoProjeto) {
         FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot child : dataSnapshot.getChildren()){
                     for (int i = 0; i < listaColabSelec.size(); i ++){
                         if (child.child("nome").getValue().toString().toLowerCase().equals(listaColabSelec.get(i).toLowerCase())) {
                            projetobd.child(nomeProjeto+"/descricaoProjeto").setValue(descricaoProjeto);
                            if (child.getKey().equals(getSharedPreferences("Dados", 0).getString("nomeLogadoUI", null))){
                                projetobd.child(nomeProjeto+"/membros/"+child.getKey()).setValue("coordenador");
                            } else {
                                projetobd.child(nomeProjeto+"/membros/"+child.getKey()).setValue("colaborador");
                            }
                         }
                     }
                     Toast.makeText(NovoProjetoActivity.this, "Projeto Criado", Toast.LENGTH_SHORT).show();
                     bancoDados.verificaProjetosUser();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

    }
}
