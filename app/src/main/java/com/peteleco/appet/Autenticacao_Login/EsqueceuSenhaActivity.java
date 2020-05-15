package com.peteleco.appet.Autenticacao_Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.peteleco.appet.R;

public class EsqueceuSenhaActivity extends AppCompatActivity {
    private Button btNovaSenha;
    private EditText campoCpf, campoEmail;
    private FirebaseAuth mAuth;
    private TextView textoCpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        btNovaSenha = findViewById(R.id.btRecuperarSenha);
        campoCpf = findViewById(R.id.editTextCPF);
        campoEmail = findViewById(R.id.editTextE);
        textoCpf = findViewById(R.id.textEsqCPF);

        mAuth = FirebaseAuth.getInstance();

        //TODO: depois tirar isso
        campoCpf.setVisibility(View.GONE);
        textoCpf.setVisibility(View.GONE);

        btNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //mAuth é null
                    // TODO: Precisa verificar se o e-mail já esta cadastrado...
                    mAuth.sendPasswordResetEmail(campoEmail.getText().toString());
                    Toast.makeText(EsqueceuSenhaActivity.this, "Um e-mail foi enviado para uma nova senha",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EsqueceuSenhaActivity.this, "Alguma coisa deu errado",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
