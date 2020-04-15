package com.peteleco.appet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button irCadastrar, logar, esqSenha, enter;
    private EditText campo_login;
    private EditText campo_senha;

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private boolean sucesso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"Criado MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        mAuth = FirebaseAuth.getInstance();

        irCadastrar = findViewById(R.id.bt_criarConta);
        irCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Passando para Activity de cadastro
                Intent intent = new Intent(getApplicationContext(), CadastrarActivity.class);
                startActivity(intent);
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
                campo_login = findViewById(R.id.edt_login);
                String loginSalvar = campo_login.getText().toString();


                campo_senha = findViewById(R.id.edt_password);
                String senhaSalvar = campo_senha.getText().toString();

                signIn(loginSalvar, senhaSalvar);

                Log.d(TAG, "botão clicado");
//                Intent intencao2 = new Intent(getApplicationContext(), FragmentsActivity.class);
//                startActivity(intencao2);

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
                            Intent intencao2 = new Intent(getApplicationContext(), FragmentsActivity.class);
                            startActivity(intencao2);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Autenticação falhou.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            // [START_EXCLUDE]
//                            checkForMultiFactorFailure(task.getException());
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
                        }
//                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm(String login, String senha) {
        // TODO: validação dos dados do usuário
        // Exemplo: senha com 8 caracteres, e-mail contém @, etc

//        String email = mEmailField.getText().toString();

        if (TextUtils.isEmpty(login)) {
            campo_login.setError("Este campo é obrigatório");
            campo_login.requestFocus();
            return false;
        } else {
            campo_login.setError(null);
        }

        if (TextUtils.isEmpty(senha)) {
            campo_senha.setError("Este campo é obrigatório");
            campo_senha.requestFocus();
            return false;
        } else {
            campo_senha.setError(null);
        }
        return true;
    }

    private void updateUI(FirebaseUser user) {
//        hideProgressBar();
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

//            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
//            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

            if (user.isEmailVerified()) {
//                findViewById(R.id.verifyEmailButton).setVisibility(View.GONE);
            } else {
//                findViewById(R.id.verifyEmailButton).setVisibility(View.VISIBLE);
            }
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);

//            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signedInButtons).setVisibility(View.GONE);

        }
    }
}
