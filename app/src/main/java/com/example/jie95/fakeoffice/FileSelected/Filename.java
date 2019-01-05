package com.example.jie95.fakeoffice.FileSelected;


public class Filename {
    private int id;
    private String title;
    private String path;
    private String pubdate;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getPubdate() {
        return pubdate;
    }
    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public Filename(String title,String path, String pubdate) {
        super();
        this.title=title;
        this.path = path;
        this.pubdate = pubdate;
    }

    public Filename(String path, String pubdate) {
        super();
        this.path = path;
        this.pubdate = pubdate;
    }
    public Filename(int id,String path) {
        super();
        this.id=id;
        this.path = path;
    }
    public Filename(int id,String title,String path) {
        super();

        this.title=title;
        this.id=id;
        this.path = path;
    }
    public Filename(int id, String title,String path, String pubdate) {
        super();

        this.id = id;
        this.title=title;
        this.path = path;
        this.pubdate = pubdate;
    }
    public Filename()
    {

    }

}
