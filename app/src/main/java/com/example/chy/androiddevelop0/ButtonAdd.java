package com.example.chy.androiddevelop0;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.Exception;import java.lang.Override;import java.util.ArrayList;

/**
 * Created by chy on 2015/9/7.
 */
public class ButtonAdd implements ButtonAddInterface{
    private ArrayList<Button> btnList;
    public boolean isVisible = true;
    public int Orientation = LinearLayout.HORIZONTAL;
    public int btnWidth = 170;
    public ButtonAdd(){
        btnList = new ArrayList<>();
    }

    public void buttonListAdd(Button btn){
        btnList.add(btn);
    }

    public void buttonListAdd(ArrayList<Button> List){
        for (int i =0;i< List.size();i++){
            btnList.add(List.get(i));
        }
    }

    public int buttonListLength(){
        return btnList.size();
    }

    public void clearAll(){
        btnList.clear();
    }

    public ArrayList<Button> buttonList(){
        return btnList;
    }

    public void buttonHide(){
        for (int i = 0;i<btnList.size();i++){
            btnList.get(i).setVisibility(View.INVISIBLE);
        }
        isVisible = false;
    }
    public void buttonShow(){
        for (int i = 0;i<btnList.size();i++){
            btnList.get(i).setVisibility(View.VISIBLE);
        }
        isVisible = true;
    }

    public void setOrientation(int num){
        Orientation = num;
    }

    public void buttonResize(int size){
        btnWidth = size;
        for (int i = 0;i<btnList.size();i++){
            btnList.get(i).setWidth(btnWidth);
        }
    }

    public void setBackground(Drawable background){
        for (int i = 0;i<btnList.size();i++){
            btnList.get(i).setBackground(background);
        }
    }

    @Override
    public void addToView(LinearLayout v,Button btn){
        try {
            btnList.add(btn);
            v.addView(btn);
            v.setOrientation(Orientation);

        }
        catch (Exception ex){

        }

    }

    @Override
    public  void addToView(LinearLayout v){
        try {
            for (int i = 0;i<btnList.size();i++){
                v.addView(btnList.get(i));
            }
            v.setOrientation(Orientation);
        }
        catch (Exception ex){

        }
    }


}
