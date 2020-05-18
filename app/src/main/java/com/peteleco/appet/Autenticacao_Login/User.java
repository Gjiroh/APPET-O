package com.peteleco.appet.Autenticacao_Login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String nome;
//    public String senha;
    public String email;
    public String cpf;
    public String telefone;
    public String grr;
    List<String> listaNomes = new ArrayList<>();
    private DatabaseReference reference;

    public User(DatabaseReference reference) {
        this.reference = reference;
    }

    public User(String nome, String email, String cpf, String telefone, String grr) {
        this.nome = nome;
//        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.grr = grr;
    }

    public List<String> nomesMembros() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        // TODO: Não esta retornando a listaNomes, tem algo de errado
        this.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaNomes.add(dataSnapshot.child("-M7T3Spd8_VcTLzBqJJJ")
                        .child("nome").getValue().toString()); // Gabriel Jiro
                listaNomes.add(dataSnapshot.child("-M7NoNe1P0ZkCNbCuNbY")
                        .child("nome").getValue().toString()); // João Turra
                Log.i("Teste", "Lista User: "+listaNomes.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Teste", "Deu RUim");
            }
        });
        Log.i("Teste", "Lista User2: "+listaNomes.toString());
        return listaNomes;


    }
}
