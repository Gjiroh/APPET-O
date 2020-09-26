package com.peteleco.appet.ProjetoEspecifico.MenuInicial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.AdicionarIdeia.AdicionarIdeiaActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.AdicionarTarefa.AdicionarTarefaActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.ModerarMembrosActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main.SectionsPagerAdapter;
import com.peteleco.appet.R;
import com.peteleco.appet.DatabaseFuncs;

import java.util.ArrayList;
import java.util.List;

public class ModeloProjetoEspecificoActivity extends AppCompatActivity {

    private String nomeProjeto;
    private final static String TAG = "ModeloProjetoEspecífico";
    private Menu menu;
    private DatabaseFuncs bancoDados;
    private boolean verificCoord, isDev;
    private SharedPreferences preferences;

    List<Tarefa> listaTarefas = new ArrayList<>();

    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_projeto_especifico, menu);
        if (!this.verificCoord && !this.isDev) {
            menu.removeItem(R.id.item_controlar_membro);
            menu.removeItem(R.id.item_adicionar_tarefa);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (this.verificCoord || this.isDev) {
            int aux = item.getItemId();
            if (aux == R.id.item_controlar_membro) {
                bancoDados.membrosProjeto(nomeProjeto);
                Intent intent = new Intent(getApplicationContext(), ModerarMembrosActivity.class);
                startActivity(intent);
            } else if (aux == R.id.item_adicionar_tarefa) {
                Intent novaTarefaIntent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                novaTarefaIntent.putExtra("nomeProjeto", nomeProjeto);
                startActivity(novaTarefaIntent);
            } else if (aux == R.id.item_adicionar_ideia) {
                Intent intentNovaIdeia = new Intent(getApplicationContext(), AdicionarIdeiaActivity.class);
                intentNovaIdeia.putExtra("nomeProjeto", nomeProjeto);
                startActivity(intentNovaIdeia);
            }
        } else {
            if (item.getItemId() == R.id.item_adicionar_ideia) {
                Intent intentNovaIdeia = new Intent(getApplicationContext(), AdicionarIdeiaActivity.class);
                startActivity(intentNovaIdeia);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto_especifico);
        bancoDados = new DatabaseFuncs(getApplicationContext());
        preferences = getSharedPreferences("Dados", 0);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        nomeProjeto = intent.getStringExtra("NOME_PROJETO");
        preferences.edit().remove("nomeMembroPE").apply();
        bancoDados.membrosProjeto(nomeProjeto);

        preferences.edit().putString("nomeProjeto", nomeProjeto).apply();

        getSupportActionBar().setTitle(nomeProjeto);

        verificCoord = preferences.getBoolean("coordenador"+nomeProjeto, false);
        isDev = preferences.getBoolean("nomeLogadoDev", false);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
}