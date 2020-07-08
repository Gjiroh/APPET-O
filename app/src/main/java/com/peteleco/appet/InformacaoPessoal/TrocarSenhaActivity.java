package com.peteleco.appet.InformacaoPessoal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

public class TrocarSenhaActivity extends AppCompatActivity {

    private String email;
    private EditText senhaAtual, novaSenha, confirNSenha;
    private Button btConfirmar;
    private AlertDialog.Builder builder;

    private final static String TAG = "TrocarSenhaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trocar_senha);

        senhaAtual = findViewById(R.id.editTextSenhaAtualTS);
        novaSenha = findViewById(R.id.editTextNovaSenhaTS);
        confirNSenha = findViewById(R.id.editTextConfirmNovaSenhaTS);
        btConfirmar = findViewById(R.id.buttonTrocarSenha);
        builder = new AlertDialog.Builder(this);

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm(senhaAtual,novaSenha,confirNSenha)){
                    builder.setMessage("Voce tem certeza que deseja alterar sua senha?")
                            .setTitle("Alerta");
                    builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alterarSenha(senhaAtual.getText().toString(), novaSenha.getText().toString());
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

    private boolean validateForm(EditText senhaAtual, EditText novaSenha, EditText confirNSenha) {

        String auxSA, auxNS, auxCNS;
        auxSA = senhaAtual.getText().toString();
        auxNS = novaSenha.getText().toString();
        auxCNS = confirNSenha.getText().toString();

        if (TextUtils.isEmpty(auxSA)) {
            this.senhaAtual.setError("Este campo é obrigatório");
            this.senhaAtual.requestFocus();
            return false;
        }else if (auxSA.length() < 6) {
            this.senhaAtual.setError("Senha deve conter ao menos 6 digitos");
            this.senhaAtual.requestFocus();
            return false;
        }else {
            this.senhaAtual.setError(null);
        }

        if (TextUtils.isEmpty(auxNS)) {
            this.novaSenha.setError("Este campo é obrigatório");
            this.novaSenha.requestFocus();
            return false;
        }else if (auxNS.length() < 6) {
            this.novaSenha.setError("Senha deve conter ao menos 6 digitos");
            this.novaSenha.requestFocus();
            return false;
        }else if( auxSA.equals(auxNS) ){
            this.novaSenha.setError("A nova senha deve ser diferente da anterior");
            this.novaSenha.requestFocus();
        }else {
            this.novaSenha.setError(null);
        }

        if (TextUtils.isEmpty(auxCNS)) {
            this.confirNSenha.setError("Este campo é obrigatório");
            this.confirNSenha.requestFocus();
            return false;
        }else if (!auxCNS.equals(auxNS)) {
            this.confirNSenha.setError("Este campo deve ser identido ao anterior");
            this.confirNSenha.requestFocus();
            return false;
        }else {
            this.confirNSenha.setError(null);
        }

        return true;
    }

    private void alterarSenha (String senhaAtual, final String novaSenha) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        Get auth credentials from the user for re-authentication. The example below shows
//        email and password credentials but there are multiple possible providers,
//        such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), senhaAtual);

//        Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(novaSenha).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(TrocarSenhaActivity.this, "" +
                                                "Senha atualizada com sucesso", Toast.LENGTH_SHORT).show();
                                        bancoDados bancoDados = new bancoDados(getApplicationContext());
                                        bancoDados.loadNomeLogado(user.getEmail());
                                        finish();
                                    } else {
                                        Toast.makeText(TrocarSenhaActivity.this, "" +
                                                "Erro ao atualizar a senha", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(TrocarSenhaActivity.this, "" +
                                    "Erro ao acessar credenciais. Por favor, verifique a senha " +
                                    "inserida.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }
}
