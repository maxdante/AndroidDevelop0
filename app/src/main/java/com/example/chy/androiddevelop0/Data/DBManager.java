package com.example.chy.androiddevelop0.Data;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhibing on 2015-09-15.
 */
public class DBManager implements Runnable{

    private static final String DATABASE_NAME="Mytest.db";
    private static final int DATABASE_VERSION=1;

    private SQLiteDatabase database;
    private Context context;
    public DBManager(){
        //helper=new DBHelper(context);
        //db=helper.getWritableDatabase();
        ///this.context=context;
        //this.db=db;
    }
    public SQLiteDatabase CreateDB(){


        //SQLiteDatabase database =SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory()+"/"+DATABASE_NAME,null,0);
           SQLiteDatabase database=SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/"+DATABASE_NAME,null);

            return  database;


    }
    public void createTable(SQLiteDatabase db){
        String sql="CREATE TABLE IF NOT EXISTS TABLE_XYZ"+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,X VARCHAR,Y VARCHAR,Z VARCHAR,M VARCHAR,time VARCHAR)";
        db.execSQL(sql);
    }
    public void add(SQLiteDatabase db,TABLE_XYZ table_xyz){


        String sql="insert into TABLE_XYZ(X,Y,Z,M,time) values('"+table_xyz.X+"','"+table_xyz.Y+"','"+table_xyz.Z+"','"+table_xyz.M2+"','"+table_xyz.time+"')";
        //execSQL("INSERT INTO TABLE_XYZ VALUES(null,?,?,?,?,?)",new Object[]{table_xyz.X,table_xyz.Y,table_xyz.Z,table_xyz.M2,table_xyz.time});
        try{
            db.execSQL(sql);
            //setText("插入成功");
        }
        catch (Exception e){
            Log.e("插入失败",e.getMessage());
            e.printStackTrace();
        }
//        db.beginTransaction();
//        try{
//            db.execSQL("INSERT INTO TABLE_XYZ VALUES(null,?,?,?,?,?)",new Object[]{table_xyz.X,table_xyz.Y,table_xyz.Z,table_xyz.M2,table_xyz.time});
//            db.setTransactionSuccessful();
//            database=db;
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        finally {
//
//            db.endTransaction();
//            //db.close();
//        }
    }
    public SQLiteDatabase db;
    public TABLE_XYZ table_xyz;
    public void run()
    {
        add(db,table_xyz);
    }
    public void closeDB(SQLiteDatabase db)
    {
     db.close();
    }
    public void serch(){
        try {


            Cursor cursor = database.query("TABLE_XYZ", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.move(i);
                    int id = cursor.getInt(0);
                    String X = cursor.getString(1);
                    String Y = cursor.getString(2);
                    String Z = cursor.getString(3);
                    String M = cursor.getString(4);
                    String time = cursor.getString(5);
                    System.out.println(id + "::" + X + "::" + Y + "::" + Z + "::" + M + "::" + time);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //ly add in
    public static TextView mTextView;
    public void setTextView(TextView v){
        mTextView = v;
    }
    public void  setText(String str){
        if (mTextView !=null){
            mTextView.setText(str);
        }
    }
}
