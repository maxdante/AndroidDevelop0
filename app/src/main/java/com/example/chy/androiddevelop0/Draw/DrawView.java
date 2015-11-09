package com.example.chy.androiddevelop0.Draw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2015/9/11.
 */
public class DrawView extends View{
    public DrawView(Context context,AttributeSet set) {
        super(context,set);
        try {
            DatabaseDraw();
        }
        catch (Exception ex){

        }


    }
/*
    @Override
    public void run(){
        while(true){
            // 不断的调用View中的postInvalidate方法，让界面重新绘制
            this.postInvalidate();
            try {
                // 暂停0.5秒继续
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ReDraw()
    {
        this.postInvalidate();
    }
    */
private static final String DATABASE_NAME="Mytest.db";

    public void DatabaseDraw()
    {
        String p_dbpath= Environment.getExternalStorageDirectory()+"/"+DATABASE_NAME;
        String p_SQLStr="select *from Table_XYZ";
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(p_dbpath,null);

        Cursor m_cursor=db.rawQuery(p_SQLStr,null);
        int m_cursorcount=m_cursor.getCount();

        if (m_cursorcount<=600)
        {
            m_cursor.moveToFirst();
/*
            TimeArray.add(m_cursor.getString(5));
            XArray_First.add(Double.valueOf(m_cursor.getString(2)));
            YArray_First.add(Double.valueOf(m_cursor.getString(1)));
            ZArray_First.add(Double.valueOf(m_cursor.getString(3)));*/
            String X = m_cursor.getString(2);
            String X_a = X.substring(0, 3);
            String X_b = X.substring(3);
            double Xdouble = 60 * Double.valueOf(X_a) + Double.valueOf(X_b);
            String Y = m_cursor.getString(1);
            String Y_a = Y.substring(0, 2);
            String Y_b = Y.substring(2);
            double Ydouble = 60 * Double.valueOf(Y_a) + Double.valueOf(Y_b);
            initTXYZ(m_cursor.getString(5),Xdouble,Ydouble,Double.valueOf(m_cursor.getString(3)));
            maxminUnit();
            TimeStr();
            while (m_cursor.moveToNext())
            {
                /*
                TimeArray.add(m_cursor.getString(5));
                XArray_First.add(Double.valueOf(m_cursor.getString(2)));
                YArray_First.add(Double.valueOf(m_cursor.getString(1)));
                ZArray_First.add(Double.valueOf(m_cursor.getString(3)));
*/
                 X = m_cursor.getString(2);
                 X_a = X.substring(0, 3);
                 X_b = X.substring(3);
                 Xdouble = 60 * Double.valueOf(X_a) + Double.valueOf(X_b);
                 Y = m_cursor.getString(1);
                 Y_a = Y.substring(0, 2);
                 Y_b = Y.substring(2);
                 Ydouble = 60 * Double.valueOf(Y_a) + Double.valueOf(Y_b);
                initTXYZ(m_cursor.getString(5),Xdouble,Ydouble,Double.valueOf(m_cursor.getString(3)));
                maxminUnit();
                TimeStr();
               // this.postInvalidate();

            }

        }
        else
        {
            m_cursor.moveToPosition(m_cursorcount - 600);
/*
            TimeArray.add(m_cursor.getString(5));
            XArray_First.add(Double.valueOf(m_cursor.getString(2)));
            YArray_First.add(Double.valueOf(m_cursor.getString(1)));
            ZArray_First.add(Double.valueOf(m_cursor.getString(3)));*/
            String X = m_cursor.getString(2);
            String X_a = X.substring(0, 3);
            String X_b = X.substring(3);
            double Xdouble = 60 * Double.valueOf(X_a) + Double.valueOf(X_b);
            String Y = m_cursor.getString(1);
            String Y_a = Y.substring(0, 2);
            String Y_b = Y.substring(2);
            double Ydouble = 60 * Double.valueOf(Y_a) + Double.valueOf(Y_b);
            initTXYZ(m_cursor.getString(5),Xdouble,Ydouble,Double.valueOf(m_cursor.getString(3)));
            maxminUnit();
            TimeStr();
            while (m_cursor.moveToNext())
            {
/*
                TimeArray.add(m_cursor.getString(5));
                XArray_First.add(Double.valueOf(m_cursor.getString(2)));
                YArray_First.add(Double.valueOf(m_cursor.getString(1)));
                ZArray_First.add(Double.valueOf(m_cursor.getString(3)));*/

                X = m_cursor.getString(2);
                X_a = X.substring(0, 3);
                X_b = X.substring(3);
                Xdouble = 60 * Double.valueOf(X_a) + Double.valueOf(X_b);
                Y = m_cursor.getString(1);
                Y_a = Y.substring(0, 2);
                Y_b = Y.substring(2);
                Ydouble = 60 * Double.valueOf(Y_a) + Double.valueOf(Y_b);
                initTXYZ(m_cursor.getString(5),Xdouble,Ydouble,Double.valueOf(m_cursor.getString(3)));
                maxminUnit();
                TimeStr();
               // this.postInvalidate();

            }

        }
       // maxminUnit();
        //TimeStr();


    }
public void init(String str,double X,double Y,double Z)
{
            initTXYZ(str,X,Y,Z);
            //initTXYZ("NOW",1,1,1);
            maxminUnit();
            TimeStr();
            this.postInvalidate();
}
    private ArrayList<String> TimeStr10=new ArrayList<String>();
    private int Timenum=0;
    private void TimeStr()
    {
        int timestrlength=(int)(TimeArray.size()/60);
        if (timestrlength==0)
            return;
        TimeStr10.clear();
        Timenum=timestrlength;
        for (int i=1;i<=timestrlength;i++)
        {
            TimeStr10.add(TimeArray.get(TimeArray.size()-i*60));
        }
    }
//传进来的XY参数要求为秒
    private ArrayList<String> TimeArray=new ArrayList<String>();
    private ArrayList<Double> XArray_First=new ArrayList<Double>();
    private ArrayList<Double> YArray_First=new ArrayList<Double>();
    private ArrayList<Double> ZArray_First=new ArrayList<Double>();

