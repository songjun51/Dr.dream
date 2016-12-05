package kr.edcan.drdream;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by songjun on 2016. 11. 30..
 */

public class DataManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context c;

    public DataManager(Context c) {
        this.c = c;
        pref = c.getSharedPreferences("drdream", 0);
        editor = pref.edit();
    }

    public int getTemp() {
        return ((pref.getInt("temp", -1) != -1) ? pref.getInt("temp", -1):-1);
    }
    public int getHumid() {
        return ((pref.getInt("humid", -1) != -1) ? pref.getInt("humid", -1):-1);
    }
    public void setTemp(int temp){
        editor.putInt("temp", temp);
        editor.apply();
    }
    public void setHumid(int humid){
        editor.putInt("humid", humid);
        editor.apply();
    }

}
