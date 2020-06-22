package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.MenuInicial.ProjetosAdapter.AdapterProjetos;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.Adapters.AdapterMembros;
import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ModerarMembrosActivity extends AppCompatActivity {
    private RecyclerView membrosAtuais;
    private TextView textMembros;
    private Button salverAlt;
    private bancoDados bancoDados;
    private SharedPreferences preferences;
    private AlertDialog.Builder builder;
    private AdapterMembros adapter;
    private List<String> listaNomesSelec;
    private boolean isAdding = false;

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
        builder = new AlertDialog.Builder(this);

        layoutDelMembro();

        listaNomesSelec = adapter.listaMembrosSelec;

        salverAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Voce tem certeza que deseja fazer estas alterações?")
                        .setTitle("Alerta");
                builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nomeProjeto = getSharedPreferences("Activity", 0)
                                .getString("nomeProjeto",null);
                        salvarAlteracoes(adapter.listaMembrosSelec, nomeProjeto);
                        finish();
                    }
                });
                builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void layoutAddMembros () {
        isAdding = true;
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
        adapter = new AdapterMembros(todosMembros);

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


        // Adapter
        adapter = new AdapterMembros(listaMembros);

        // Configurando o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        membrosAtuais.setLayoutManager(layoutManager);
        membrosAtuais.setHasFixedSize(true);
        membrosAtuais.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        membrosAtuais.setAdapter(adapter);
    }

    public void salvarAlteracoes (final List<String> nomesSelec, final String nomeProjeto) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if (nomesSelec != null) {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childID : dataSnapshot.child("users").getChildren()){
                        for (int i = 0; i < nomesSelec.size(); i++){
                            if (dataSnapshot.child("users/"+childID.getKey()+"/nome").getValue().toString()
                                    .equals(nomesSelec.get(i))){
                                Log.i(TAG, "Atendeu a contição");
                                Log.i(TAG, "Ref: " + dataSnapshot.child("users/"+childID.getKey()+"/nome").getValue().toString());
                                Log.i(TAG, "nome: " + nomesSelec.get(i));

                                String  membroID = childID.getKey();
                                Log.i(TAG, "ID: " + membroID);

                                if (isAdding){
                                    DatabaseReference membroPE = reference.child("testeProjetos/"+nomeProjeto+"/membros");
                                    membroPE.child(membroID).setValue("colaborador");
                                }else {
                                    DatabaseReference membroPE = reference.child("testeProjetos/"+nomeProjeto+"/membros/"+membroID);
                                    membroPE.removeValue();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG,"Erro msg: " + databaseError.getMessage());
                    Log.e(TAG,"Erro detalhes: " + databaseError.getDetails());
                }
            });
        } else {
            Toast.makeText(this, "Nenhum nome foi selecionado para alteração", Toast.LENGTH_SHORT).show();
        }



    }

}