    //涂上绘制是的相对位置参数
    private ArrayList<Integer> XArray_Table=new ArrayList<Integer>();
    private ArrayList<Integer> YArray_Table=new ArrayList<Integer>();
    private ArrayList<Integer> ZArray_Table=new ArrayList<Integer>();

    //是否已经开始绘图
    private boolean isDraw=false;


    private double Xmax=0;
    private double Ymax=0;
    private  double Zmax=0;
    private double Xmin=10800;
    private double Ymin=5400;
    private double Zmin=10000;
    private double XUnit=0;
    private double YUnit=0;
    private double ZUnit=0;

    private void maxminUnit()
    {
        int size=XArray_First.size();//获取数组长度
        if (size==0)
            return;
        Xmax=Math.max(Xmax,XArray_First.get(size-1));
        Xmin=Math.min(Xmin,XArray_First.get(size-1));
        Ymax=Math.max(Ymax,YArray_First.get(size-1));
        Ymin=Math.min(Ymin,YArray_First.get(size-1));
        Zmax=Math.max(Zmax,ZArray_First.get(size-1));
        Zmin=Math.min(Zmin,ZArray_First.get(size-1));

        double XUnit_temp=(Xmax-Xmin)/400;
        double YUnit_temp=(Ymax-Ymin)/400;
        double ZUnit_temp=(Zmax-Zmin)/400;
        if (XUnit_temp!=XUnit)//单位变化
        {
            XArray_Table.clear();
            for (int i=0;i<size;i++)
            {
                XArray_Table.add((int)(((XArray_First.get(i)-Xmin)/XUnit_temp)));
            }
            XUnit=XUnit_temp;
            isDraw=true;
        }
        if (XUnit_temp==XUnit&&size>1)//单位不变，说明数字在最大最小中间浮动
        {
            XArray_Table.add((int)(((XArray_First.get(size-1)-Xmin)/XUnit_temp)));
        }
        if (XUnit_temp==0&&size>1)//单位一直为零，数据不止一条，说明位置没有变化
        {
            XArray_Table.clear();
            for (int i=0;i<size;i++)
            {
                XArray_Table.add(200);
            }
            isDraw=true;
        }

        if (YUnit_temp!=YUnit)
        {
            YArray_Table.clear();
            for (int i=0;i<size;i++)
            {
                YArray_Table.add((int)(((YArray_First.get(i)-Ymin)/YUnit_temp)));
            }
            YUnit=YUnit_temp;
            isDraw=true;
        }
        if (YUnit_temp==YUnit&&size>1)
        {
            YArray_Table.add((int)(((YArray_First.get(size-1)-Ymin)/YUnit_temp)));
        }
        if (YUnit_temp==0&&size>1)
        {
            YArray_Table.clear();
            for (int i=0;i<size;i++)
            {
                YArray_Table.add(200);
            }
            isDraw=true;
        }
        if (ZUnit_temp!=ZUnit)
        {
            ZArray_Table.clear();
            for (int i=0;i<size;i++)
            {
                ZArray_Table.add((int)(((ZArray_First.get(i)-Zmin)/ZUnit_temp)));
            }
            ZUnit=ZUnit_temp;
            isDraw=true;
        }
        if (ZUnit_temp==ZUnit&&size>1)
        {
            ZArray_Table.add((int)(((ZArray_First.get(size-1)-Zmin)/ZUnit_temp)));
        }
        if (ZUnit_temp==0&&size>1)
        {
            ZArray_Table.clear();
            for (int i=0;i<size;i++)
            {
                ZArray_Table.add(200);
            }
            isDraw=true;
        }
        if (XArray_Table.size()>600)
        {
            XArray_Table.remove(0);
            YArray_Table.remove(0);
            ZArray_Table.remove(0);
        }

    }

