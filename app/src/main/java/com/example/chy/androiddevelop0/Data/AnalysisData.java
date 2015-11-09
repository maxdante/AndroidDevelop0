package com.example.chy.androiddevelop0.Data;

/**
 * Created by zhibing on 2015-09-14.
 */
public class AnalysisData {
    private String message;
    public AnalysisData(String msg){
        this.message=msg;

    }
    public String[] Analysis(){
        //TABLE_XYZ table_xyz;
        String[] strs=new String[4];
        //取出N的坐标
        int position_N=message.indexOf("N");
        int last_N=message.lastIndexOf(",",position_N);
        int lastsecond_N=message.lastIndexOf(",",last_N-1);
        String North=message.substring(lastsecond_N+1,last_N);
        //取出E的坐标
        int postion_E=message.indexOf("E");
        int last_E=message.lastIndexOf(",",postion_E);
        int lastSecond_E=message.lastIndexOf(",",last_E-1);
        String Easth=message.substring(lastSecond_E+1,last_E);

        //取出M的坐标
        String Moutin,Moutin2;
        try {
            int postion_M=message.indexOf("M");
            int last_M=message.lastIndexOf(",",postion_M);
            int lastsecond_M=message.lastIndexOf(",",last_M-1);
            Moutin=message.substring(lastsecond_M+1,last_M);
            //取出最后一个M的坐标
            int postion_M2=message.lastIndexOf("M");
            int last_M2=message.lastIndexOf(",",postion_M2);
            int lastsecond_M2=message.lastIndexOf(",",last_M2-1);
            Moutin2=message.substring(lastsecond_M2+1,last_M2);
        }
        catch (Exception e){
            Moutin=null;
            Moutin2=null;
        }
        if(North.equals("")||North==null){
            strs[0]="0";
        }
        strs[0]=North;
        strs[1]=Easth;
        strs[2]=Moutin;
        strs[3]=Moutin2;

        return strs;
    }
}
