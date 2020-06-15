package com.peteleco.appet.addNovoProjeto.RecyclerTeste.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.connection.util.StringListReader;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdapterTeste extends RecyclerView.Adapter<AdapterTeste.MyViewHolder> {

    final static private String TAG = "AdapterTeste.class";
    private List<ModelTeste> listaColab;
    public List<String> listaColabSelec = new ArrayList<>();

    public AdapterTeste(List<ModelTeste> listaColab) {
        this.listaColab = listaColab;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox nomeColab;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeColab = itemView.findViewById(R.id.checkBoxNomeColab);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_teste, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        ModelTeste colabs = listaColab.get(position);
        holder.nomeColab.setText(colabs.getNomeColab());

        holder.nomeColab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String s = holder.nomeColab.getText().toString();
                    listaColabSelec.add(s);
                }else{
                    String s = holder.nomeColab.getText().toString();
                    listaColabSelec.remove(s);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaColab.size();
    }

}
