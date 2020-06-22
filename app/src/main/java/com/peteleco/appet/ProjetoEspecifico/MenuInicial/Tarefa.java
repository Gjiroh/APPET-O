package com.peteleco.appet.ProjetoEspecifico.MenuInicial;

import java.util.ArrayList;
import java.util.List;

public class Tarefa {
    public String descricao;
    public List<String> responsavel;
    public String prazo;
    public String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Tarefa() {
        descricao = "";
        prazo = "";
        responsavel = new ArrayList<>();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(List<String> responsavel) {
        this.responsavel = responsavel;
    }

    public String getPrazo() {
        return prazo;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }
}
