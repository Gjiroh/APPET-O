package com.peteleco.appet.Pautas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.peteleco.appet.R;

public class PautasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pautas);
        getSupportActionBar().setTitle("Pautas");

        // TODO: Fazer tudo referente a mostrar pautas. Pensar em um meio de organizar por pastas e
        //  por datas...
    }
}
