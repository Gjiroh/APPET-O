package com.peteleco.appet.InformacaoPessoal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

public class InformacaoPessoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao_pessoal);

        bancoDados bancoDados = new bancoDados(this.getApplicationContext());
        // TODO: tirar comentário abaixo
        /*CharSequence nomeLogado = bancoDados.getInfoNomeLogado("nomeLogado");
        getSupportActionBar().setTitle(nomeLogado);*/
    }
}
