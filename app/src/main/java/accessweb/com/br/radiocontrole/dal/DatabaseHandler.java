package accessweb.com.br.radiocontrole.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import accessweb.com.br.radiocontrole.model.Noticia;

/**
 * Created by Des. Android on 22/06/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "noticiasManager";

    // Noticias table name
    private static final String TABLE_NOTICIAS = "noticias";

    // Noticias Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TEMPO_NOTICIA = "tempoNoticia";
    private static final String KEY_TITULO_NOTICIA = "tituloNoticia";
    private static final String KEY_RESUMO_NOTICIA = "resumoNoticia";
    private static final String KEY_LINK_NOTICIA = "linkNoticia";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTICIAS_TABLE = "CREATE TABLE " + TABLE_NOTICIAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TEMPO_NOTICIA + " TEXT,"
                + KEY_TITULO_NOTICIA + " TEXT," + KEY_RESUMO_NOTICIA + " TEXT,"
                + KEY_LINK_NOTICIA + " TEXT" + ")";
        db.execSQL(CREATE_NOTICIAS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICIAS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new noticia
    public void addNoticia(Noticia noticia) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEMPO_NOTICIA, noticia.getTempoNoticia());
        values.put(KEY_TITULO_NOTICIA, noticia.getTituloNoticia());
        values.put(KEY_RESUMO_NOTICIA, noticia.getResumoNoticia());
        values.put(KEY_LINK_NOTICIA, noticia.getLinkNoticia());


        // Inserting Row
        db.insert(TABLE_NOTICIAS, null, values);
        db.close(); // Closing database connection
        Log.v("addNoticia", "Notícia adicionada com sucesso.");
    }

    // Getting single noticia
    public Noticia getNoticia(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTICIAS, new String[] { KEY_ID,
                        KEY_TEMPO_NOTICIA, KEY_TITULO_NOTICIA, KEY_RESUMO_NOTICIA, KEY_LINK_NOTICIA }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Noticia noticia = new Noticia(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return noticia
        return noticia;
    }

    // Check if Noticias already exists in db
    public  boolean isEqualTo(Noticia n) {
        List<Noticia> noticiasBanco = getAllNoticias();
        for (Noticia noticiaBanco : noticiasBanco){
            if (n.getLinkNoticia().equals(noticiaBanco.getLinkNoticia())) {
                return true;
            }
        }
        return false;
    }
    // Getting All Noticias
    public List<Noticia> getAllNoticias() {
        List<Noticia> noticiaList = new ArrayList<Noticia>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTICIAS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Noticia noticia = new Noticia();
                noticia.setTempoNoticia(cursor.getString(1));
                noticia.setTituloNoticia(cursor.getString(2));
                noticia.setResumoNoticia(cursor.getString(3));
                noticia.setLinkNoticia(cursor.getString(4));

                // Adding noticia to list
                noticiaList.add(noticia);
            } while (cursor.moveToNext());
        }

        // return noticia list
        return noticiaList;
    }

    // Updating single noticia
    public int updateNoticia(Noticia noticia) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEMPO_NOTICIA, noticia.getTempoNoticia());
        values.put(KEY_TITULO_NOTICIA, noticia.getTituloNoticia());
        values.put(KEY_RESUMO_NOTICIA, noticia.getResumoNoticia());
        values.put(KEY_LINK_NOTICIA, noticia.getLinkNoticia());

        // updating row
        return db.update(TABLE_NOTICIAS, values, KEY_LINK_NOTICIA + " = ?",
                new String[] { String.valueOf(noticia.getLinkNoticia()) });
    }

    // Deleting single noticia
    public void deleteNoticia(Noticia noticia) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTICIAS, KEY_LINK_NOTICIA + " = ?",
                new String[] { String.valueOf(noticia.getLinkNoticia()) });
        db.close();
    }


    // Getting noticias Count
    public int getNoticiasCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTICIAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
