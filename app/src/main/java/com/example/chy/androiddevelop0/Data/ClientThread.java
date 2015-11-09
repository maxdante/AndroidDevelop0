package com.example.chy.androiddevelop0.Data;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.example.chy.androiddevelop0.Draw.DrawView;
import com.example.chy.androiddevelop0.MainActivity;

/**
 * Created by zhibing on 2015-09-14.
 */
public class ClientThread implements Runnable {
    private Socket socket;
    private Handler handler;
    private String ipadress;
    private int port;
    private Activity sss;
    BufferedReader br=null;
    private DBManager dbManager;
    private TABLE_XYZ table_xyz;
    private SQLiteDatabase db;

    private  DrawView m_drawview;
    public String[] data;
    public static boolean isConnected = false;
    //ly add in------------
    public static final int CONNECTED=0;
    public static final int DISCONNECTED=1;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CONNECTED) {
                MainActivity.mConnectionStatue.setText("连接成功");
            }else
                MainActivity.mConnectionStatue.setText("连接失败");
            super.handleMessage(msg);
        }
    };
    //--------------
    public ClientThread(String ip,int port,DBManager dbManager,SQLiteDatabase db,DrawView drawview)throws IOException{
        this.ipadress=ip;
        this.port=port;
        this.dbManager=dbManager;
        this.db=db;
       //dbManager=new DBManager(context);
       //this.sss=ss;
       // br=new BufferedReader(new InputStreamReader(s.getInputStream()));
        m_drawview=drawview;
    }
    public void run(){
        try{
            String content=null;
            StartConnect(ipadress,port);
            //ly add in-----------
            if (isConnected){
                Message message = new Message();
                message.what = CONNECTED;
                mHandler.sendMessage(message);
            }else{
                Message message = new Message();
                message.what = DISCONNECTED;
                mHandler.sendMessage(message);
            }
            //---------------

            while (true){
                if(socket.isConnected()==false){
                    StartConnect(ipadress,port);
                }
                //ly add in-----------
                if (isConnected){
                    Message message = new Message();
                    message.what = CONNECTED;
                    //mHandler.sendMessage(message);
                }else{
                    Message message = new Message();
                    message.what = DISCONNECTED;
                    mHandler.sendMessage(message);
                }
                //---------------
                content = br.readLine();
                if(content.contains("GPGGA")) {
                    AnalysisData ss = new AnalysisData(content);
                    data = ss.Analysis();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date currentTime = new Date(System.currentTimeMillis());
                    String time = sdf.format(currentTime);
                    table_xyz = new TABLE_XYZ(data[0], data[1], data[2], data[3], time);
                    //Toast.makeText(sss,data[2],Toast.LENGTH_SHORT).show();
                    //
                    if (content == null){
                        continue;
                    }

                            String X = data[1];
                            String X_a = X.substring(0, 3);
                            String X_b = X.substring(3);
                            double Xdouble = 60 * Double.valueOf(X_a) + Double.valueOf(X_b);
                            String Y = data[0];
                            String Y_a = Y.substring(0, 2);
                            String Y_b = Y.substring(2);
                            double Ydouble = 60 * Double.valueOf(Y_a) + Double.valueOf(Y_b);
                            String Z = data[2];
                           // String Timestr = time;
                            //Date TimeDate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").parse(Timestr);
                            //String TimeStr2 = new SimpleDateFormat("HH:mm:ss").format(TimeDate);
                            m_drawview.init(time, Xdouble, Ydouble, Double.valueOf(Z));


                    //

                    try {
                        DBManager DBm=new DBManager();
                        DBm.db=db;
                        DBm.table_xyz=table_xyz;
                        DBm.setTextView(MainActivity.mConnectionStatue);
                        new Thread(DBm).start();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    //dbManager.add(db, table_xyz);
                    table_xyz = null;
                }
            }
            //dbManager.closeDB(db);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public boolean StartConnect(String ip,int port){
        try {
            socket=new Socket(ip,port);
            socket.setSoTimeout(5000);
            if(socket.isConnected()){
                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                isConnected = true;
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            isConnected = false;
            return false;
        }
    }
}
