package com.example.chy.androiddevelop0;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chy on 2015/9/21.
 */
public class ProfileReader {
    private Context context;
    public ProfileReader(Context context) {
        super();
        this.context = context;
    }
    /**
     * 保存参数
     * @param ipAddress IP地址
     * @param port  端口号
     */
    public void save(String ipAddress, int port) {
        //第一个参数 指定名称 不需要写后缀名 第二个参数文件的操作模式
        SharedPreferences preferences=context.getSharedPreferences("itcast", Context.MODE_PRIVATE);
        //取到编辑器
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("ipAddress", ipAddress);
        editor.putInt("port", port);
        //把数据提交给文件中
        editor.commit();
    }
    /**
     * 获取各项配置参数
     * @return
     */
    public Map<String,String> getPreferences(){
        SharedPreferences pre=context.getSharedPreferences("itcast", Context.MODE_PRIVATE);
        //如果得到的ipAddress没有值则设置为空 pre.getString("ipAddress", "");
        Map<String,String> params=new HashMap<String,String>();
        params.put("ipAddress", pre.getString("ipAddress", "192.168.2.1"));
        params.put("port", String.valueOf(pre.getInt("port", 200)));
        return params;
    }

}
