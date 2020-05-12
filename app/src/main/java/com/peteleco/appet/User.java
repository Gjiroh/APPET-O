package com.peteleco.appet;

public class User {
    public String nome;
    public String senha;
    public String email;
    public String cpf;
    public String telefone;
    public String grr;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String nome, String senha, String email, String cpf, String telefone) {
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public User(String nome, String senha, String email, String cpf, String telefone, String grr) {
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.grr = grr;
    }

}
