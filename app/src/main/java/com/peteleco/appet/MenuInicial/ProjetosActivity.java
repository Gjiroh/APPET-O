package com.peteleco.appet.MenuInicial;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.AdapterProjetos;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.RecyclerItemClickListener;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModeloProjetoEspecificoActivity;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.NovoProjetoActivity;

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
            case R.id.itemAddProjeto:
                Toast.makeText(this, "Item selecionado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NovoProjetoActivity.class);
                startActivity(intent);

            case R.id.itemInfoPessoal:
                Toast.makeText(this, "Item selecionado", Toast.LENGTH_SHORT).show();
                // TODO: autoexplicativo, mostrar informações pessoais e possibilitar que o usuário atualize.

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);
        mostrarProjetos = findViewById(R.id.RecyclerViewProjetos);

        getSupportActionBar().setTitle("Seus projetos");

        // TODO: Adicionar projetos baseado no banco de dados
        // Listar projetos
        this.AdicionarProjeto("APPET");
        this.AdicionarProjeto("PET Mind");
        this.AdicionarProjeto("PET Indica");
        this.AdicionarProjeto("Oficinas para calouros");
        this.AdicionarProjeto("Vai com o PET");
//        this.AdicionarProjeto("Nome do Projeto");

        // Adapter
        AdapterProjetos adapter = new AdapterProjetos(listProjetos);

        // Configurando o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mostrarProjetos.setLayoutManager(layoutManager);
        mostrarProjetos.setHasFixedSize(true);
        mostrarProjetos.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        mostrarProjetos.setAdapter(adapter);

        // Evento de click
        mostrarProjetos.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                mostrarProjetos,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        ModeloProjetos projetos = listProjetos.get(position);
                        Toast.makeText(ProjetosActivity.this
                                , "Projeto " + projetos.getNomeProjeto()
                                , Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), ModeloProjetoEspecificoActivity.class);
//                        EditText editText = (EditText) findViewById(R.id.editText);
//                        String message = editText.getText().toString();
                        intent.putExtra("NOME_PROJETO", projetos.getNomeProjeto());
                        Log.i("teste", projetos.getNomeProjeto());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        ModeloProjetos projetos = listProjetos.get(position);
                        Toast.makeText(ProjetosActivity.this
                                , "Item clicado longo " + projetos.getNomeProjeto()
                                , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));
        SharedPreferences preferences = getSharedPreferences("Nomes",0);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        User user = new User(reference, preferences);
        user.nomesMembros();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences preferences = getSharedPreferences("Nomes",0);
        // Limpando a SharedPreferences do usuário para não ficar ocupando espaço na memória
        preferences.edit().remove("nomes").apply();
        Log.i("teste", "ProjetosActivity listaNomesApagada: "+ preferences.getStringSet("nomes", null));
    }

    public void AdicionarProjeto(String nomeProjeto) {
        ModeloProjetos Projeto = new ModeloProjetos(nomeProjeto);
        this.listProjetos.add( Projeto );
    }
}
