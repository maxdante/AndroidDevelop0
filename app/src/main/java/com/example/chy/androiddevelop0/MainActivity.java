package com.example.chy.androiddevelop0;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.example.chy.androiddevelop0.Data.ClientThread;
import com.example.chy.androiddevelop0.Data.DBManager;
import com.example.chy.androiddevelop0.Draw.DrawView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //百度地图
    MapView mMapView = null;
    MKOfflineMap mOffline = null;
    BaiduMap mBaiduMap;
    private ArrayList<MKOLUpdateElement> localMapList = null;

    //定位相关
    public LatLng mCurrentLocation;
    boolean isFirstLoc = true;// 是否首次定位
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
    //定时器
    public Timer mDrawPointTimer;

    //画图
    public DrawView mDrawView;
    public String mIPAddress = "36.107.41.151";
    public int mPort = 5017;
    public static TextView mConnectionStatue;
    public Thread mThread;
    //IP 端口设置
    public AlertDialog mAlertDialog;
    public EditText mcIpAddress;
    public EditText mcPort;
    public ProfileReader mProfileReader;
    //按钮部分
    public LinearLayout mButtonLayout;
    public ButtonAdd mButtonAdd;
    //当前viewID
    public int mCurrentViewID;

    //基站定位
    LatLng mLastLocaion =null;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mainView_init();
        Ip_PortLayout_init();
        mContext = this;
    }

    @Override
    public void onStart(){
        super.onStart();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ChangeCurrentView(R.layout.activity_wave_graphic);
            graphicView_init();
            return true;
        }
        else if (id == R.id.action_settings2){
            ChangeCurrentView(R.layout.activity_main);
            mainView_init();
            return true;
        }
        else if (id == R.id.action_settings3){
            if(ClientThread.isConnected) {
                Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else if (id == R.id.action_settings4){
            mAlertDialog.show();
            Map<String,String> param= mProfileReader.getPreferences();
            try {
                mcIpAddress = (EditText)mAlertDialog.findViewById(R.id.ip);
                mcPort = (EditText)mAlertDialog.findViewById(R.id.port);
                mcIpAddress.setText(param.get("ipAddress"));
                mcPort.setText(param.get("port"));
            }catch (Exception ex){

            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }



    public void  OnClick(View v){
        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();

        }
        setmCurrentLocation(mLocClient,0);
        //mDrawPointTimer.schedule(new timerListener(),1000,2000);
        Toast.makeText(getApplicationContext(), "ddd",Toast.LENGTH_SHORT).show();
        MapStatus currentMapStatus = new MapStatus.Builder().target(mCurrentLocation).zoom(18).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(currentMapStatus);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));


    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void setmCurrentLocation(LocationClient client,double offset){
        mCurrentLocation  = new LatLng(client.getLastKnownLocation().getLatitude()+offset,client.getLastKnownLocation().getLongitude());
    }

    private void mainView_init(){
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mOffline = new MKOfflineMap();
        mOffline.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int type, int state) {
                switch (type) {
                    case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                        MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                        //mText.setText(String.format("%s : %d%%", update.cityName, update.ratio));
                    }
                    break;
                    case MKOfflineMap.TYPE_NEW_OFFLINE:
                        Log.d("OfflineDemo", String.format("add offlinemapnum:%d", state));
                        break;
                    case MKOfflineMap.TYPE_VER_UPDATE:
                        Log.d("OfflineDemo", String.format("new offlinemapver"));
                        break;
                }
            }
        });

        mBaiduMap.setMaxAndMinZoomLevel(19,1);
        //读取离线地图
        mOffline.importOfflineData();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        // 定位初始化
        mCurrentLocation = new LatLng(0,0);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocClient.setLocOption(option);
        mLocClient.start();

        //定时器
        mDrawPointTimer = new Timer();

        //Button_init
        mButtonLayout = (LinearLayout)findViewById(R.id.button_layout);
        mButtonAdd = Button_init();
        mButtonAdd.addToView(mButtonLayout);
    }

    private void graphicView_init(){
        //
        mConnectionStatue = (TextView)findViewById(R.id.textViewc);
        //连接检查定时器
        mDrawView = (DrawView)findViewById(R.id.graphic);
        Map<String,String> param= mProfileReader.getPreferences();
        mIPAddress = param.get("ipAddress");
        mPort = Integer.parseInt(param.get("port"));
        if(Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try
        {
            DBManager dbManager=new DBManager();
            SQLiteDatabase db=dbManager.CreateDB();
            dbManager.createTable(db);
            mThread =  new Thread(new ClientThread(mIPAddress,mPort,dbManager,db,mDrawView));
            mThread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    class timerListener extends TimerTask {
        double offset = 0.001;
        @Override
        public void run(){
            /*
            setmCurrentLocation(mLocClient,offset);
            offset +=0.0001;
            OverlayOptions point = new DotOptions().center(mCurrentLocation).color(0xAAFFFF00).radius(50);
            mBaiduMap.clear();
            mBaiduMap.addOverlay(point);
            */
            if (!mThread.isAlive()){
                System.out.println("线程死亡");
                //mConnectionStatue.setText("线程死亡");
                try
                {
                    DBManager dbManager=new DBManager();
                    SQLiteDatabase db=dbManager.CreateDB();
                    dbManager.createTable(db);
                    mThread =  new Thread(new ClientThread(mIPAddress,mPort,dbManager,db,mDrawView));
                    mThread.start();
                    mDrawPointTimer.cancel();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }
    }


    private void Ip_PortLayout_init(){

        mProfileReader = new ProfileReader(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.ip_port_set, null);
        mAlertDialog = new AlertDialog.Builder(this).setTitle("IP端口设定").setView(layout, 0, 0, 0, 0)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String AddressString = mcIpAddress.getText().toString();
                        String PortString = mcPort.getText().toString();
                        mProfileReader.save(AddressString, Integer.parseInt(PortString));
                        mButtonAdd.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                public void  onClick(DialogInterface dialog, int whch){
                        mButtonAdd.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
                    }
                }).create();
    }

    public ButtonAdd Button_init(){
        ArrayList<Button> btnlist = new ArrayList<>();
        Button btn1 = new Button(this);
        btn1.setText("当前位置");
        //btn1.setTextColor(Color.BLACK);
        btn1.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set pressed button background image
                mButtonAdd.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
                v.setBackground(getResources().getDrawable(R.drawable.button_bg3,null));
                //
                setmCurrentLocation(mLocClient, 0);
                MapStatus currentMapStatus = new MapStatus.Builder().target(mCurrentLocation).zoom(18).build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(currentMapStatus);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));

            }
        });
        btnlist.add(btn1);


        Button btn2 = new Button(this);
        btn2.setText("切换图表");
        //btn2.setTextColor(Color.BLACK);
        btn2.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set pressed button background image
                mButtonAdd.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
                v.setBackground(getResources().getDrawable(R.drawable.button_bg3,null));
                //
                setContentView(R.layout.activity_wave_graphic);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                graphicView_init();
                mDrawPointTimer.schedule(new timerListener(),1000,2000);
            }
        });
        btnlist.add(btn2);

        Button btn_fake_Location = new Button(this);
        btn_fake_Location.setText("基站定位");
        //btn_fake_Location.setTextColor(Color.BLACK);
        btn_fake_Location.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
        btn_fake_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set pressed button background image
                if (NetWorkConnection.isConnect(mContext)==false)
                {
                    new AlertDialog.Builder(mContext)
                            .setTitle("网络错误")
                            .setMessage("网络连接失败，请确认网络连接")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    //android.os.Process.killProcess(android.os.Process.myPid());
                                    //System.exit(0);
                                }
                            }).show();
                }
                else {
                    mButtonAdd.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
                    v.setBackground(getResources().getDrawable(R.drawable.button_bg3,null));
                    //
                    mBaiduMap.clear();
                    LatLng fake_Location =  new LatLng(26.4767370000,106.6791850000);
                    //LatLng Location0 =  new LatLng(26.4739476560078,106.669090700172);
                    LatLng Location1 =  new LatLng(26.4093180618085,106.256874268727);
                    LatLng Location2 =  new LatLng(26.2532125634427,105.937152536157);
                    LatLng Location3 =  new LatLng(26.0277585343974,106.440282813349);
                    createPointOnMap(fake_Location,mBaiduMap);
                    //createPointOnMap(Location0,mBaiduMap);
                    createPointOnMap(Location1,mBaiduMap);
                    createPointOnMap(Location2,mBaiduMap);
                    createPointOnMap(Location3,mBaiduMap);


                    //在点之间切换  需要优化
                    if(mLastLocaion == null){
                        locateOnMap(fake_Location,mBaiduMap);
                    }else if (mLastLocaion.latitude == fake_Location.latitude){
                        locateOnMap(Location1,mBaiduMap);
                    }else if (mLastLocaion.latitude == Location1.latitude){
                        locateOnMap(Location2,mBaiduMap);
                    }else if(mLastLocaion.latitude == Location2.latitude){
                        locateOnMap(Location3,mBaiduMap);
                    }else{
                        locateOnMap(fake_Location,mBaiduMap);
                    }
                }



            }
        });
        btnlist.add(btn_fake_Location);

        Button btn3 = new Button(this);
        btn3.setText("IP设置");
        //btn3.setTextColor(Color.BLACK);
        btn3.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set pressed button background image
                mButtonAdd.setBackground(getResources().getDrawable(R.drawable.button_bg4,null));
                v.setBackground(getResources().getDrawable(R.drawable.button_bg3,null));
                //
                mAlertDialog.show();
                Map<String,String> param= mProfileReader.getPreferences();
                try {
                    mcIpAddress = (EditText)mAlertDialog.findViewById(R.id.ip);
                    mcPort = (EditText)mAlertDialog.findViewById(R.id.port);
                    mcIpAddress.setText(param.get("ipAddress"));
                    mcPort.setText(param.get("port"));
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnlist.add(btn3);
        ButtonAdd ba = new ButtonAdd();
        ba.buttonListAdd(btnlist);
        return ba;
    }

    public void ChangeCurrentView(int id){
        setContentView(id);
        mCurrentViewID = id;
    }
    public void getBack(View v){
        setContentView(R.layout.activity_main);
        mainView_init();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    }

    public void createPointOnMap(LatLng location_point,BaiduMap map){
        map.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        OverlayOptions point = new DotOptions().center(location_point).color(0x550055FF).radius(30);
        OverlayOptions point2 = new DotOptions().center(location_point).color(0xFF0055FF).radius(10);
        //map.clear();
        map.addOverlay(point);
        map.addOverlay(point2);
    }

    public void locateOnMap(LatLng location_point,BaiduMap map){
        MapStatus currentMapStatus = new MapStatus.Builder().target(location_point).zoom(12).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(currentMapStatus);
        map.animateMapStatus(mapStatusUpdate);
        mLastLocaion = location_point;
    }



}
