package com.peteleco.appet.addNovoProjeto;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.Adapter.AdapterTeste;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;

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
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Adicionar o evento de clique do botão "Criar Projeto", recuperar os dados inseridos/selecioandos e Adicionar o Projeto ao bd
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_projeto);
        mostrarColabs = findViewById(R.id.RecyclerTeste);
        nomeProjetoNovo = findViewById(R.id.editTextNomeProjeto);
        descricaoProjetoNovo = findViewById(R.id.editTextDescrição);

        //Listar colaboradores
        preferences = getSharedPreferences("Nomes",0);
        Colaboradores(preferences);

        //Adapter
        AdapterTeste adapterTeste = new AdapterTeste(listaColabs);

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
                        projetobd.child(nomeProjeto).child("descricaoProjeto").setValue(descricaoProjeto);
                        Toast.makeText(NovoProjetoActivity.this, "Projeto Criado", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.e("Error", e.toString());
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpando a SharedPreferences do usuário para não ficar ocupando espaço na memória
        NovoProjetoActivity.this.preferences.edit().remove("nomes").apply();
        Log.i(TAG, "NovoProjetoActivity listaNomesApagada: "+ preferences.getStringSet("nomes", null));
    }

    public void Colaboradores(SharedPreferences preferences) {

        // TODO: Problema com o método "user.nomesMembros", onDataChange tem um "delay". Identificar uma possível solução.
        try {
            Thread.sleep(500);
            // Recuperando o nome dos membros que foram salvos
            Set<String> set = preferences.getStringSet("nomes", null);
            Log.i(TAG, "NovoProjetoActivity set: "+ set);

            // Converção do set<String> para uma List<String>
            assert set != null;
            int n = set.size();
            List<String> listaNomes = new ArrayList<String>(n);
            listaNomes.addAll(set);
            Log.i(TAG, "NovoProjetoActivity listaNomes: "+ listaNomes);

            // Adicionando o nome dos membros para serem mostrados na tela do usuário
            int aux = 0;
            while (aux < listaNomes.size()){
                ModelTeste Colab = new ModelTeste(listaNomes.get(aux));
                this.listaColabs.add(Colab);
                aux += 1;
            }

            Log.i(TAG, "NPA: Conectou");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "NPA: Não Conectou");
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
