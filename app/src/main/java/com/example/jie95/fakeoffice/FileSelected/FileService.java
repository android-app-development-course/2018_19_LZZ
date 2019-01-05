package com.example.jie95.fakeoffice.FileSelected;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FileService {
    private DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase = null;

    public FileService(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * 保存日记
     *
     * @param filename
     */

    public void save(Filename filename) {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "insert into tb_diary(title,path,pubdate) values (?,?,?)";
        sqLiteDatabase.execSQL(
                sql,
                new String[] { filename.getTitle(),
                        filename.getPath(),
                        filename.getPubdate() });
        sqLiteDatabase.close();

    }

    public void savetorecyle(Filename filename) {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "insert into tb_file(title,path,pubdate) values (?,?,?)";
        sqLiteDatabase.execSQL(
                sql,
                new String[] { filename.getTitle(),
                        filename.getPath(),
                        filename.getPubdate() });
        sqLiteDatabase.close();

    }
    // 查询日志
    public List<Filename> getAllFile() {
        Filename filename = null;
        List<Filename> diaryList = new ArrayList<Filename>();
        sqLiteDatabase = dbHelper.getReadableDatabase();
        // 得到游标，最多只有一条数据
        Cursor cursor = sqLiteDatabase.rawQuery("select * from tb_file", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title =cursor.getString(cursor.getColumnIndex("title"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            String pubdate = cursor.getString(cursor.getColumnIndex("pubdate"));
            filename = new Filename(id,title, path, pubdate);
            diaryList.add(filename);
        }
        cursor.close();
        sqLiteDatabase.close();

        return diaryList;
    }

    // 查询日志
    public List<Filename> getAllDiary() {
        Filename filename = null;
        List<Filename> diaryList = new ArrayList<Filename>();
        sqLiteDatabase = dbHelper.getReadableDatabase();
        // 得到游标，最多只有一条数据
        Cursor cursor = sqLiteDatabase.rawQuery("select * from tb_diary", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title =cursor.getString(cursor.getColumnIndex("title"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            String pubdate = cursor.getString(cursor.getColumnIndex("pubdate"));
            filename = new Filename(id,title, path, pubdate);

            diaryList.add(filename);
        }
        cursor.close();
        sqLiteDatabase.close();

        return diaryList;

    }

    /**
     * 根据id删除日记
     *
     * @param id
     *            日记的id号
     */
    public void delete(Integer id) {
        sqLiteDatabase = dbHelper.getReadableDatabase();
        sqLiteDatabase.execSQL("delete from tb_diary where _id=?",
                new Object[] { id });
        sqLiteDatabase.close();

    }
    public void delete_recycle(Integer id) {
        sqLiteDatabase = dbHelper.getReadableDatabase();
        sqLiteDatabase.execSQL("delete from tb_file where _id=?",
                new Object[] { id });
        sqLiteDatabase.close();

    }

//    public Diary search_id(String ab)
//    {
//
//        Diary diary = null;
//        List<Diary> diaryList = new ArrayList<Diary>();
//
//        sqLiteDatabase = dbHelper.getReadableDatabase();
//        Cursor cursor =sqLiteDatabase.rawQuery("select * from tb_diary where title like ?",new String[]{ab});
//
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String title = cursor.getString(cursor.getColumnIndex("title"));
//            String content = cursor.getString(cursor.getColumnIndex("content"));
//            String pubdate = cursor.getString(cursor.getColumnIndex("pubdate"));
//
//            diary = new Diary(id, title, content, pubdate);
//            diaryList.add(diary);
//        }
//        sqLiteDatabase.close();
//        cursor.close();
//        return diary;
//    }
    /**
     * 更新日记
     *
     * @param diary
     */
//    public void update(Diary diary) {
//        sqLiteDatabase = dbHelper.getReadableDatabase();
//        sqLiteDatabase.execSQL(
//                "update tb_diary set title=?,content=?,pubdate=? where _id=?",
//                new Object[] { diary.getTitle(), diary.getContent(),
//                        diary.getPubdate(), diary.getId() });
//        sqLiteDatabase.close();
//
//    }

}

