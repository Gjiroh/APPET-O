package com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste;

import android.widget.CheckBox;

public class ModelTeste {
    String nomeColab;

    public ModelTeste(String nomeColab) {
        this.nomeColab = nomeColab;
    }

    public String getNomeColab() {
        return nomeColab;
    }

    public void setNomeColab(String nomeColab) {
        this.nomeColab = nomeColab;
    }
}
