package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        layoutDelMembro();
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
        AdapterMembros adapter = new AdapterMembros(todosMembros);

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
        AdapterMembros adapter = new AdapterMembros(listaMembros);

        // Configurando o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        membrosAtuais.setLayoutManager(layoutManager);
        membrosAtuais.setHasFixedSize(true);
        membrosAtuais.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        membrosAtuais.setAdapter(adapter);
    }

}
