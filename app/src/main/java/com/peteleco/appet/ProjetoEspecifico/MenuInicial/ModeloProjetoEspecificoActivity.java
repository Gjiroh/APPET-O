package com.peteleco.appet.ProjetoEspecifico.MenuInicial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.peteleco.appet.MenuInicial.ProjetosActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main.SectionsPagerAdapter;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.NovoProjetoActivity;

public class ModeloProjetoEspecificoActivity extends AppCompatActivity {

    public String nomeProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto_especifico);
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
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String nomeProjeto = intent.getStringExtra("NOME_PROJETO");

        SharedPreferences preferences = getSharedPreferences("Activity", 0);
        preferences.edit().putString("nomeProjeto", nomeProjeto).apply();

        getSupportActionBar().setTitle(nomeProjeto);
    }


}