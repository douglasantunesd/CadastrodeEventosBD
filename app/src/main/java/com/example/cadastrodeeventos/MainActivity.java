package com.example.cadastrodeeventos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cadastrodeeventos.database.EventoDAO;
import com.example.cadastrodeeventos.model.Evento;

public class MainActivity extends AppCompatActivity {

    ListView listViewEventos;
    ArrayAdapter<Evento> adapterEventos;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Eventos");
        listViewEventos = findViewById(R.id.listView_eventos);

        definirOnClickListenerListView();
        definirOnLongClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventoDAO eventoDAO = new EventoDAO(getBaseContext());
        adapterEventos = new ArrayAdapter<Evento>(MainActivity.this, android.R.layout.simple_list_item_1, eventoDAO.listar());
        listViewEventos.setAdapter(adapterEventos);
    }

    private void definirOnLongClickListener() {
        listViewEventos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Evento eventoClicado = adapterEventos.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setIcon(android.R.drawable.ic_delete);
                        builder.setTitle("Removendo Evento!");
                        builder.setMessage("Deseja realmente remover este Evento?");
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventoDAO eventoDAO = new EventoDAO(getBaseContext());
                                eventoDAO.deletar(eventoClicado);
                                Toast.makeText(MainActivity.this, "Evento removido com sucesso!", Toast.LENGTH_LONG).show();
                                onResume();
                            }
                        })
                        .setNegativeButton("Não",null).show();
                return true;
            }
        });
    }

    private void definirOnClickListenerListView(){
        listViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Evento eventoClicado = adapterEventos.getItem(position);
                Intent intent = new Intent(MainActivity.this, CadastroEventoActivity.class);
                intent.putExtra("eventoEdicao", eventoClicado);
                startActivity(intent);
            }
        });
    }

    public void onClickCadastrarEvento(View view){
        Intent intent = new Intent(MainActivity.this, CadastroEventoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 10){
           Evento evento = (Evento) data.getExtras().getSerializable("novoEvento");
            evento.setId(++id);
            adapterEventos.add(evento);
        } else if(requestCode == 2 && resultCode == 11){
            Evento eventoEditado = (Evento) data.getExtras().getSerializable("eventoEditado");
            for (int i = 0; i < adapterEventos.getCount(); ++i){
                Evento evento = adapterEventos.getItem(i);
               if(evento.getId() == eventoEditado.getId()){
                    adapterEventos.remove(evento);
                    adapterEventos.insert(eventoEditado, i);
                    break;
                }

           }
       }
   }
}
