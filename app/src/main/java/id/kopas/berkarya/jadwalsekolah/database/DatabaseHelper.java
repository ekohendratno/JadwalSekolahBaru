package id.kopas.berkarya.jadwalsekolah.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "jadwalsekolah";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create jadwals table
        db.execSQL(Jadwal.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Jadwal.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertPelajaran(String hari, String jadwal, String ruang, String mulai, String selesai, String pengingat) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Jadwal.COLUMN_HARI, hari);
        values.put(Jadwal.COLUMN_JADWAL, jadwal);
        values.put(Jadwal.COLUMN_RUANG, ruang);
        values.put(Jadwal.COLUMN_MULAI, mulai);
        values.put(Jadwal.COLUMN_SELESAI, selesai);
        values.put(Jadwal.COLUMN_PENGINGAT, pengingat);

        // insert row
        long id = db.insert(Jadwal.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Jadwal getPelajaran(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Jadwal.TABLE_NAME,
                new String[]{ Jadwal.COLUMN_ID,Jadwal.COLUMN_HARI, Jadwal.COLUMN_JADWAL, Jadwal.COLUMN_RUANG, Jadwal.COLUMN_MULAI, Jadwal.COLUMN_SELESAI, Jadwal.COLUMN_PENGINGAT, Jadwal.COLUMN_TIMESTAMP},
                Jadwal.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare jadwal object
        Jadwal jadwal = new Jadwal(
                cursor.getInt(cursor.getColumnIndex(Jadwal.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_HARI)),
                cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_JADWAL)),
                cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_RUANG)),
                cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_MULAI)),
                cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_SELESAI)),
                cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_PENGINGAT)),
                cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return jadwal;
    }

    public List<Jadwal> getAllPelajaran(String hari) {
        String hariQuery = "";
        List<Jadwal> jadwals = new ArrayList<>();

        if (hari != null)
            hariQuery = " WHERE " + Jadwal.COLUMN_HARI + "='" + hari + "'";


        // Select All Query
        String selectQuery = "SELECT  * FROM " + Jadwal.TABLE_NAME + hariQuery + " ORDER BY " +
                Jadwal.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        jadwals.clear();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                jadwals.add(new Jadwal(
                        cursor.getInt(cursor.getColumnIndex(Jadwal.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_HARI)),
                        cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_JADWAL)),
                        cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_RUANG)),
                        cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_MULAI)),
                        cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_SELESAI)),
                        cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_PENGINGAT)),
                        cursor.getString(cursor.getColumnIndex(Jadwal.COLUMN_TIMESTAMP))
                ));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return jadwals list
        return jadwals;
    }

    public int getPelajaranCount() {
        String countQuery = "SELECT  * FROM " + Jadwal.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updatePelajaran(Jadwal jadwal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Jadwal.COLUMN_HARI, jadwal.getHari());
        values.put(Jadwal.COLUMN_JADWAL, jadwal.getJadwal());
        values.put(Jadwal.COLUMN_RUANG, jadwal.getRuang());
        values.put(Jadwal.COLUMN_MULAI, jadwal.getMulai());
        values.put(Jadwal.COLUMN_SELESAI, jadwal.getSelesai());
        values.put(Jadwal.COLUMN_PENGINGAT, jadwal.getPengingat());

        // updating row
        return db.update(Jadwal.TABLE_NAME, values, Jadwal.COLUMN_ID + " = ?",
                new String[]{String.valueOf(jadwal.getId())});
    }

    public void deletePelajaran(Jadwal jadwal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Jadwal.TABLE_NAME, Jadwal.COLUMN_ID + " = ?",
                new String[]{String.valueOf(jadwal.getId())});
        db.close();
    }
}
