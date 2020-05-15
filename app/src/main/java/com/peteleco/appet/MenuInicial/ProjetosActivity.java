package com.peteleco.appet.MenuInicial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.peteleco.appet.MenuInicial.ProjetosAdapter.AdapterProjetos;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.R;

import java.util.ArrayList;
import java.util.List;

public class ProjetosActivity extends AppCompatActivity {

    private RecyclerView mostrarProjetos;
    private List<ModeloProjetos> listProjetos = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_projetos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item:
                Toast.makeText(this, "Item selecionado", Toast.LENGTH_SHORT).show();

            case R.id.itemItem2:
                Toast.makeText(this, "Item selecionado", Toast.LENGTH_SHORT).show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
        ModeloProjetos APPET = new ModeloProjetos("APPET");
        this.listProjetos.add( APPET );

        ModeloProjetos PET_MIND = new ModeloProjetos("PET_MIND");
        this.listProjetos.add( PET_MIND );

        ModeloProjetos PET_INDICA = new ModeloProjetos("PET_INDICA");
        this.listProjetos.add( PET_INDICA );

        ModeloProjetos OFICINAS = new ModeloProjetos("OFICINAS");
        this.listProjetos.add( OFICINAS );

        ModeloProjetos VAI_COM_PET = new ModeloProjetos("Vai_com_PET");
        this.listProjetos.add( VAI_COM_PET );

    }
}
