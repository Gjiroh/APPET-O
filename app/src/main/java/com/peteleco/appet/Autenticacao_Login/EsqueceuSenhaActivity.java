package com.peteleco.appet.Autenticacao_Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

import java.util.List;

public class EsqueceuSenhaActivity extends AppCompatActivity {
    private Button btNovaSenha;
    private EditText campoCpf, campoEmail;
    private FirebaseAuth mAuth;
    private TextView textoCpf;
    private final static String TAG = "EsqueceuSenhaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        btNovaSenha = findViewById(R.id.btRecuperarSenha);
        campoCpf = findViewById(R.id.editTextCPF);
        campoEmail = findViewById(R.id.editTextE);
        textoCpf = findViewById(R.id.textEsqCPF);

        mAuth = FirebaseAuth.getInstance();

        btNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean formatoDados = validateForm(campoEmail, campoCpf);
                if (formatoDados) {
                    String email = campoEmail.getText().toString();
                    String cpf = campoCpf.getText().toString();


                    boolean verificEmail = verificarDado("email", email);
                    boolean verificCPF = verificarDado("cpf", cpf);

                    if (verificEmail && verificCPF) {
                        try {
                            mAuth.sendPasswordResetEmail(email);
                            Toast.makeText(EsqueceuSenhaActivity.this, "Um e-mail foi enviado para gerar uma nova senha",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(EsqueceuSenhaActivity.this, "Algo deu errado",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    public boolean verificarDado (String tipoDado, String dadoInserido) {
        boolean verificar = false;

        bancoDados bancoDados = new bancoDados(this.getApplicationContext());
        List<String> dadosCadastrados = bancoDados.getInfos(tipoDado);

        for (int i = 0; i < dadosCadastrados.size(); i++){
            if (dadoInserido.equals(dadosCadastrados.get(i))){
                verificar = true;
            }
        }
        if (!verificar){
            Toast.makeText(
                    this,
                    "Algo deu errado, por favor, verifique os dados inseridos",
                    Toast.LENGTH_LONG
            ).show();
        }

        return verificar;
    }

    private boolean validateForm(EditText email, EditText cpf) {

        if (email.getText().toString().isEmpty()) {
            email.setError("Este campo é obrigatório");
            email.requestFocus();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Digite um formato de e-mail válido");
            email.requestFocus();
            return false;
        }else {
            email.setError(null);
        }

        if (cpf.getText().toString().isEmpty()) {
            cpf.setError("Este campo é obrigatório");
            cpf.requestFocus();
            return false;
        }else if (cpf.getText().length() != 11) {
            cpf.setError("CPF inválido. Por Favor insira um CPF válido");
            cpf.requestFocus();
            return false;
        }else {
            cpf.setError(null);
        }

        return true;
    }
}