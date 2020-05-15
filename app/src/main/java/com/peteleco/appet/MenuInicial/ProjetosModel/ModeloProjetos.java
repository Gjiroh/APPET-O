package com.peteleco.appet.MenuInicial.ProjetosModel;

import android.widget.ProgressBar;

public class ModeloProjetos {
    private String nomeProjeto;
    private ProgressBar andamentoAtividades;


    public ModeloProjetos(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
        this.andamentoAtividades = andamentoAtividades;
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    public ProgressBar getAndamentoAtividades() {
        return andamentoAtividades;
    }

    public void setAndamentoAtividades(ProgressBar andamentoAtividades) {
        this.andamentoAtividades = andamentoAtividades;
    }
}
