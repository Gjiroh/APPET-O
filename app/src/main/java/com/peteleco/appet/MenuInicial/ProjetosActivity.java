package com.peteleco.appet.MenuInicial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.peteleco.appet.MenuInicial.ProjetosAdapter.AdapterProjetos;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.R;

import java.util.ArrayList;
import java.util.List;

public class ProjetosActivity extends AppCompatActivity {

    private RecyclerView mostrarProjetos;
    private List<ModeloProjetos> listProjetos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);

        mostrarProjetos = findViewById(R.id.RecyclerViewProjetos);

        // Listar projetos
        this.AdicionarProjeto("Nome do Projeto");

        // Adapter
        AdapterProjetos adapter = new AdapterProjetos(listProjetos);

        // Configurando o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mostrarProjetos.setLayoutManager(layoutManager);
        mostrarProjetos.setHasFixedSize(true);
        mostrarProjetos.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        mostrarProjetos.setAdapter(adapter);
    }

    public void AdicionarProjeto(String nomeProjeto) {
        ModeloProjetos modeloProjetos = new ModeloProjetos(nomeProjeto);
        this.listProjetos.add( modeloProjetos );

    }
}
