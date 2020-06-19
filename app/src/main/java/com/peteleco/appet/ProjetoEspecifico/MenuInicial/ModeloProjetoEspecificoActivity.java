package com.peteleco.appet.ProjetoEspecifico.MenuInicial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.peteleco.appet.InformacaoPessoal.InformacaoPessoalActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.ModerarMembrosActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.RecyclerViewTarefas.AdapterTarefas;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main.SectionsPagerAdapter;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.NovoProjetoActivity;
import com.peteleco.appet.bancoDados;

import java.util.ArrayList;
import java.util.List;

public class ModeloProjetoEspecificoActivity extends AppCompatActivity {

    private String nomeProjeto;
    private final static String TAG = "ModeloProjetoEspecífico";
    private Menu menu;
    private bancoDados bancoDados;
    private boolean verificCoord;

    List<Tarefa> listaTarefas = new ArrayList<>();

    public boolean onCreateOptionsMenu (Menu menu){
        if (this.verificCoord) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_projeto_especifico, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        // TODO: Utilizar método ou função para adicionar um membro em um projeto especifico (
        //  Lembrar de utilizar o formato "ID unica do usuario = colaborador (ou coordenador tambem))
        if (item.getItemId() == R.id.item_controlar_membro){
            Intent intent = new Intent(getApplicationContext(), ModerarMembrosActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.item_adicionar_tarefa) {
            Toast.makeText(this, "Building", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto_especifico);
        bancoDados = new bancoDados(getApplicationContext());

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String nomeProjeto = intent.getStringExtra("NOME_PROJETO");
        bancoDados.membrosProjeto(nomeProjeto);

        SharedPreferences preferences2 = getSharedPreferences("Activity", 0);
        preferences2.edit().putString("nomeProjeto", nomeProjeto).apply();

        getSupportActionBar().setTitle(nomeProjeto);

        SharedPreferences preferences = getSharedPreferences("Dados", 0);
        verificCoord = preferences.getBoolean("Projeto:"+nomeProjeto, false);

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