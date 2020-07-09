package com.peteleco.appet.ProjetoEspecifico.MenuInicial.AdicionarTarefa;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ModerarMembros.Adapters.AdapterMembros;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main.PlaceholderFragment;
import com.peteleco.appet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdicionarTarefaActivity extends AppCompatActivity {
    private String nomeProjeto, pattern, prazoText;
    private EditText nomeTarefa, descricao;
    private TextView prazo;
    private Button btSalvar;
    private List<String> responsaveis;
    private RecyclerView recyclerView;
    private AdapterMembros adapter;
    private SharedPreferences preferences;
    private SimpleDateFormat sdf;
    private DatePickerDialog.OnDateSetListener mDateSet;
    private Date date;

    private final static String TAG = "AdicionarTarefaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
        nomeProjeto = getSharedPreferences("Activity", 0).getString("nomeProjeto",null);

        preferences = getSharedPreferences("Dados", 0);

        // Configure o SimpleDateFormat no onCreate ou onCreateView
        pattern = "dd/MM/yy";
        sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);

        nomeTarefa = findViewById(R.id.editTextNomeTarefa);
        descricao = findViewById(R.id.editTextDescricaoTarefa);
        recyclerView = findViewById(R.id.recyclerViewMembros);
        prazo = findViewById(R.id.textViewData);
        prazoText = prazo.getText().toString();

        mDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mes = month+1;
                prazoText = dayOfMonth+"/"+mes+"/"+year;
                prazo.setText(prazoText);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    date = sdf.parse(prazoText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        prazo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AdicionarTarefaActivity.this,
                        mDateSet,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        getSupportActionBar().setTitle("Nova Tarefa");

        final List<String> listaMembros = new ArrayList<>(preferences.getStringSet("nomeMembroPE", null));
        Collections.sort(listaMembros);


        // Adapter
        adapter = new AdapterMembros(listaMembros);

        // Configurando o RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        responsaveis = adapter.listaMembrosSelec;

        btSalvar = findViewById(R.id.buttonSalvarTarefa);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auxNome = nomeTarefa.getText().toString();
                String auxDesc = descricao.getText().toString();
                if (verificarDados(auxNome, auxDesc, prazoText, responsaveis)) {
                    salvarTarefa(auxNome, auxDesc, prazoText, responsaveis);
                    Log.i(TAG, "respponsáveis: "+ responsaveis);
                }

            }
        });
    }

    public boolean verificarDados (
            String nomeTarefa, String descricao, String prazo, List<String> responsaveis
    ) {

        List<String> listD = new ArrayList<>(preferences.getStringSet("listaTarefasDONE", null));
        List<String> listTD = new ArrayList<>(preferences.getStringSet("listaTarefasTO DO", null));
        List<String> listDNG = new ArrayList<>(preferences.getStringSet("listaTarefasDOING", null));
        List<String> listI = new ArrayList<>(preferences.getStringSet("listaTarefasIDEIA", null));
        List<String> tarefas = new ArrayList<>();
        tarefas.addAll(listD);
        //tarefas.addAll(listI); //Acho q podem existir tarefas e ideias com o mesmo nome
        tarefas.addAll(listDNG);
        tarefas.addAll(listTD);

        boolean isRepeat = false;
        for (int i = 0; i < tarefas.size(); i++) {
            if (nomeTarefa.equals(tarefas.get(i))){
                isRepeat = true;
            }
        }

        if (nomeTarefa.isEmpty()){
            this.nomeTarefa.requestFocus();
            this.nomeTarefa.setError("A tarefa deve ter um nome");
            return false;
        } else if (isRepeat) {
            this.nomeTarefa.requestFocus();
            this.nomeTarefa.setError("Nomde de Tarefa em uso");
            return false;
        } else {
            this.nomeTarefa.setError(null);
        }

        if (descricao.isEmpty()){
            this.descricao.requestFocus();
            this.descricao.setError("A tarefa deve ter uma descrição");
            return false;
        } else {
            this.descricao.setError(null);
        }

        Date currentTime = new Date();
        int month = Calendar.getInstance().getTime().getMonth() + 1;
        int year = Calendar.getInstance().getTime().getYear() + 1900;
        String aux = Calendar.getInstance().getTime().getDate()+"/"+month+"/"+year;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            currentTime = sdf.parse(aux);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: corrigir verificação de data, ela esta aceitando datas anteriores ao dia de hoje
        if (prazoText.equals("Selecione a data do prazo")){
            this.prazo.requestFocus();
            Toast.makeText(this, "Insira uma data", Toast.LENGTH_SHORT).show();
            return false;
        } else if (date.compareTo(currentTime) < 0) {
            this.prazo.requestFocus();
            Toast.makeText(this, "Não tem como o prazo ser pra ontem... :D", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            this.prazo.setError(null);
        }

        if (responsaveis.isEmpty()) {
            this.recyclerView.requestFocus();
            Toast.makeText(this, "A tarefa deve ter ao menos um responsável", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void salvarTarefa (String nomeTarefa, String descricao, String prazo, List<String> responsavel) {
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("testeProjetos/"+nomeProjeto+"/TO DO");
            reference.child(nomeTarefa).child("descricao").setValue(descricao);
            reference.child(nomeTarefa).child("prazo").setValue(prazo);
            reference.child(nomeTarefa).child("responsavel").setValue(responsavel);
            preferences.edit().putBoolean("ReiniciarVerify", false).apply();
            Toast.makeText(this, "Tarefa salva", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e){
            Toast.makeText(this, "Houve um erro ao tentar salvar a tarefa", Toast.LENGTH_SHORT).show();
        }

    }

}
