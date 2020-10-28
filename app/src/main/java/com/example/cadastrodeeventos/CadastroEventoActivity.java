package com.example.cadastrodeeventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cadastrodeeventos.database.EventoDAO;
import com.example.cadastrodeeventos.model.Evento;


public class CadastroEventoActivity extends AppCompatActivity {

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);
        setTitle("Cadastrar Evento");
        carregarEvento();
    }

    private void carregarEvento(){
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().get("eventoEdicao") != null){
            Evento evento = (Evento) intent.getExtras().getSerializable("eventoEdicao");
            EditText editTextNome = findViewById(R.id.editText_nome);
            EditText editTextLocal = findViewById(R.id.editText_local);
            EditText editTextData = findViewById(R.id.editText_data);
            editTextNome.setText(evento.getNome());
            editTextLocal.setText(evento.getLocal());
            editTextData.setText(String.valueOf(evento.getData()));
            id = evento.getId();
        }
    }

    public void onClickVoltar(View v){
        finish();
    }

    public void onClickSalvar(View v) {
        EditText editTextNome = findViewById(R.id.editText_nome);
        EditText editTextData = findViewById(R.id.editText_data);
        EditText editTextLocal = findViewById(R.id.editText_local);

        if(!editTextNome.getText().toString().isEmpty() && !editTextData.getText().toString().isEmpty() &&  !editTextLocal.getText().toString().isEmpty()){

            String nome = editTextNome.getText().toString();
            String data = editTextData.getText().toString();
            String local = editTextLocal.getText().toString();

            Evento evento = new Evento( id, nome, data, local);
            EventoDAO eventoDAO = new EventoDAO(getBaseContext());
            boolean salvou = eventoDAO.salvar(evento);
            if (salvou) {
                finish();
            } else {
                Toast.makeText(CadastroEventoActivity.this, "Erro ao salvar evento", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(CadastroEventoActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }


    }

}
