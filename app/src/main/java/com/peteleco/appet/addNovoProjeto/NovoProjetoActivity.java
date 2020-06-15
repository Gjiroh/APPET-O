package com.peteleco.appet.addNovoProjeto;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.Autenticacao_Login.LoginActivity;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.Adapter.AdapterTeste;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;
import com.peteleco.appet.bancoDados;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NovoProjetoActivity extends AppCompatActivity {

    private EditText nomeProjetoNovo, descricaoProjetoNovo;
    private Button btCriaProjeto;
    private RecyclerView mostrarColabs;
    private List<ModelTeste> listaColabs = new ArrayList<>();
    public final static String TAG = "teste";
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference projetobd = FirebaseDatabase.getInstance().getReference("projetos");
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

                if (validarDados(nomeProjeto, descricaoProjeto)){
                    try {
                        List<String> listaColabSelec= adapterTeste.listaColabSelec;
                        projetobd.child(nomeProjeto).child("descricaoProjeto").setValue(descricaoProjeto);
                        projetobd.child(nomeProjeto).child("membros").setValue(listaColabSelec);
                        Toast.makeText(NovoProjetoActivity.this, "Projeto Criado", Toast.LENGTH_SHORT).show();
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
            Log.i(TAG, "NovoProjetoActivity listaNomes: "+ listaNomes);

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

    public boolean validarDados (String nome, String descricao){
        boolean valiDados = false;
        if(nome.isEmpty()){
            nomeProjetoNovo.requestFocus();
            Toast.makeText(this, "Insira um nome de projeto", Toast.LENGTH_SHORT).show();
        } else { valiDados = true; }

        if (descricao.isEmpty()){
            descricaoProjetoNovo.requestFocus();
            Toast.makeText(this, "Insira uma breve descrição de projeto", Toast.LENGTH_SHORT).show();
        } else { valiDados = true; }

        return valiDados;
    }
}
