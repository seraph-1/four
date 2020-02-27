package com.example.newapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.litepal.LitePal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private List<User> userList = new ArrayList<>();
    static List<Depository>clctList = new ArrayList<>();
    static DepositoryAdapter depositoryAdapter;
    private DrawerLayout drawerLayout;
    private UserAdapter userAdapter;
    private RecyclerView recyclerView_clct;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home);
        }
        initRecycleView();
        search();
    }

    public void initRecycleView(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);
        recyclerView_clct = (RecyclerView)findViewById(R.id.clct_view);
        LinearLayoutManager layoutManager_clct = new LinearLayoutManager(this);
        recyclerView_clct.setLayoutManager(layoutManager_clct);
        LitePal.getDatabase();
        clctList = LitePal.findAll(Depository.class);
        depositoryAdapter = new DepositoryAdapter(clctList);
        recyclerView_clct.setAdapter(depositoryAdapter);
    }

    public void search(){
        Button botton = (Button)findViewById(R.id.search_request);
        final EditText editText = (EditText)findViewById(R.id.search_content);
        botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userList.clear();
                userAdapter.notifyDataSetChanged();
                sendRequestWithOkHttp(editText.getText().toString());

            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:userAdapter.notifyDataSetChanged();
                    break;
                    default:break;
                }
            }
        };
    }

    private void sendRequestWithOkHttp(final String content){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL("https://api.github.com/search/users?q="+content);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                    }
                catch (Exception e){
                e.printStackTrace();
            }
        }
        }).start();
    }

    public void parseJSONWithJSONObject(String jsonData){
        try {
            JSONObject obj = new JSONObject(jsonData);
            JSONArray jsonArray = obj.getJSONArray("items");
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("login");
                String pic = jsonObject.getString("avatar_url");
                Bitmap picture = MyApplication.scaleBitmap(MyApplication.getImage(pic),100,100);
                userList.add(new User(name,picture));
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }catch (Exception e){
            e.printStackTrace();}
    }



    @Override
    protected void onResume(){
        clctList = LitePal.findAll(Depository.class);
        depositoryAdapter = new DepositoryAdapter(clctList);
        recyclerView_clct.setAdapter(depositoryAdapter);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:break;
        }
        return true;
    }
}
