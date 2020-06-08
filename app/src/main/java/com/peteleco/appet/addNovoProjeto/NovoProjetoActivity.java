package com.peteleco.appet.addNovoProjeto;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.R;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.Adapter.AdapterTeste;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NovoProjetoActivity extends AppCompatActivity {

    private EditText nomeProjetoNovo, descriçãoProjetoNovo;
    private RecyclerView mostrarColabs;
    private List<ModelTeste> listaColabs = new ArrayList<>();
    private String TAG = "NovoProjeto";
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Adicionar o evento de clique do botão "Criar Projeto", recuperar os dados inseridos/selecioandos e Adicionar o Projeto ao bd
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_projeto);
        mostrarColabs = findViewById(R.id.RecyclerTeste);

        //Listar colaboradores
        Colaboradores();

        //Adapter
        AdapterTeste adapterTeste = new AdapterTeste(listaColabs);

        // Configurando o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mostrarColabs.setLayoutManager(layoutManager);
        mostrarColabs.setHasFixedSize(true);
        mostrarColabs.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));
        mostrarColabs.setAdapter(adapterTeste);
    }

    public void Colaboradores() {
        SharedPreferences preferences = getSharedPreferences("Nomes",0);
        User user = new User(this.reference, preferences);
        user.nomesMembros();

        // Recuperando o nome dos membros que foram salvos
        Set<String> set = preferences.getStringSet("nomes", null);

        // Converção do set<String> para uma List<String>
        int n = set.size();
        List<String> listaNomes = new ArrayList<String>(n);
        for (String x : set)
            listaNomes.add(x);
        Log.i("Teste", "Lista Nomes: "+listaNomes.toString());

        // Adicionando o nome dos membros para serem mostrados na tela do usuário
        int aux = 0;
        while (aux < listaNomes.size()){
            ModelTeste Colab = new ModelTeste(listaNomes.get(aux));
            this.listaColabs.add(Colab);
            aux += 1;
        }

        // Limpando a SharedPreferences do usuário para não ficar ocupando espaço na memória
        preferences.edit().remove("nomes").apply();

    }
}
