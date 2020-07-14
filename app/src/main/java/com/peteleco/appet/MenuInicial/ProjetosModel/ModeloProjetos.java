package com.peteleco.appet.MenuInicial.ProjetosModel;

import android.widget.ProgressBar;

public class ModeloProjetos {
    private String nomeProjeto;


    public ModeloProjetos(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }
    public ModeloProjetos() {
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }
}
