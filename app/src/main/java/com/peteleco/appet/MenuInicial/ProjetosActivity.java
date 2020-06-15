package com.peteleco.appet.MenuInicial;

import androidx.annotation.NonNull;
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
                Intent intent = new Intent(getApplicationContext(), NovoProjetoActivity.class);
                startActivity(intent);

            case R.id.itemInfoPessoal:
                Toast.makeText(this, "Item selecionado", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getApplicationContext(), InformacaoPessoalActivity.class);
                startActivity(intent2);
                // TODO: autoexplicativo, mostrar informações pessoais e possibilitar que o usuário atualize.
                
            case R.id.itemLogOut:
                // TODO: Deslogar o usuário e voltar a tela inicial
                //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                //firebaseAuth.signOut();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);
        mostrarProjetos = findViewById(R.id.RecyclerViewProjetos);
        bancoDados = new bancoDados(this.getApplicationContext());

        getSupportActionBar().setTitle("Seus projetos");

        // TODO: Fazer com que seja mostrado apenas os projetos que o usuário é membro
        //  Fazer isso na função "AdicionarProjeto" (Acho)

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

    public void AdicionarProjeto() {
        List<String> listaAux = bancoDados.listaProjetos();
        for (int i = 0; i < listaAux.size(); i++){
            ModeloProjetos Projeto = new ModeloProjetos(listaAux.get(i));
            this.listProjetos.add( Projeto );
        }
    }
}
