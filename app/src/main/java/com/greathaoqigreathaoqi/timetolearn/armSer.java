package com.greathaoqigreathaoqi.timetolearn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class armSer extends BroadcastReceiver {
    //接受广播
    @Override
    public void onReceive(Context context, Intent intent) {
        int count = intent.getIntExtra("count",-1);
        Log.e(TAG, "onReceive: "+count );
        if(count>0){
            for(int i=0;i<count;i++){
                String kkk = intent.getStringExtra(String.valueOf(i+1));
                try {
                    unhide(kkk);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //显示程序
    private void unhide(String string) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = null;
        os = new DataOutputStream(process.getOutputStream());
        string = "pm unhide "+ string;
        try{
            os.write(string.getBytes());
            os.writeBytes("\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();}catch (Exception e){}
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
