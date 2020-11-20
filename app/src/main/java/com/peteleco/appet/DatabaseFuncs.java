package com.peteleco.appet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peteleco.appet.Autenticacao_Login.User;
import com.peteleco.appet.MenuInicial.ProjetosModel.ModeloProjetos;
import com.peteleco.appet.ProjetoEspecifico.MenuInicial.Tarefa;
import com.peteleco.appet.addNovoProjeto.RecyclerTeste.ModelTeste.ModelTeste;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import github.nisrulz.stackedhorizontalprogressbar.StackedHorizontalProgressBar;

public class DatabaseFuncs {
    private DatabaseReference reference;
    private SharedPreferences preferences;
    private String TAG = "bancoDados.class";
    private Context context;

    public DatabaseFuncs(Context context) {
        reference = FirebaseDatabase.getInstance().getReference();
        preferences = context.getSharedPreferences("Dados", 0);
        this.context = context;
    }

    public void carregarUsuarios () {
        final List<String> listaIDs = new ArrayList<>();
        final List<String> listaNome = new ArrayList<>();
        final List<String> listaCPF = new ArrayList<>();
        final List<String> listaGRR = new ArrayList<>();
        final List<String> listaEmail = new ArrayList<>();
        final List<String> listaTelefone = new ArrayList<>();

        DatabaseReference users = reference.getDatabase().getReference("users");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    listaIDs.add(child.getKey());
                    listaNome.add(dataSnapshot.child(child.getKey()+"/nome").getValue().toString());
                    listaCPF.add(dataSnapshot.child(child.getKey()+"/cpf").getValue().toString());
                    listaGRR.add(dataSnapshot.child(child.getKey()+"/grr").getValue().toString());
                    listaTelefone.add(dataSnapshot.child(child.getKey()+"/telefone").getValue().toString());
                    listaEmail.add(dataSnapshot.child(child.getKey()+"/email").getValue().toString());

                    Set<String> setIDs = new HashSet<>(listaIDs);
                    Set<String> setNome = new HashSet<>(listaNome);
                    Set<String> setCPF = new HashSet<>(listaCPF);
                    Set<String> setGRR = new HashSet<>(listaGRR);
                    Set<String> setTelefone = new HashSet<>(listaTelefone);
                    Set<String> setEmail = new HashSet<>(listaEmail);

                    preferences.edit().putStringSet("ids", setIDs).apply();
                    preferences.edit().putStringSet("nome", setNome).apply();
                    preferences.edit().putStringSet("grr", setGRR).apply();
                    preferences.edit().putStringSet("cpf", setCPF).apply();
                    preferences.edit().putStringSet("telefone", setTelefone).apply();
                    preferences.edit().putStringSet("email", setEmail).apply();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro ao carregar Informações: " + databaseError.getMessage());
            }
        });
    }

    public List<String> getInfos (String info) {

        List<String> listaInfo = new ArrayList<>();
        try {
            // Recuperando o nome dos membros que foram salvos
            Set<String> setInfo;

            if (info.toLowerCase().equals("nome")
                    || info.toLowerCase().equals("cpf")
                    || info.toLowerCase().equals("email")
                    || info.toLowerCase().equals("grr")
                    || info.toLowerCase().equals("telefone")
                    || info.toLowerCase().equals("ids")){

                setInfo = preferences.getStringSet(info.toLowerCase(), null);

                // Converção do set<String> para uma List<String>
                assert setInfo != null;
                int n = setInfo.size();
                listaInfo = new ArrayList<>(n);
                listaInfo.addAll(setInfo);
            } else {
                Toast.makeText(context, "Houve algum erro ao procurar informação no banco de dados", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao listar Usuários");
        }


        return listaInfo;
    }

    public void carregarProjetos () {
        final List<String> listaProjetos = new ArrayList<>();
        DatabaseReference projects = reference.getDatabase().getReference("testeProjetos");

        projects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    listaProjetos.add(child.getKey());

                }
                Set<String> set = new HashSet<>(listaProjetos);
                preferences.edit().putStringSet("projetos", set).apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro ao carregar Projetos");
            }
        });
    }

    public List<String> listaProjetos () {
        carregarProjetos();
        List<String> listaProjetos = null;
        try {
            // Recuperando o nome dos membros que foram salvos
            Set<String> set = preferences.getStringSet("projetos", null);

            // Converção do set<String> para uma List<String>
            assert set != null;
            int n = set.size();
            listaProjetos = new ArrayList<>(n);
            listaProjetos.addAll(set);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao listar Projetos");
        }

        Collections.sort(listaProjetos);
        return listaProjetos;
    }

    public void loadNomeLogado (final String email) {

        DatabaseReference user = reference.getDatabase().getReference("users");
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    if (email.equals(child.child("email").getValue().toString())){
                        preferences.edit().putString("nomeLogadoUI", child.getKey()).apply();
                        preferences.edit().putString("nomeLogado", child.child("nome").getValue().toString()).apply();
                        preferences.edit().putString("nomeLogadoGRR", child.child("grr").getValue().toString()).apply();
                        preferences.edit().putString("nomeLogadoCPF", child.child("cpf").getValue().toString()).apply();
                        preferences.edit().putString("nomeLogadoEmail", child.child("email").getValue().toString()).apply();
                        preferences.edit().putString("nomeLogadoTelefone", child.child("telefone").getValue().toString()).apply();
                        try{
                            if (child.child("dev").getValue().toString() == "true"){
                                preferences.edit().putBoolean("nomeLogadoDev", true).apply();
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public String getInfoNomeLogado ( String infokey ) {
        // infoKey é a chave que ficou salvo no SharedePreferences,
        // ver keys utilizadas no método "loadNomeLogado"
        return preferences.getString(infokey, null);
    }

    public void salvarDadoEspecificoBD (String referenciaBD, String dado) {
        String userUI = this.getInfoNomeLogado("nomeLogadoUI");

        if (referenciaBD.equals("nome") || referenciaBD.equals("email") || referenciaBD.equals("grr") ||
                referenciaBD.equals("cpf") || referenciaBD.equals("telefone")) {
            try {
                reference.child("users").child(userUI).child(referenciaBD).setValue(dado);
                Toast.makeText(context, "Dado salvo com sucesso", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(context, "Erro ao salvar dado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void salvarDadosBD (User user) {
        String userUI = this.getInfoNomeLogado("nomeLogadoUI");

        try {
            reference.child("users").child(userUI).setValue(user);
            Toast.makeText(context, "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Erro ao salvar dados", Toast.LENGTH_SHORT).show();
        }
    }

    private void setProjetosDoUser (final String nomeDoProjeto) {
        final String userUI = getInfoNomeLogado("nomeLogadoUI");

        final List<String> listaProjetos = new ArrayList<>();
        DatabaseReference membros = reference.getDatabase().getReference("testeProjetos/"+nomeDoProjeto+"/membros");
        membros.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    if (userUI.equals(child.getKey())){
                        Set<String> set = preferences.getStringSet("nomeLogadoProjeto", null);
                        if (set != null) {
                            listaProjetos.addAll(set);
                            listaProjetos.add(nomeDoProjeto);
                        } else {
                            listaProjetos.add(0, nomeDoProjeto);
                        }
                        set = new HashSet<>(listaProjetos);
                        preferences.edit().putStringSet("nomeLogadoProjeto", set).apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verificaProjetosUser () {
        DatabaseReference projetos = reference.getDatabase().getReference("testeProjetos");

        projetos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    setProjetosDoUser(child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro setProjetosDoUser: " + databaseError.getMessage());
            }
        });
    }

    public List<String> getUserProjects () {
        Set<String> set = preferences.getStringSet("nomeLogadoProjeto", null);
        List<String> listaProjetos = new ArrayList<>(set.size());
        listaProjetos.addAll(set);
        Collections.sort(listaProjetos);

        return listaProjetos;
    }

    public void isCoordenador (final String projeto) {
        final String userUI = getInfoNomeLogado("nomeLogadoUI");
        DatabaseReference teste = reference.child("testeProjetos/"+projeto+"/membros/"+userUI);
        try {
            teste.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String aux = dataSnapshot.getValue().toString().toLowerCase();
                    if (aux.equals("coordenador")) {
                        preferences.edit().putBoolean("coordenador"+projeto,true).apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            });
        }catch (Exception e){
            Log.e(TAG, "Erro ao utilizar addValueEventListener");
            Log.e(TAG, e.getMessage());
        }
    }

    public void getInfoMembro (String idUnica, String info) {
        try {
            reference.child("users/"+idUnica+"/"+info).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Set<String> set = preferences.getStringSet("nomeMembroPE", null);
                    List<String> aux = new ArrayList<>();
                    if (set == null){
                        aux.add(0, dataSnapshot.getValue().toString());
                    } else {
                        aux.addAll(set);
                        try {
                            aux.add(dataSnapshot.getValue().toString());
                        } catch (Exception e) {
                            Log.e(TAG, "Referencia invalida ou nula");
                        }
                        Collections.sort(aux);
                    }
                    set = new HashSet<>(aux);
                    //Log.i(TAG, "Set: "+set);
                    preferences.edit().putStringSet("nomeMembroPE",  set).apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }

    public void membrosProjeto (final String projeto) {

        reference.child("testeProjetos/"+projeto+"/membros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    getInfoMembro(child.getKey(), "nome");
                    //Log.i(TAG, "Projeto: "+projeto+", membro: "+child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro ao recuperar IDs em membrosProjeto");
            }
        });
    }

    public void progressoProjeto (String nomeProjeto, final StackedHorizontalProgressBar progressBar) {
        // Verificando número de tarefas
        DatabaseReference projetos = reference.child("testeProjetos/"+nomeProjeto);
        projetos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
                int numTaskToDo = (int) dataSnapshot.child("TO DO").getChildrenCount();
                int numTaskDoing = (int) dataSnapshot.child("DOING").getChildrenCount();
                int numTaskDone = (int) dataSnapshot.child("DONE").getChildrenCount();
                int totalTasks = numTaskToDo+numTaskDoing+numTaskDone;
                progressBar.setMax(totalTasks);
                progressBar.setProgress(numTaskDone);
                progressBar.setSecondaryProgress(numTaskDoing);
                //progressBar.init();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void agendarConsultaFirebase(long delay_sec) {
        /*
            Essa função agenda o horário para a consulta dos dados no firebase! Essa vrificação vai ser
            feita após um período X depois que o usuário entrar ou (após implementação do app em segundo plano)
            cada dia a meia noite será diparado essa verificação

            A função que lê os dados após que o trigger da função for ativada é a função NotificationService.scheduleNotification

         */

        long delay_milis = delay_sec *1000;
        int scheduleId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Log.i(TAG, "ScheduleID "+scheduleId);

        Intent scheduleIntent = new Intent(context, BcReceiver.class);
        scheduleIntent.putExtra(BcReceiver.SCHEDULE_ID, scheduleId);

        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, scheduleId, scheduleIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = Calendar.getInstance().getTimeInMillis() + delay_milis;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC, futureInMillis, pendingIntent2);
            } else {
                alarmManager.set(AlarmManager.RTC, futureInMillis, pendingIntent2);
            }
        }else {
            Log.w(TAG, "Alarm manager é null");
        }
    }
    public void getTaskTimeNotification(){
        Set<String> aux = preferences.getStringSet("nomeLogadoProjeto", null);
        ArrayList<String> projetosUser;
        try{
            projetosUser = new ArrayList<String>(aux);
        }catch (Exception e){
            projetosUser = null;
            Log.e(TAG, "nomeLogadoProjeto = Null");
            Log.e(TAG, e.getMessage());
        }
        if (projetosUser!=null){
            final NotificationService notificationService = new NotificationService();
            DatabaseReference projetos = reference.child("testeProjetos");
            final ArrayList<String> finalProjetosUser = projetosUser;
            projetos.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (String projetoUser: finalProjetosUser){
                        try {
                            Iterable<DataSnapshot> tarefas = dataSnapshot.child(projetoUser+"/DOING").getChildren();
                            for (DataSnapshot tarefa : tarefas){
                                /* TODO: Corrigir ID de notificação em Notification Service
                                         Fazer implementar delay correto
                                         Pedir para o Marketing da Logo do PET uma image 24x24 px para utilizar na notificação
                                 */
                                notificationService.scheduleNotification(
                                        context, 5, projetoUser, tarefa.getKey()
                                );
                                Thread.sleep(20);
                            }
                            /*notificationService.scheduleNotification(
                                    context, 20, projetoUser, nomeTarefa
                            );
                            Thread.sleep(100);
                            nomeTarefa = dataSnapshot.child(projetoUser+"/TO DO").getValue().toString();
                            notificationService.scheduleNotification(
                                    context, 20, projetoUser, nomeTarefa
                            );*/
                        }catch (Exception e){
                            Log.e(TAG, "Erro ao localizar uma tarefa");
                            Log.e(TAG, e.getMessage());
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
