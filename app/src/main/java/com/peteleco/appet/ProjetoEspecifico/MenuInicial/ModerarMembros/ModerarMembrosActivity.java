package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.AdapterProjetos;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.Adapters.AdapterMembros;
import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModerarMembrosActivity extends AppCompatActivity {
    private RecyclerView membrosAtuais;
    private TextView textMembros;
    private Button salverAlt;
    private bancoDados bancoDados;
    private SharedPreferences preferences;
    private AlertDialog.Builder builder;
    private AdapterMembros adapter;

    private final static String TAG = "ModerarMembros";


    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_moderar_membros, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.item_adicionar_membro){
            layoutAddMembros();
            Toast.makeText(this, "Opção adicionar membro selecionada", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.item_remover_membro) {
            layoutDelMembro();
            Toast.makeText(this, "Opção remover membro selecionada", Toast.LENGTH_SHORT).show();
        }
            return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderar_membros);
        getSupportActionBar().setTitle("Membros");

        membrosAtuais = findViewById(R.id.recyclerViewMembrosAtuais);
        textMembros = findViewById(R.id.textViewAlterarMembro);
        salverAlt = findViewById(R.id.buttonSalvarAlteracaoMembros);
        bancoDados = new bancoDados(getApplicationContext());
        preferences = getSharedPreferences("Dados",0);
        builder = new AlertDialog.Builder(this);

        layoutDelMembro();

        salverAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Voce tem certeza que deseja fazer estas alterações?")
                        .setTitle("Alerta");
                builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nomeProjeto = getSharedPreferences("Activity", 0)
                                .getString("nomeProjeto", null);
                        int i = preferences.getInt("sizeNomeSelec", 0);
                        String[] strings = new String[i];
                        String stringID;
                        for (int a = 0; a < i; a++) {
                            // TODO: Revisar parte em que acessa e verifica ID Unica. Falta algo
                            //  pois só acessa a primeira, talvez fazer/achar algum fator de verificação
                            try {
                                strings[a] = preferences.getString("nomeSelec"+a, null);
                                if (strings[a] != null ){
                                    stringID = bancoDados.getMembroID("nome", strings[a]);

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                                            "testeProjetos/"+nomeProjeto+"/membros/"+ stringID
                                    );
                                    Log.i(TAG, "a: " + a);
                                    Log.i(TAG, "nome: " + strings[a]);
                                    Log.i(TAG, "id: " + stringID);
                                    Log.i(TAG, "refDB: " + reference.getKey());
                                }

                            } catch (Exception e){
                                Log.e(TAG, "Erro ocorrido ao recuperar um nome selecionado " +
                                        "nome não foi selecionado");
                            }

                        }

                        //finish();
                    }
                });
                builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void layoutAddMembros () {
        // Listar membros TODO: Arrumar lista para mostrar apenas membros desse projeto
        textMembros.setText("Demais Membros");
        List<String> listaMembrosProjeto = new ArrayList<>();
        List<String> todosMembros = new ArrayList<>();
        List<String> aux = new ArrayList<>();
        if (preferences.getStringSet("nomeMembroPE", null) != null){
            listaMembrosProjeto.addAll(preferences.getStringSet("nomeMembroPE", null));
        }
        if (preferences.getStringSet("nome", null) != null) {
            todosMembros.addAll(preferences.getStringSet("nome", null));
        }
        Collections.sort(listaMembrosProjeto);
        Collections.sort(todosMembros);

        for (int i = 0; i < todosMembros.size(); i++) {
            for (int a = 0; a < listaMembrosProjeto.size(); a++){
                if (!todosMembros.get(i).equals(listaMembrosProjeto.get(a))){
                } else {
                    todosMembros.remove(i);
                }

            }
        }
        // Adapter
        adapter = new AdapterMembros(todosMembros, this.getApplicationContext());

        // Configurando o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        membrosAtuais.setLayoutManager(layoutManager);
        membrosAtuais.setHasFixedSize(true);
        membrosAtuais.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        membrosAtuais.setAdapter(adapter);
    }

    public void layoutDelMembro () {
        textMembros.setText("Membros Atuais do Projeto");
        List<String> listaMembros = new ArrayList<>();
        listaMembros.addAll(preferences.getStringSet("nomeMembroPE", null));
        Collections.sort(listaMembros);

        // Listar membros participantes do projeto

        // Listar demais membros

        // Adapter
        adapter = new AdapterMembros(listaMembros, this.getApplicationContext());

        // Configurando o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        membrosAtuais.setLayoutManager(layoutManager);
        membrosAtuais.setHasFixedSize(true);
        membrosAtuais.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        membrosAtuais.setAdapter(adapter);
    }

}
