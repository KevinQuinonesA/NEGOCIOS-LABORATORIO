package com.hellohasan.sqlite_project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;
import com.hellohasan.sqlite_project.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.hellohasan.sqlite_project.Features.Planta.CreatePlanta.Planta;
import com.hellohasan.sqlite_project.Features.Linea.CreateLinea.Linea;


public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertPlanta(Planta planta){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PLANTA_NAME, planta.getName());
        contentValues.put(Config.COLUMN_PLANTA_STATE, planta.getState());
        try {
            if(getPlantaName(planta.getName())==null) {
                databaseHelper = DatabaseHelper.getInstance(context);
                sqLiteDatabase = databaseHelper.getWritableDatabase();
                id = sqLiteDatabase.insertOrThrow(Config.TABLE_PLANTA, null, contentValues);
            }
            else{
                Toast.makeText(context,"Ya existe una Planta con ese nombre",Toast.LENGTH_SHORT);
            }
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Planta> getAllPlanta(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_PLANTA, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_PLANTA_ID, Config.COLUMN_PLANTA_NAME, Config.COLUMN_PLANTA_REGISTRATION, Config.COLUMN_PLANTA_EMAIL, Config.COLUMN_PLANTA_PHONE, Config.TABLE_PLANTA);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Planta> plantaList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PLANTA_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PLANTA_NAME));
                        String state = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PLANTA_STATE));
                        plantaList.add(new Planta(id, name,state));
                    }   while (cursor.moveToNext());

                    return plantaList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Planta getPlantaName(String name){
        Log.d("Agregando 02", "getPlantaName: "+name);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Planta planta = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_PLANTA, null,
                    Config.COLUMN_PLANTA_NAME + " = ? ", new String[]{name},
                    null, null, null);



            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PLANTA_ID));
                name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PLANTA_NAME));
                String  state= cursor.getString(cursor.getColumnIndex(Config.COLUMN_PLANTA_STATE));
                planta = new Planta(id, name,state);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return planta;
    }

    public long updatePlantaInfo(Planta planta){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PLANTA_NAME, planta.getName());
        contentValues.put(Config.COLUMN_PLANTA_STATE, planta.getState());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_PLANTA, contentValues,
                    Config.COLUMN_PLANTA_ID + " = ? ",
                    new String[] {String.valueOf(planta.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deletePlantaByRegNum(String state) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_PLANTA,
                    Config.COLUMN_PLANTA_STATE + " = ? ",
                    new String[]{ state});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllPlantas(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_PLANTA, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_PLANTA);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

    //-----------------------------------------------------------------------------------------------------//
    //----------------------------LINEA DE PRODUCCION-----------------------------------------------------//
    public long insertLinea(Linea linea){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_LINEAP_NAME, linea.getName());
        contentValues.put(Config.COLUMN_LINEAP_STATE, linea.getState());
        try {
            if(getLineaName(linea.getName())==null) {
                databaseHelper = DatabaseHelper.getInstance(context);
                sqLiteDatabase = databaseHelper.getWritableDatabase();
                id = sqLiteDatabase.insertOrThrow(Config.TABLE_LINEAP, null, contentValues);
            }
            else{
                Toast.makeText(context,"Ya existe una Linea de Producción con ese nombre",Toast.LENGTH_SHORT);
            }
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Linea> getAllLinea(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_LINEAP, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_LINEA_ID, Config.COLUMN_LINEAP_NAME, Config.COLUMN_LINEAP_REGISTRATION, Config.COLUMN_LINEAP_EMAIL, Config.COLUMN_LINEAP_PHONE, Config.TABLE_LINEAP);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Linea> lineaList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_LINEAP_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_LINEAP_NAME));
                        String state = cursor.getString(cursor.getColumnIndex(Config.COLUMN_LINEAP_STATE));
                        lineaList.add(new Linea(id, name,state));
                    }   while (cursor.moveToNext());

                    return lineaList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Linea getLineaName(String name){
        Log.d("Agregando 02", "getLineaName: "+name);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Linea linea = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_LINEAP, null,
                    Config.COLUMN_LINEAP_NAME + " = ? ", new String[]{name},
                    null, null, null);



            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_LINEAP_ID));
                name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_LINEAP_NAME));
                String  state= cursor.getString(cursor.getColumnIndex(Config.COLUMN_LINEAP_STATE));
                linea = new Linea(id, name,state);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return linea;
    }

    public long updateLineaInfo(Linea linea){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_LINEAP_NAME, linea.getName());
        contentValues.put(Config.COLUMN_LINEAP_STATE, linea.getState());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_LINEAP, contentValues,
                    Config.COLUMN_LINEAP_ID + " = ? ",
                    new String[] {String.valueOf(linea.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteLineaByRegNum(String state) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_LINEAP,
                    Config.COLUMN_LINEAP_STATE + " = ? ",
                    new String[]{ state});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllLineas(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_LINEAP, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_LINEAP);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

}