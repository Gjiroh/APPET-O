package com.peteleco.appet.ProjetoEspecifico.MenuInicial.DetalheTarefas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.AdicionarTarefa.AdicionarTarefaActivity;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.Adapters.AdapterMembros;
import com.peteleco.appet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetalhesTarefaActivity extends AppCompatActivity {
    private EditText ednomeTarefa, eddescricao;
    private TextView twprazo;
    private RecyclerView recyclerViewResp;
    private AdapterMembros adapter;
    private Button btSalvar;

    private String nomeTarefa, prazo, descricao, responsaveis, pattern;

    private SimpleDateFormat sdf;
    private DatePickerDialog.OnDateSetListener mDateSet;
    private Date date;

    private DatabaseReference reference;
    private SharedPreferences preferences;

    private static final String TAG = "DetalhesTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_tarefa);

        reference = FirebaseDatabase.getInstance().getReference("testeProjetos");
        preferences = getSharedPreferences("Dados", 0);

        ednomeTarefa = findViewById(R.id.editTextShowTaskName);
        eddescricao = findViewById(R.id.editTextShowDescription);
        twprazo = findViewById(R.id.textViewShowPrazo);
        btSalvar = findViewById(R.id.buttonSalvarAlteracoesTask);
        recyclerViewResp = findViewById(R.id.recyclerViewResponsaveis);

        pattern = "dd/MM/yy";
        sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);

        nomeTarefa = getIntent().getExtras().getString("nomeTarefa","Erro ao carregar dado");
        prazo = getIntent().getExtras().getString("prazo","Erro ao carregar dado");
        descricao = getIntent().getExtras().getString("descricao","Erro ao carregar dado");
        responsaveis = getIntent().getExtras().getString("nomeResponsavel","Erro ao carregar dado");

        getSupportActionBar().setTitle(nomeTarefa);

        ednomeTarefa.setText(nomeTarefa);
        eddescricao.setText(descricao);
        twprazo.setText(prazo);

        mDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mes = month+1;
                prazo = dayOfMonth+"/"+mes+"/"+year;
                twprazo.setText(prazo);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    date = sdf.parse(prazo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        twprazo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DetalhesTarefaActivity.this,
                        mDateSet,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        List<String> listaMembros = new ArrayList<>(preferences.getStringSet("nomeMembroPE", null));
        Collections.sort(listaMembros);

        // Adapter
        adapter = new AdapterMembros(listaMembros);

        // Configurando o RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewResp.setLayoutManager(layoutManager);
        recyclerViewResp.setHasFixedSize(true);
        recyclerViewResp.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerViewResp.setAdapter(adapter);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeTarefaAt = ednomeTarefa.getText().toString();
                descricao = eddescricao.getText().toString();

                List<String> aux = adapter.listaMembrosSelec;
                int size = aux.size();
                Collections.sort(aux);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < size; i++){
                    if (i == size-1){
                        builder.append(aux.get(i));
                    } else {
                        builder.append(aux.get(i)).append(", ");
                    }
                    responsaveis = builder.toString();
                }

                boolean validar = verificarDados(nomeTarefaAt, descricao, prazo, responsaveis);
                if (validar) {
                    atualizarTarefa(nomeTarefaAt, descricao, prazo, responsaveis);
                }
            }
        });

    }

    public boolean verificarDados (
            String nomeTarefa, String descricao, String prazo, String responsaveis
    ) {

        List<String> listD = new ArrayList<>(preferences.getStringSet("listaTarefasDONE", null));
        List<String> listTD = new ArrayList<>(preferences.getStringSet("listaTarefasTO DO", null));
        List<String> listDNG = new ArrayList<>(preferences.getStringSet("listaTarefasDOING", null));
        List<String> tarefas = new ArrayList<>();
        tarefas.addAll(listD);
        tarefas.addAll(listDNG);
        tarefas.addAll(listTD);
        tarefas.remove(nomeTarefa);
        boolean isRepeat = false;
        for (int i = 0; i < tarefas.size(); i++) {
            if (nomeTarefa.equals(tarefas.get(i))){
                isRepeat = true;
            }
        }

        if (nomeTarefa.isEmpty()){
            this.ednomeTarefa.requestFocus();
            this.ednomeTarefa.setError("A tarefa deve ter um nome");
            return false;
        } else if (isRepeat) {
            this.ednomeTarefa.requestFocus();
            this.ednomeTarefa.setError("Nomde de Tarefa em uso");
            return false;
        } else {
            this.ednomeTarefa.setError(null);
        }

        if (descricao.isEmpty()){
            this.eddescricao.requestFocus();
            this.eddescricao.setError("A tarefa deve ter uma descrição");
            return false;
        } else {
            this.eddescricao.setError(null);
        }

        Date currentTime = new Date();
        int month = Calendar.getInstance().getTime().getMonth() + 1;
        int year = Calendar.getInstance().getTime().getYear() + 1900;
        String aux = Calendar.getInstance().getTime().getDate()+"/"+month+"/"+year;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            currentTime = sdf.parse(aux);
            date = sdf.parse(prazo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (prazo.equals("Selecione a data do prazo")){
            this.twprazo.requestFocus();
            Toast.makeText(this, "Insira uma data", Toast.LENGTH_SHORT).show();
            return false;
        } else if (date.compareTo(currentTime) < 0) {
            this.twprazo.requestFocus();
            Toast.makeText(this, "Não tem como o prazo ser pra ontem... :D", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            this.twprazo.setError(null);
        }

        if (responsaveis.equals("")) {
            this.recyclerViewResp.requestFocus();
            Toast.makeText(this, "A tarefa deve ter ao menos um responsável", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void atualizarTarefa (String nomeTarefa, String descricao, String prazo, String responsavel) {
        showProgressBar();
        try {
            reference.child(this.nomeTarefa).removeValue();
            String nomeProjeto = getIntent().getStringExtra("nomeProjeto");
            String status = getIntent().getStringExtra("status");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("testeProjetos/"+nomeProjeto+"/"+status);
            reference.child(nomeTarefa).child("descricao").setValue(descricao);
            reference.child(nomeTarefa).child("prazo").setValue(prazo);
            reference.child(nomeTarefa).child("responsavel").setValue(responsavel);
            preferences.edit().putString("ReiniciarVerify", "reiniciar").apply();
            Toast.makeText(this, "Tarefa atualizada", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e){
            Toast.makeText(this, "Houve um erro ao tentar salvar a tarefa", Toast.LENGTH_SHORT).show();
        }
        hideProgressBar();
    }

    private void showProgressBar() {
        View dados_tarefa;
        dados_tarefa = findViewById(R.id.linearLayoutAtualizarTarefa);

        View progressBar;
        progressBar = findViewById(R.id.progressBarAtualizarTarefa);

        dados_tarefa.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        View dados_tarefa;
        dados_tarefa = findViewById(R.id.linearLayoutAtualizarTarefa);

        View progressBar;
        progressBar = findViewById(R.id.progressBarAtualizarTarefa);

        dados_tarefa.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}