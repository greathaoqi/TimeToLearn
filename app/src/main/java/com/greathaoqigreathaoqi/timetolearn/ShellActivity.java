package com.greathaoqigreathaoqi.timetolearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellActivity extends AppCompatActivity {

    MultiAutoCompleteTextView shell;
    private TextView outshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);
        outshow = findViewById(R.id.out);
        Button button = findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shell = findViewById(R.id.shellText);
                String commend =  shell.getText().toString();
                try {
                    start(commend);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Button setting =findViewById(R.id.set);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShellActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void start(String commend) throws IOException {
        Process p = Runtime.getRuntime().exec("pm hide com.android.chrome");
        String data = null;
        BufferedReader ie = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String error = null;
        while ((error = ie.readLine()) != null
                && !error.equals("null")) {
            data += error + "\n";
        }
        String line = null;
        while ((line = in.readLine()) != null
                && !line.equals("null")) {
            data += line + "\n";
        }
        outshow.setText(data);
        /*
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream os = null;
        os = new DataOutputStream(process.getOutputStream());
        try{
            os.write(commend.getBytes());
            os.writeBytes("\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();}catch (Exception e){}
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
