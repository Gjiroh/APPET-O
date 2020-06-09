package com.peteleco.appet.Autenticacao_Login;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    public String nome;
    public String email;
    public String cpf;
    public String telefone;
    public String grr;
    List<String> listaNomes = new ArrayList<>();
    private DatabaseReference reference;
    private SharedPreferences preferences;
    private static final String TAG = "teste";


    public User(DatabaseReference reference, SharedPreferences preferences) {
        this.reference = reference;
        this.preferences = preferences;
    }

    public User(String nome, String email, String cpf, String telefone, String grr) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.grr = grr;
    }

    public void nomesMembros() {

        this.reference.addValueEventListener(new ValueEventListener() {
            final String NOME = "listaNome";
            int i = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double aux = Double.parseDouble(dataSnapshot.child(NOME).child("contador").getValue().toString());
                Log.i(TAG,"aux = "+aux);
                int cont = (int) aux;
                for ( i = 0 ; i < cont; i+=1 ) {
                    String s = String.valueOf(i);
                    listaNomes.add(dataSnapshot.child(NOME).child(s).getValue().toString());
                }
                Log.i(TAG, " i = "+ i);
                //Set the values
                Set<String> set = new HashSet<>(listaNomes);
                Log.i(TAG, "User: "+ set);
                // Salvando os nomes dos membros em uma SharedPreference
                preferences.edit().putStringSet("nomes", set).apply();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Teste", "Deu Ruim");
            }
        });
    }
}
