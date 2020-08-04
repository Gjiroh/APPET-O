package com.peteleco.appet.ProjetoEspecifico.MenuInicial;


public class Tarefa {
    public String descricao;
    public String responsavel;
    public String prazo;
    public String nome;
    private String TAG = "Tarefas.java";

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Tarefa() {
        descricao = "";
        prazo = "";
        responsavel = "";
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getPrazo() {
        return prazo;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }
}
