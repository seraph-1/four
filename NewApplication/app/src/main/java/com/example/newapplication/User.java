package com.example.newapplication;

import android.graphics.Bitmap;

public class User {
    private String name;
    private Bitmap pic;
    private String sign;
    private String follows;
    private String fans;
    private String time;

    User(String name, Bitmap pic)
    {
        this.name = name;
        this.pic = pic;
        this.sign = this.follows = this.fans = this.time = null;
    }

    User(String name,Bitmap pic,String sign,String follows,String fans,String time)
    {
        this.name = name;
        this.pic = pic;
        this.sign = sign;
        this.follows = follows;
        this.fans = fans;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public String getSign() {
        return sign;
    }

    public String getFollows() {
        return follows;
    }

    public String getFans() {
        return fans;
    }

    public String getTime() {
        return time;
    }
}
