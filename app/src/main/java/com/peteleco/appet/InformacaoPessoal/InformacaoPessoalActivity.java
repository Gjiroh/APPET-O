package com.peteleco.appet.InformacaoPessoal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

public class InformacaoPessoalActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private bancoDados bancoDados;
    private EditText nome,CPF,GRR,telefone,email;
    private Button btSalvar;
    private static final String TAG = "InfoPessoalActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info_pessoal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_trocar_senha:
                // TODO: Inserir método para trocar de senha
                Intent intentSenha = new Intent(getApplicationContext(), TrocarSenhaActivity.class);
                startActivity(intentSenha);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao_pessoal);

        builder = new AlertDialog.Builder(this);

        bancoDados = new bancoDados(this.getApplicationContext());

        nome = findViewById(R.id.editTextNomeUser);
        CPF = findViewById(R.id.editTextCPFUser);
        GRR = findViewById(R.id.editTextGRRUser);
        telefone = findViewById(R.id.editTextTelefoneUser);
        email = findViewById(R.id.editTextEmailUser);
        btSalvar = findViewById(R.id.buttonSalvarDados);

        CharSequence nomeLogado = bancoDados.getInfoNomeLogado("nomeLogado");
        getSupportActionBar().setTitle(nomeLogado);

        nome.setText(bancoDados.getInfoNomeLogado("nomeLogado"));
        CPF.setText(bancoDados.getInfoNomeLogado("nomeLogadoCPF"));
        GRR.setText(bancoDados.getInfoNomeLogado("nomeLogadoGRR"));
        telefone.setText(bancoDados.getInfoNomeLogado("nomeLogadoTelefone"));
        email.setText(bancoDados.getInfoNomeLogado("nomeLogadoEmail"));

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String auxNome, auxCPF, auxGRR, auxTel, auxEmail;

                auxEmail = email.getText().toString();
                auxNome = nome.getText().toString();
                auxCPF = CPF.getText().toString();
                auxGRR = GRR.getText().toString();
                auxTel = telefone.getText().toString();

                if ( validateForm(auxEmail, auxNome, auxGRR, auxCPF, auxTel)) {

                    builder.setMessage("Voce tem certeza que deseja alterar e salvar os dados?")
                            .setTitle("Salvar");
                    builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User user = new User(auxNome, auxEmail, auxCPF, auxTel, auxGRR);
                            bancoDados.salvarDadosBD(user);
                            bancoDados.loadNomeLogado(auxEmail);
                            salvarEmailAlterado(auxEmail);
                            finish();
                        }
                    });
                    builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private boolean validateForm(String email, String nome, String grr, String cpf, String telefone) {

        String auxNome, auxCPF, auxGRR, auxTel, auxEmail;

        auxNome = bancoDados.getInfoNomeLogado("nomeLogado");
        auxCPF = bancoDados.getInfoNomeLogado("nomeLogadoCPF");
        auxGRR = bancoDados.getInfoNomeLogado("nomeLogadoGRR");
        auxTel = bancoDados.getInfoNomeLogado("nomeLogadoTelefone");
        auxEmail = bancoDados.getInfoNomeLogado("nomeLogadoEmail");

        if (TextUtils.isEmpty(nome)) {
            this.nome.setError("Este campo é obrigatório");
            this.nome.requestFocus();
            return false;
        }else {
            this.nome.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            this.email.setError("Este campo é obrigatório");
            this.email.requestFocus();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Digite um formato de e-mail válido");
            this.email.requestFocus();
            return false;
        } else {
            this.email.setError(null);
        }

        if (TextUtils.isEmpty(cpf)) {
            this.CPF.setError("Este campo é obrigatório");
            this.CPF.requestFocus();
            return false;
        }else if (cpf.length() != 11) {
            this.CPF.setError("O CPF deve ter 11 caracteres, exemplo: 12345678900");
            this.CPF.requestFocus();
            return false;
        }else {
            this.CPF.setError(null);
        }

        if (TextUtils.isEmpty(telefone)) {
            this.telefone.setError("Este campo é obrigatório");
            this.telefone.requestFocus();
            return false;
        }else if  (telefone.length() != 11) { //41 9 9999 9999 ou 041 3333-3333
            this.telefone.setError("O Telefone deve ser um numero válido," +
                    " exemplo: 41999887766 ou 4133223322");
            this.telefone.requestFocus();
            return false;
        }else {
            this.telefone.setError(null);
        }

        if (TextUtils.isEmpty(grr)) {
            this.GRR.setError("Este campo é obrigatório");
            this.GRR.requestFocus();
            return false;
        }else if (grr.length() != 8) {
            this.GRR.setError("O GRR deve ter 8 caracteres, exemplo: 20209999");
            this.GRR.requestFocus();
            return false;
        }else {
            this.GRR.setError(null);
        }

        /*if (
                !nome.equals(auxNome) || !grr.equals(auxGRR) || !cpf.equals(auxCPF) ||
                        !telefone.equals(auxTel) || !email.equals(auxEmail)
        ) {
            return true;
        }*/

        return true;
    }

    private void salvarEmailAlterado (String email) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updateEmail(email);
        } else {
            Toast.makeText(this, "Erro ao alterar o email", Toast.LENGTH_SHORT).show();
        }
    }
}
