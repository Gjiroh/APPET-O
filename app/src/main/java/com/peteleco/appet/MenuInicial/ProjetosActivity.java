package com.peteleco.appet.MenuInicial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.InformacaoPessoal.InformacaoPessoalActivity;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.AdapterProjetos;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.RecyclerItemClickListener;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModeloProjetoEspecificoActivity;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.NovoProjetoActivity;
import com.peteleco.appet.bancoDados;

import java.util.ArrayList;
import java.util.List;

public class ProjetosActivity extends AppCompatActivity {

    private RecyclerView mostrarProjetos;
    public List<ModeloProjetos> listProjetos = new ArrayList<>();
    private bancoDados bancoDados;
    private final static String TAG = "ProjetosActivity";
    private int cont;

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_projetos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.itemAddProjeto){
            // TODO: talvez restringir o acesso a essa função
            Intent intent = new Intent(this.getApplicationContext(), NovoProjetoActivity.class);
            startActivity(intent);

        } else if ( item.getItemId() == R.id.itemInfoPessoal ) {
            Intent infoIntent = new Intent(this.getApplicationContext(), InformacaoPessoalActivity.class);
            startActivity(infoIntent);

        } else if ( item.getItemId() == R.id.itemLogOut ) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);

        /* TODO: Por algum motivo estranho o switch bugou
            quando seleciona um caso ESPECIFICO, ele entra nos outros tbm (?????????????????)
        switch (item.getItemId()) {
            case R.id.itemAddProjeto:
                Log.i(TAG, "Clicou itemAddProjeto");

            case R.id.itemInfoPessoal:
                Log.i(TAG, "Clicou itemInfoPessoal");
                Intent infoIntent = new Intent(this.getApplicationContext(), InformacaoPessoalActivity.class);
                startActivity(infoIntent);

            case R.id.itemLogOut:
                Log.i(TAG, "Clicou itemLogOut");
                //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                //firebaseAuth.signOut();

            default:
                return super.onOptionsItemSelected(item);
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);

        cont = 0;
        mostrarProjetos = findViewById(R.id.RecyclerViewProjetos);
        bancoDados = new bancoDados(this.getApplicationContext());

        getSupportActionBar().setTitle("Seus projetos");

        setRecycler();
    }

    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
        cont = getSharedPreferences("Dados", 0).getInt("contAux", 0);
        if (cont == 1) {
            finish();
            startActivity(getIntent());
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        cont = getSharedPreferences("Dados", 0).getInt("contAux", 0);
        if (cont == 0){
            cont = 1;
            getSharedPreferences("Dados", 0).edit().putInt("contAux", cont).apply();
        } else {
            cont = 0;
            getSharedPreferences("Dados", 0).edit().putInt("contAux", cont).apply();
        }
    }

    public void AdicionarProjeto() {
        // Utilizar .listaProjetos para mostrar todos os projetos
        this.listProjetos.clear();
        bancoDados.verificaProjetosUser();
        List<String> listaAux = bancoDados.getUserProjects();
        for (int i = 0; i < listaAux.size(); i++){
            ModeloProjetos Projeto = new ModeloProjetos(listaAux.get(i));
            bancoDados.isCoordenador(listaAux.get(i));
            this.listProjetos.add( Projeto );
        }
    }

    public void setRecycler () {
        // Listar projetos
        this.AdicionarProjeto();

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

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        ModeloProjetos projetos = listProjetos.get(position);
                        Toast.makeText(ProjetosActivity.this
                                , "Projeto: " + projetos.getNomeProjeto()
                                , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));
    }
}