    private void PathXYZ(Path XPath,Path YPath,Path ZPath)
    {
        int size=XArray_First.size();
        if (!isDraw)
        {
            return;
        }
        int BeginIndex=600-size;
        boolean isfirstpoint=true;
        for (int i=0;i<size;i++)
        {
            if (isfirstpoint)
            {
                XPath.moveTo(104+4*(BeginIndex+i),400-XArray_Table.get(i));
                YPath.moveTo(104+4*(BeginIndex+i),850-YArray_Table.get(i));
                ZPath.moveTo(104+4*(BeginIndex+i),1300-ZArray_Table.get(i));
                isfirstpoint=false;
            }
            else
            {
                XPath.lineTo(104+4*(BeginIndex+i),400-XArray_Table.get(i));
                YPath.lineTo(104+4*(BeginIndex+i),850-YArray_Table.get(i));
                ZPath.lineTo(104+4*(BeginIndex+i),1300-ZArray_Table.get(i));
            }
        }
    }
    private void initTXYZ(String newTime,double X,double Y,double Z)
    {
        TimeArray.add(newTime);
        XArray_First.add(X);
        YArray_First.add(Y);
        ZArray_First.add(Z);
        if (TimeArray.size()>600)
        {
            TimeArray.remove(0);
            XArray_First.remove(0);
            YArray_First.remove(0);
            ZArray_First.remove(0);
        }
    }
    //XYZ基准横线高度
    /*
    public int X=400;
    public int Y=850;
    public int Z=1300;*/
    private int T_youbiao=240;
    @Override
    // 重写该方法，进行绘图
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        try {
            canvas.drawColor(Color.rgb(38,38,38));
            Paint paint = new Paint();
            // 去锯齿
          //  paint.setAntiAlias(true);
            paint.setColor(Color.rgb(169,183,198));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            //X
                //横
            canvas.drawLine(100,400,2500,400,paint);
                //箭头
            canvas.drawLine(2490,390,2500,400,paint);
            canvas.drawLine(2490,410,2500,400,paint);
                //纵
            canvas.drawLine(100,400,100,50,paint);
                //箭头
            canvas.drawLine(90,60,100,50,paint);
            canvas.drawLine(110, 60, 100, 50, paint);

            //Y
            canvas.drawLine(100,850,2500,850,paint);
            canvas.drawLine(2490,840,2500,850,paint);
            canvas.drawLine(2490,860,2500,850,paint);
            canvas.drawLine(100,850,100,500,paint);
            canvas.drawLine(90,510,100,500,paint);
            canvas.drawLine(110,510,100,500,paint);
            //Z
            canvas.drawLine(100,1300,2500,1300,paint);
            canvas.drawLine(2490,1290,2500,1300,paint);
            canvas.drawLine(2490,1310,2500,1300,paint);
            canvas.drawLine(100,1300,100,950,paint);
            canvas.drawLine(90,960,100,950,paint);
            canvas.drawLine(110,960,100,950,paint);
            //刻度
            paint.setTextSize(25);
            paint.setStrokeWidth(0);
            canvas.drawText("E 东西",0,75,paint);
            canvas.drawText("N 南北",0,525,paint);
            canvas.drawText("M 高程",0,975,paint);
            paint.setStrokeWidth(5);
            paint.setShader(null);
            for (int i=1;i<10;i++)
            {
                int a=100+i*T_youbiao;
                canvas.drawLine(a,400,a,390,paint);
                canvas.drawLine(a,850,a,840,paint);
                canvas.drawLine(a,1300,a,1290,paint);
            }
            paint.setStrokeWidth(0);
            for (int i=0;i<Timenum;i++)
            {

                Date TimeDate=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").parse(TimeStr10.get(i));
                String TimeStr2=new SimpleDateFormat("HH:mm:ss").format(TimeDate);
                SimpleDateFormat formatter = new SimpleDateFormat("dd");
                String dd=formatter.format(new Date());
                String TimeStr=new SimpleDateFormat("dd").format(TimeDate);
                if (dd.equals(TimeStr))
                {
                    paint.setColor(Color.rgb(169,183,198));
                }
                else
                {
                    paint.setColor(Color.RED);
                }
                int a=2200-i*240;

                canvas.drawText(TimeStr2,a,435,paint);
                canvas.drawText(TimeStr2, a, 885, paint);
                canvas.drawText(TimeStr2,a,1335,paint);
                paint.setColor(Color.rgb(38,38,38));
            }
            paint.setColor(Color.rgb(169,183,198));
            paint.setStrokeWidth(5);
            for (int i=1;i<4;i++)
            {
                canvas.drawLine(100,400-100*i,110,400-100*i,paint);
                canvas.drawLine(100,850-100*i,110,850-100*i,paint);
                canvas.drawLine(100,1300-100*i,110,1300-100*i,paint);
                paint.setStrokeWidth(0);
                double X=100*i*XUnit+Xmin;
                int X_a=(int)X/60;
                double X_b=X%60;
                String X_str=Integer.toString(X_a)+"°"+Double.toString(X_b)+"'";
                double Y=100*i*YUnit+Ymin;
                int Y_a=(int)Y/60;
                double Y_b=Y%60;
                String Y_str=Integer.toString(Y_a)+"°"+Double.toString(Y_b)+"'";
                canvas.drawText(X_str,0,425-100*i,paint);
                canvas.drawText(Y_str,0,875-100*i,paint);
                canvas.drawText(Double.toString(100*i*ZUnit+Zmin),0,1325-100*i,paint);
                paint.setStrokeWidth(5);
            }
            if (isDraw)
            {
                Path XPath=new Path();
                Path YPath=new Path();
                Path ZPath=new Path();
                PathXYZ(XPath,YPath,ZPath);
                paint.setColor(Color.RED);
                paint.setStrokeWidth(5);
                canvas.drawPath(XPath, paint);

                paint.setColor(Color.YELLOW);
                canvas.drawPath(YPath,paint);

                paint.setColor(Color.BLUE);
                canvas.drawPath(ZPath,paint);

                paint.setStrokeWidth(0);
                paint.setColor(Color.GREEN);
                Date TimeDate=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").parse(TimeArray.get(TimeArray.size()-1));
                String TimeStr2=new SimpleDateFormat("HH:mm:ss").format(TimeDate);
                canvas.drawText(TimeStr2,2440,435,paint);
                canvas.drawText(TimeStr2,2440,885,paint);
                canvas.drawText(TimeStr2,2440,1335,paint);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }
}