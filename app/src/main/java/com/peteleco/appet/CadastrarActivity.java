package com.peteleco.appet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class CadastrarActivity extends AppCompatActivity {

    private Button cadastrar;
    private EditText nome, senha, grr, email, cpf;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        cadastrar = findViewById(R.id.btCadastro);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = findViewById(R.id.editNome);
                String pegarNome = nome.getText().toString();

                senha = findViewById(R.id.editSenha);
                String pegarSenha = senha.getText().toString();

                grr = findViewById(R.id.editGRR);
                String pegarGRR = grr.getText().toString();

                email = findViewById(R.id.editEmail);
                String pegarEmail = email.getText().toString();

                cpf = findViewById(R.id.editCPF);
                String pegarCPF = cpf.getText().toString();

            }
        });
    }
}
