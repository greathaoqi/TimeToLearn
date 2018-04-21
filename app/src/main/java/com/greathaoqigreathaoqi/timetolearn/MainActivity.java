package com.greathaoqigreathaoqi.timetolearn;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.greathaoqigreathaoqi.timetolearn.DemoAdapter.ViewHolder;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements OnClickListener,
        OnItemClickListener {
    List<ResolveInfo> apps = new ArrayList<ResolveInfo>();
    int num_App=0;

    /**
     * shell按钮
     */
    private ViewGroup btnCancle = null;

    /**
     * 设置时间按钮
     */
    private ViewGroup btnAdd = null;

    /**
     * 选择所有
     */
    private Button btnSelectAll = null;

    /**
     * 删除
     */
    private Button btnDelete = null;

    private Button btnmode =null;

    /**
     * ListView列表
     */
    private ListView lvListView = null;

    /**
     * 适配对象
     */
    private DemoAdapter adpAdapter = null;

    private TextView time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化视图
        initView();
        // 初始化控件
        initData();
        saveName();
    }

    private void saveName() {
        {
            FileOutputStream out = null;
            BufferedWriter writer = null;
            try {
                out = openFileOutput("ListApp.txt", Context.MODE_PRIVATE);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                for (int i = 0; i < apps.size(); i++) {
                    ResolveInfo info = apps.get(i);
                    String packageName = info.activityInfo.packageName;
                    CharSequence cls = info.activityInfo.name;
                    CharSequence name = info.activityInfo.loadLabel(getPackageManager());
                    writer.write("packageName: " + packageName + "     name: " + name + "\r\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {

        btnCancle = (ViewGroup) findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(this);

        btnAdd = (ViewGroup) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnmode = findViewById(R.id.mode);
        btnmode.setOnClickListener(this);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
        btnSelectAll.setOnClickListener(this);

        lvListView = (ListView) findViewById(R.id.lvListView);
        lvListView.setOnItemClickListener(this);

        time=findViewById(R.id.left_time);
        time.setText("10s");
    }
    //载入app
    private void loadApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        apps = getPackageManager().queryIntentActivities(intent, 0);
        //for循环遍历ResolveInfo对象获取包名和类名
        num_App = apps.size();/*
        for (int i = 0; i < apps.size(); i++) {
            ResolveInfo info = apps.get(i);
            String packageName = info.activityInfo.packageName;
            CharSequence cls = info.activityInfo.name;
            CharSequence name = info.activityInfo.loadLabel(getPackageManager());
        }*/
    }
    /**
     * 初始化视图
     */
    private void initData() {
        loadApp();

        // 数据
        List<DemoBean> demoDatas = new ArrayList<DemoBean>();

        for(int i =0;i<num_App;i++){
            demoDatas.add(new DemoBean(apps.get(i).activityInfo.loadLabel(getPackageManager()).toString(), true));
        }

        adpAdapter = new DemoAdapter(this, demoDatas);

        lvListView.setAdapter(adpAdapter);

    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {

		/*
		 * 当点击Shell的时候
		 */
        if (v == btnCancle) {
            Intent intent = new Intent(this,ShellActivity.class);
            startActivity(intent);
        }

		/*
		 * 当点击设置时间的时候
		 */
        if (v == btnAdd) {
            showDialog();

            adpAdapter.notifyDataSetChanged();
        }

        if(v==btnmode){
            Intent intent = new Intent(this,mode.class);
            startActivity(intent);
        }

		/*
		 * 当点击隐藏的时候
		 */
        if (v == btnDelete) {


			/*
			 * 拿到checkBox选择寄存map
			 */
            Map<Integer, Boolean> map = adpAdapter.getCheckMap();
            Intent myintent = new Intent();
            // 获取当前的数据数量
            int count = adpAdapter.getCount();
            int c =0 ;

            // 进行遍历
            for (int i = 0; i < count; i++) {
                if (map.get(i) != null && map.get(i)) {
                    c++;
                    String string= apps.get(i).activityInfo.packageName;
                    myintent.putExtra(String.valueOf(c),string);
                    Log.e(TAG, "put: "+string+"        " +c);
                    try {
                        hide(string);
                        saveLog(string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    Log.e(TAG, "onClick: "+string);
                }
            }
//定时执行任务
            myintent.putExtra("count",c);
            AlarmManager am=  (AlarmManager)getSystemService(ALARM_SERVICE);

            PendingIntent pi;
            myintent.setClass(this,armSer.class);
            pi = PendingIntent.getBroadcast(this,0,myintent,0);
            long ac=System.currentTimeMillis();
            long ab= Long.parseLong(time.getText().toString().replace("s",""));
            am.set(AlarmManager.RTC_WAKEUP,ac+ab,pi);
            adpAdapter.notifyDataSetChanged();
        }

		/*
		 * 当点击全选的时候
		 */
        if (v == btnSelectAll) {

            if (btnSelectAll.getText().toString().trim().equals("全选")) {

                // 所有项目全部选中
                adpAdapter.configCheckMap(true);

                adpAdapter.notifyDataSetChanged();

                btnSelectAll.setText("全不选");
            } else {

                // 所有项目全部不选中
                adpAdapter.configCheckMap(false);

                adpAdapter.notifyDataSetChanged();

                btnSelectAll.setText("全选");
            }

        }
    }
//保存日志
    private void saveLog(String string) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{out = openFileOutput("Log.txt", Context.MODE_APPEND);
        writer=new BufferedWriter(new OutputStreamWriter(out));
        writer.write(getStringDate(System.currentTimeMillis())+": "+string+"\r\n");}catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//显示出时间框图
    private void showDialog() {
        DateTimePickerDialog dialog  = new DateTimePickerDialog(this, System.currentTimeMillis());
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener()
        {
            public void OnDateTimeSet(AlertDialog dialog, long date)
            {
                long leftTime = (date-System.currentTimeMillis())/1000;
                Log.e(TAG, "OnDateTimeSet: "+leftTime );
                time.setText(""+leftTime+"s");
                Toast.makeText(MainActivity.this, "您输入的日期是："+getStringDate(date), Toast.LENGTH_LONG).show();
            }

        });
        dialog.show();
    }
//日期格式转换
    public String getStringDate(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);

        return dateString;
    }

    //隐藏程序
    private void hide(String string) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = null;
        os = new DataOutputStream(process.getOutputStream());
        Log.e(TAG, "hide: "+string);
        string = "pm hide "+ string;
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

    /**
     * 当ListView 子项点击的时候
     */
    @Override
    public void onItemClick(AdapterView<?> listView, View itemLayout,
                            int position, long id) {

        if (itemLayout.getTag() instanceof ViewHolder) {

            ViewHolder holder = (ViewHolder) itemLayout.getTag();

            // 会自动出发CheckBox的checked事件
            holder.cbCheck.toggle();

        }

    }
}
