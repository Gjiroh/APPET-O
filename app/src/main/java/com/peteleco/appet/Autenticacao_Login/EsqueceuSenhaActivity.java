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
import com.peteleco.appet.FirebaseFuncs;

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
        campoCpf = findViewById(R.id.editTextCPFUser);
        campoEmail = findViewById(R.id.editTextE);
        textoCpf = findViewById(R.id.textEsqCPF);

        mAuth = FirebaseAuth.getInstance();

        btNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //mAuth é null

                    String email = campoEmail.getText().toString();
                    String cpf = campoCpf.getText().toString();
                    boolean formatoDados = validateForm(email,cpf);

                    if (formatoDados && verificarDado("email", email) && verificarDado("cpf", cpf)){
                        mAuth.sendPasswordResetEmail(email);
                        Toast.makeText(EsqueceuSenhaActivity.this, "Um e-mail foi enviado para uma nova senha",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EsqueceuSenhaActivity.this, "Algo deu errado",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean verificarDado (String tipoDado, String dadoInserido) {
        boolean verificar = false;

        FirebaseFuncs bancoDados = new FirebaseFuncs(this.getApplicationContext());
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

    private boolean validateForm(String email, String cpf) {

        if (TextUtils.isEmpty(email)) {
            campoEmail.setError("Este campo é obrigatório");
            campoEmail.requestFocus();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campoEmail.setError("Digite um formato de e-mail válido");
            campoEmail.requestFocus();
            return false;
        }else {
            campoEmail.setError(null);
        }

        if (TextUtils.isEmpty(cpf)) {
            campoCpf.setError("Este campo é obrigatório");
            campoCpf.requestFocus();
            return false;
        }else if (cpf.length() != 11) {
            campoCpf.setError("CPF inválido. Por Favor insira um CPF válido");
            campoCpf.requestFocus();
            return false;
        }else {
            campoCpf.setError(null);
        }

        return true;
    }
}