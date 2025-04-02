package com.example.bibliotecavirtual;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etTitulo, etAutor;
    Spinner spCategoria;
    CheckBox cbLido;
    Button btnSalvar;
    ListView lvLivros;
    ArrayList<String> categorias = new ArrayList<>();
    SimpleCursorAdapter adapter;
    int livroSelecionadoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        etTitulo = findViewById(R.id.etTitulo);
        etAutor = findViewById(R.id.etAutor);
        spCategoria = findViewById(R.id.spCategoria);
        cbLido = findViewById(R.id.cbLido);
        btnSalvar = findViewById(R.id.btnSalvar);
        lvLivros = findViewById(R.id.lvLivros);


        categorias.add("Ficção");
        categorias.add("Técnico");
        categorias.add("Autoajuda");
        categorias.add("História");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(spinnerAdapter);

        carregarLivros();

        btnSalvar.setOnClickListener(view -> {
            String titulo = etTitulo.getText().toString();
            String autor = etAutor.getText().toString();
            String categoria = spCategoria.getSelectedItem().toString();
            boolean lido = cbLido.isChecked();

            if (titulo.isEmpty() || autor.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (livroSelecionadoId == -1) {
                db.adicionarLivro(titulo, autor, categoria, lido);
            } else {
                db.atualizarLivro(livroSelecionadoId, titulo, autor, categoria, lido);
                livroSelecionadoId = -1;
            }

            carregarLivros();
        });

        lvLivros.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            livroSelecionadoId = cursor.getInt(0);
            etTitulo.setText(cursor.getString(1));
            etAutor.setText(cursor.getString(2));
            cbLido.setChecked(cursor.getInt(4) == 1);
        });

        lvLivros.setOnItemLongClickListener((parent, view, position, id) -> {
            db.deletarLivro((int) id);
            carregarLivros();
            return true;
        });
    }

    private void carregarLivros() {
        Cursor cursor = db.obterLivros();
        String[] from = {"titulo", "autor"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        lvLivros.setAdapter(adapter);
    }
}
