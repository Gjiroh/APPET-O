package com.peteleco.appet.addNovoProjeto;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.Objects;

public class NovoProjetoActivity extends AppCompatActivity {

    private EditText nomeProjetoNovo, descriçãoProjetoNovo;
    private RecyclerView mostrarColabs;
    private List<ModelTeste> listaColabs = new ArrayList<>();
    private String TAG = "NovoProjeto";
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_projeto);
        mostrarColabs = findViewById(R.id.RecyclerTeste);

        //Listar colaboradores
        Colaboradores();

        //Configurando RecyclerView:
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
        User user = new User(this.reference);
        List<String> listaNomes = user.nomesMembros();
        Log.i("Teste", "Lista: "+listaNomes.toString());
        int aux = 0;
        while (aux < listaNomes.size()){
            ModelTeste Colab = new ModelTeste(listaNomes.get(aux));
            Log.i("Teste", "Nome: "+listaNomes.get(aux));
            this.listaColabs.add(Colab);
        }
    }
}
