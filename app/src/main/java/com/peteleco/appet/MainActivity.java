package com.peteleco.appet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button irCadastrar, logar, esqSenha, enter;
    private EditText login;
    private EditText senha;

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

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

    private void signIn(String email, String password) {

        Log.d(TAG, "signIn:" + email);

        if (!validateForm(email, password)) {
            return;
        }
//        showProgressBar();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);

                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",

                                    Toast.LENGTH_SHORT).show();

                            updateUI(null);

                            // [START_EXCLUDE]

                            checkForMultiFactorFailure(task.getException());

                            // [END_EXCLUDE]

                        }


                        // [START_EXCLUDE]

                        if (!task.isSuccessful()) {

                            mStatusTextView.setText(R.string.auth_failed);

                        }

                        hideProgressBar();

                        // [END_EXCLUDE]

                    }

                });

        // [END sign_in_with_email]

    }

    private boolean validateForm(String login, String senha) {
        // TODO: validação dos dados do usuário
        // Exemplo: senha com 8 caracteres, e-mail contém @, etc

//        String email = mEmailField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            campo_email.setError("Este campo é obrigatório");
            campo_email.requestFocus();
            return false;
        } else {
            campo_email.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            campo_senha.setError("Este campo é obrigatório");
            campo_senha.requestFocus();
            return false;
        } else {
            campo_senha.setError(null);
        }
        return true;
    }
}
