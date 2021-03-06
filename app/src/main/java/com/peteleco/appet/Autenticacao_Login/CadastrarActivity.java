package com.peteleco.appet.Autenticacao_Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.R;

public class CadastrarActivity extends AppCompatActivity {

    private Button cadastrar;
    private static final String TAG = "teste";
    private EditText campo_nome, campo_senha, campo_grr, campo_email, campo_cpf, campo_telefone;
    private boolean teste = false;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        getSupportActionBar().setTitle("Cadastro");

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

//                Log.i(TAG, "test: "+teste);
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

        if (TextUtils.isEmpty(nome)) {
            campo_nome.setError("Este campo é obrigatório");
            campo_nome.requestFocus();
            return false;
        }else {
            campo_nome.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            campo_senha.setError("Este campo é obrigatório");
            campo_senha.requestFocus();
            return false;
        } else if (password.length() < 6) {
            campo_senha.setError("A senha deve ter, no mínimo, 6 caracteres");
            campo_senha.requestFocus();
            return false;
        } else {
            campo_senha.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            campo_email.setError("Este campo é obrigatório");
            campo_email.requestFocus();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campo_email.setError("Digite um formato de e-mail válido");
            campo_email.requestFocus();
            return false;
        }else {
            campo_email.setError(null);
        }

        if (TextUtils.isEmpty(cpf)) {
            campo_cpf.setError("Este campo é obrigatório");
            campo_cpf.requestFocus();
            return false;
        }else if (cpf.length() != 11) {
            campo_cpf.setError("O CPF deve ter 11 caracteres, exemplo: 12345678900");
            campo_cpf.requestFocus();
            return false;
        }else {
            campo_cpf.setError(null);
        }

        if (TextUtils.isEmpty(telefone)) {
            campo_telefone.setError("Este campo é obrigatório");
            campo_telefone.requestFocus();
            return false;
        }else if  (telefone.length() != 11) { //41 9 9999 9999 ou 041 3333-3333
            campo_telefone.setError("O Telefone deve ser um numero válido," +
                    " exemplo: 41999887766 ou 4133223322");
            campo_telefone.requestFocus();
            return false;
        }else {
            campo_telefone.setError(null);
        }

        if (TextUtils.isEmpty(grr)) {
            campo_grr.setError("Este campo é obrigatório");
            campo_grr.requestFocus();
            return false;
        }else if (grr.length() != 8) {
            campo_grr.setError("O GRR deve ter 8 caracteres, exemplo: 20209999");
            campo_grr.requestFocus();
            return false;
        }else {
            campo_grr.setError(null);
        }

        return true;
    }

    private void createAccount(final String email, String password, final String nome, final String grr, final String cpf, final String telefone) {
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
                                assert user != null;
                                user.sendEmailVerification();
                                Toast.makeText(CadastrarActivity.this, " Cadastro realizado com sucesso! ",
                                        Toast.LENGTH_SHORT).show();
                                teste = true;
//                                Log.i(TAG, "test task.isSuccessful: "+teste);
                                finish();
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
                                    Log.e(TAG, "Erro: " + e.getMessage());
                                    Toast.makeText(CadastrarActivity.this, "Autenticação falhou. Procure ajuda! ",
                                            Toast.LENGTH_SHORT).show();
                                }

                                updateUI(null);
                            }

                            // [START_EXCLUDE]
                            hideProgressBar();
                            // [END_EXCLUDE]

                            // Verifica se o cadastro foi realizado com sucesso (atraves de "teste")
                            // e cadastra o nome do usuario na listaNome e em users no Banco de Dados
                            if (teste){
                                User user = new User(nome, email, cpf, telefone, grr);
                                mDatabase.child("users").push().setValue(user);
                            }
                        }
                    });

            // [END create_user_with_email]
        } catch (Exception e) {
            e.printStackTrace();
        }
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

