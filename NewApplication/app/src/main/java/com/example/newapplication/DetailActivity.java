package com.example.newapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {
    private ImageView pic;
    private TextView name;
    private TextView follow;
    private TextView fans;
    private TextView sign;
    private TextView time;
    private User user;
    private Handler handler;
    private List<Depository> depositoryList = new ArrayList<>();
    private DepositoryAdapter depositoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");
        sendRequestWithOkHttp(userName);

    }

    public void  init(){
        pic = (ImageView)findViewById(R.id.pic);
        name = (TextView)findViewById(R.id.name);
        follow = (TextView)findViewById(R.id.follow);
        fans = (TextView)findViewById(R.id.fans);
        sign = (TextView)findViewById(R.id.sign);
        time = (TextView)findViewById(R.id.time);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        depositoryAdapter = new DepositoryAdapter(depositoryList);
        recyclerView.setAdapter(depositoryAdapter);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        pic.setImageBitmap(user.getPic());
                        name.setText("用户名:"+user.getName());
                        follow.setText("关注数量:"+user.getFollows());
                        fans.setText("粉丝数:"+user.getFans());
                        sign.setText("签名:"+user.getSign());
                        time.setText("加入时间:"+user.getTime());
                        break;
                    case 2:
                        depositoryAdapter.notifyDataSetChanged();
                        MainActivity.clctList = LitePal.findAll(Depository.class);
                        MainActivity.depositoryAdapter.notifyDataSetChanged();
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
                        OkHttpClient client = new OkHttpClient();
                        URL url = new URL("https://api.github.com/users/"+content);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url = new URL("https://api.github.com/users/"+content+"/"+"repos");
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        parseDepositoryWithJSONObject(responseData);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        private void parseJSONWithJSONObject(String jsonData){
            try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String userName = jsonObject.getString("login");
                    String userPic = jsonObject.getString("avatar_url");
                    String userFollow = jsonObject.getString("following");
                    String userFans = jsonObject.getString("followers");
                    String userSign = jsonObject.getString("bio");
                    String time = jsonObject.getString("created_at");
                    Bitmap picture = MyApplication.scaleBitmap(MyApplication.getImage(userPic),600,600);
                    user = new User(userName,picture,userSign,userFollow,userFans,time);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
            }catch (Exception e){
                e.printStackTrace();}
        }

        private void parseDepositoryWithJSONObject(String jsonData){
            try {
                JSONArray jsonArray1 = new JSONArray(jsonData);
                for(int i = 0;i < jsonArray1.length();i++){
                    JSONObject obj = jsonArray1.getJSONObject(i);
                    String name = obj.getString("name");
                    JSONObject owner = obj.getJSONObject("owner");
                    String author = owner.getString("login");
                    String language = obj.getString("language");
                    String fork = obj.getString("forks_count");
                    String star = obj.getString("stargazers_count");
                    depositoryList.add(new Depository(name,author,language,fork,star));
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();}
        }

    }


