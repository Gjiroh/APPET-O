package com.peteleco.appet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button irCadastrar, logar, esqSenha, enter;
    private EditText login;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        irCadastrar = findViewById(R.id.bt_criarConta);
        irCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Passando para Activity de cadastro
                Intent intent = new Intent(getApplicationContext(), CadastrarActivity.class);
                startActivity(intent);
            }
        });

        logar = findViewById(R.id.bt_entrar);
        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = findViewById(R.id.edt_login);
                String loginSalvar = login.getText().toString();


                senha = findViewById(R.id.edt_password);
                String senhaSalvar = senha.getText().toString();

            }
        });

        esqSenha = findViewById(R.id.bt_esqueceuSenha);
        esqSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declatando a inteção intent
                Intent intencao = new Intent(getApplicationContext(), EsqueceuSenhaActivity.class);
                startActivity(intencao);
            }
        });

        enter = findViewById(R.id.bt_entrar);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencao2 = new Intent(getApplicationContext(), FragmentsActivity.class);
                startActivity(intencao2);
            }
        });

    }


}
