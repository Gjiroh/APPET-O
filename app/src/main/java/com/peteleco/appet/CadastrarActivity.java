package com.peteleco.appet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastrarActivity extends AppCompatActivity {

    private Button cadastrar;
    private static final String TAG = "CadastrarActivity";
    private EditText campo_nome, campo_senha, campo_grr, campo_email, campo_cpf, campo_telefone;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        mAuth = FirebaseAuth.getInstance();

        cadastrar = findViewById(R.id.btCadastro);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                campo_nome = findViewById(R.id.editNome);
                String pegarNome = campo_nome.getText().toString();

                campo_senha = findViewById(R.id.editSenha);
                String pegarSenha = campo_senha.getText().toString();

                campo_grr = findViewById(R.id.editGRR);
                String pegarGRR = campo_grr.getText().toString();

                campo_email = findViewById(R.id.editEmail);
                String pegarEmail = campo_email.getText().toString();

                campo_cpf = findViewById(R.id.editCPF);
                String pegarCPF = campo_cpf.getText().toString();

                campo_telefone = findViewById(R.id.editTel);
                String pegarTelefone = campo_telefone.getText().toString();

                createAccount(pegarEmail, pegarSenha, pegarNome, pegarGRR, pegarCPF, pegarTelefone);

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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

    private boolean validateForm(String email, String password, String nome, String grr, String cpf, String telefone) {
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

    private void createAccount(String email, String password, String nome, String grr, String cpf, String telefone) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email, password, nome, grr, cpf, telefone)) {
            return;
        }
        showProgressBar();
        try {
            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Toast.makeText(CadastrarActivity.this, " Cadastro realizado com sucesso! ",
                                        Toast.LENGTH_SHORT).show();
//                                confirma_email();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    campo_senha.setError("A senha deve ter pelo menos 6 caracteres!");
                                    campo_senha.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    campo_email.setError("Verifique se o endereço de e-mail está correto");
                                    campo_email.requestFocus();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    campo_email.setError("Usuário já existe");
                                    campo_email.requestFocus();
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(CadastrarActivity.this, "Autenticação falhou. Procure ajuda! ",
                                            Toast.LENGTH_SHORT).show();
                                }

                                updateUI(null);
                            }

                            // [START_EXCLUDE]
                            hideProgressBar();
                            // [END_EXCLUDE]
                        }
                    });
                User user = new User(nome, password, email, cpf, telefone);

                mDatabase.child("users").child("userId").setValue(user);
            // [END create_user_with_email]
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  TODO: enviar link de confirmação do e-mail

    }

    // desloga o usuário
    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void showProgressBar() {
        View dados_cadastro;
        dados_cadastro = findViewById(R.id.dados_cadastro);

        View progressBar;
        progressBar = findViewById(R.id.progressBarCadastro);

        dados_cadastro.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        View dados_cadastro;
        dados_cadastro = findViewById(R.id.dados_cadastro);

        View progressBar;
        progressBar = findViewById(R.id.progressBarCadastro);

        dados_cadastro.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
