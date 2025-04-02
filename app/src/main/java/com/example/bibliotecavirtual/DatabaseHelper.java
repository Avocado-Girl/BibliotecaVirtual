package com.example.bibliotecavirtual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LIVROS = "livros";
    private static final String COL_ID = "id";
    private static final String COL_TITULO = "titulo";
    private static final String COL_AUTOR = "autor";
    private static final String COL_CATEGORIA = "categoria";
    private static final String COL_LIDO = "lido";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_LIVROS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITULO + " TEXT, " +
                COL_AUTOR + " TEXT, " +
                COL_CATEGORIA + " TEXT, " +
                COL_LIDO + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIVROS);
        onCreate(db);
    }

    public boolean adicionarLivro(String titulo, String autor, String categoria, boolean lido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITULO, titulo);
        values.put(COL_AUTOR, autor);
        values.put(COL_CATEGORIA, categoria);
        values.put(COL_LIDO, lido ? 1 : 0);

        long result = db.insert(TABLE_LIVROS, null, values);
        return result != -1;
    }

    public Cursor obterLivros() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_LIVROS, null);
    }

    public boolean atualizarLivro(int id, String titulo, String autor, String categoria, boolean lido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITULO, titulo);
        values.put(COL_AUTOR, autor);
        values.put(COL_CATEGORIA, categoria);
        values.put(COL_LIDO, lido ? 1 : 0);

        int result = db.update(TABLE_LIVROS, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deletarLivro(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_LIVROS, COL_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
