package com.example.newapplication;
import org.litepal.crud.LitePalSupport;
public class Depository extends LitePalSupport{
    private String name;
    private String author;
    private String language;
    private String fork;
    private String star;

    public Depository(String name,String author,String language,String fork,String star)
    {
        this.name=name;
        this.author=author;
        this.language=language;
        this.fork=fork;
        this.star=star;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getAuthor() {
        return author;
    }

    public String getStar() {
        return star;
    }

    public String getFork() {
        return fork;
    }
}
