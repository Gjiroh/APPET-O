package com.peteleco.appet.MenuInicial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.peteleco.appet.InformacaoPessoal.InformacaoPessoalActivity;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.AdapterProjetos;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.RecyclerItemClickListener;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.Notifications.NotificationService;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModeloProjetoEspecificoActivity;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.NovoProjetoActivity;
import com.peteleco.appet.FirebaseFunc;

import java.util.ArrayList;
import java.util.List;

public class ProjetosActivity extends AppCompatActivity {

    private RecyclerView mostrarProjetos;
    public List<ModeloProjetos> listProjetos = new ArrayList<>();
    private FirebaseFunc bancoDados;
    private SharedPreferences preferences;
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
            boolean verifyAdm = preferences.getBoolean("nomeLogadoDev", false);
            if (verifyAdm){
                Intent intent = new Intent(this.getApplicationContext(), NovoProjetoActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Esta funcionalidade é exclusiva para os administradores", Toast.LENGTH_LONG).show();
            }


        } else if ( item.getItemId() == R.id.itemInfoPessoal ) {
            Intent infoIntent = new Intent(this.getApplicationContext(), InformacaoPessoalActivity.class);
            startActivity(infoIntent);

            FirebaseFunc func = new FirebaseFunc(getApplicationContext());
            func.verificarPrazosTarefas();

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
        bancoDados = new FirebaseFunc(this.getApplicationContext());
        preferences = getSharedPreferences("Dados", 0);

        getSupportActionBar().setTitle("Seus projetos");
        showProgressBar();
        setRecycler();
    }

    protected void onResume() {
        super.onResume();
        //Log.i(TAG,"onResume");
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
        } else {
            cont = 0;
        }
        getSharedPreferences("Dados", 0).edit().putInt("contAux", cont).apply();
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
        AdapterProjetos adapter = new AdapterProjetos(listProjetos, this.getApplicationContext());

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
        hideProgressBar();
    }

    private void showProgressBar() {
        View projetos;
        projetos = findViewById(R.id.viewProjetos);

        ProgressBar progressBar;
        progressBar = findViewById(R.id.progressBarInicio);

        projetos.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        View projetos;
        projetos = findViewById(R.id.viewProjetos);

        ProgressBar progressBar;
        progressBar = findViewById(R.id.progressBarInicio);

        projetos.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}
