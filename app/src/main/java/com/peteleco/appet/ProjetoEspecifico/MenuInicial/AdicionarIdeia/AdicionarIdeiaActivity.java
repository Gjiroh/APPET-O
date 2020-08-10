package com.peteleco.appet.ProjetoEspecifico.MenuInicial.AdicionarIdeia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.R;

import java.util.ArrayList;
import java.util.List;

public class AdicionarIdeiaActivity extends AppCompatActivity {
    private EditText ideia, descricao;
    private Button btSalvar;
    private String nomeProjeto;
    private ProgressBar progressBar;
    private List<String> listaIdeia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_ideia);
        getSupportActionBar().setTitle("Ideia");
        nomeProjeto = getIntent().getExtras().getString("nomeProjeto");
        Log.i("TAG", nomeProjeto);
        btSalvar = findViewById(R.id.buttonSalvarIdeia);
        ideia = findViewById(R.id.editTextTituloIdeia);
        descricao = findViewById(R.id.editTextDescricaoIdeia);
        progressBar = findViewById(R.id.progressBarIdeia);
        listaIdeia = new ArrayList<>();
        getIdeasNames();

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showProgressBar();
                    String sIdeia = ideia.getText().toString();
                    String sDescri = descricao.getText().toString();
                    if (verific(sIdeia, sDescri)){
                        salvar(sIdeia, sDescri);
                        Toast.makeText(AdicionarIdeiaActivity.this, "Ideia salva com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    hideProgressBar();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private boolean verific (String nomeIdeia, String descri) {
        if (nomeIdeia.isEmpty()){
            ideia.requestFocus();
            ideia.setError("Este campo não pode ser vazio");
            return false;
        } else {
            for (int i = 0; i < listaIdeia.size(); i++){
                if (listaIdeia.get(i).equals(nomeIdeia)){
                    ideia.requestFocus();
                    ideia.setError("Este nome de ideia ja exite");
                    return false;
                }
            }
        }
        if (descri.isEmpty()){
            descricao.requestFocus();
            descricao.setError("Este campo não pode ser vazio");
            return false;
        }
        return true;
    }

    private void getIdeasNames () {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("testeProjetos/"+nomeProjeto+"/IDEIA");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    listaIdeia.add(child.getKey());
                    Log.i("TAG", child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void salvar (String nomeIdeia, String descri) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("testeProjetos/"+nomeProjeto+"/IDEIA");
        reference.child(nomeIdeia).child("descricao").setValue(descri);
    }

    private void showProgressBar() {
        View dados_cadastro;
        dados_cadastro = findViewById(R.id.linearLayoutIdeia);

        View progressBar;
        progressBar = findViewById(R.id.progressBarIdeia);

        dados_cadastro.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        View dados_cadastro;
        dados_cadastro = findViewById(R.id.linearLayoutIdeia);

        View progressBar;
        progressBar = findViewById(R.id.progressBarIdeia);

        dados_cadastro.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
