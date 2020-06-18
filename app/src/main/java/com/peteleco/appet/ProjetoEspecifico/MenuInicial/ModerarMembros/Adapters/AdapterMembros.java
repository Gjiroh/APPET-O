package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peteleco.appet.R;
import com.peteleco.appet.bancoDados;

import java.util.List;

public class AdapterMembros extends RecyclerView.Adapter<AdapterMembros.MyViewHolder> {
    private List<String> listaMembros;
    private SharedPreferences preferences;
    private final static String TAG = "AdapterMembros";
    public String getNome;
    Context context;

    public AdapterMembros(List<String> listaMembros, Context context) {
        this.listaMembros = listaMembros;
        preferences = context.getSharedPreferences("Dados", 0);
        preferences.edit().putInt("sizeNomeSelec", listaMembros.size()).apply();
        this.context = context;
    }

    public String getGetNome() {
        return getNome;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_alterar_membros, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nomeMembro.setText(listaMembros.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                     preferences.edit().putString("nomeSelec"+position, holder.nomeMembro.getText().toString()).apply();
                     getNome = holder.nomeMembro.getText().toString();
                    Toast.makeText(context, "Nome selecionado: "+ getNome, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        preferences.edit().remove("nomeSelec"+position).apply();
                        Toast.makeText(context, "Nome descelecionado: " + getNome, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "Erro ao remover.");
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMembros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nomeMembro;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeMembro = itemView.findViewById(R.id.textViewNomeMembro);
            checkBox = itemView.findViewById(R.id.checkBoxMembro);
        }
    }


}
