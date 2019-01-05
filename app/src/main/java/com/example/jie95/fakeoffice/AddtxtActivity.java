package com.example.jie95.fakeoffice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jie95.fakeoffice.FileSelected.FileService;
import com.example.jie95.fakeoffice.FileSelected.Filename;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddtxtActivity extends AppCompatActivity {

    private EditText edittitle,editcontent;
    String path,title,content;
    String savepath,truepath;
    FileService fileService;
    private BoomMenuButton boomMenuButton;
    private int[] res={R.drawable.ic_add,R.drawable.ic_delete,R.drawable.ic_newtxt};
    private String[] text={"添加文件","删除指定文件","新建文件(TXT)"};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtxt);

        Typeface mTypeface=Typeface.createFromAsset(getAssets(),"lingdong.ttf");
        try{
            Field field=Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null,mTypeface);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        /*Typeface mTypeface=Typeface.createFromAsset(getAssets(),"font/lingdong.ttf");
        try{
            Field field=Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null,mTypeface);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }*/

        edittitle=(EditText) this.findViewById(R.id.title1);
        editcontent=(EditText) this.findViewById(R.id.content1);

        boomMenuButton=(BoomMenuButton)findViewById(R.id.boom);
        boomMenuButton.setButtonEnum(ButtonEnum.TextOutsideCircle);
        boomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
        boomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.SC_1);
        TextOutsideCircleButton.Builder builder=new TextOutsideCircleButton.Builder() ;
        builder=new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                title=edittitle.getText().toString();
                content=editcontent.getText().toString();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType(“image/*”);//选择图片
                //intent.setType(“audio/*”); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.setType(“video/*;image/*”);//同时选择视频和图片
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        })
                .normalImageRes(res[2])
                .normalText(text[2]);
            boomMenuButton.addBuilder(builder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // 创建菜单，并设置图表
        menu.add(0, R.id.addfile, 0, R.string.add).setIcon(
                android.R.drawable.ic_input_add);
        return true;
    }

    public static void stringTxt(String str,String path,String title){
        try {
            FileWriter fw = new FileWriter(path+"/"+title+".txt");
            fw.flush();
            fw.write(str);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addfile:

                title=edittitle.getText().toString();
                content=editcontent.getText().toString();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType(“image/*”);//选择图片
                //intent.setType(“audio/*”); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.setType(“video/*;image/*”);//同时选择视频和图片
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        savepath="";
        truepath="";
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
//            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
//                path = uri.getPath();
//                tv.setText(path);
//                Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
//                savefilepath();
//                return;
//            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);

                String[] sourceStrArray = path.split("\\/");

                for (int i=0;i<sourceStrArray.length;i++)
                { System.out.println(sourceStrArray[i]);
                    if(i!=sourceStrArray.length-1)
                        savepath+=sourceStrArray[i]+"/";

                }
                stringTxt(content,savepath,title);
                truepath=savepath+title+".txt";
                //tv.setText(path);
                savefilepath();
                Intent intent1 = new Intent();
                intent1.setClass(AddtxtActivity.this,MainActivity.class);
                startActivity(intent1);

                Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                //tv.setText(path);
                String[] sourceStrArray = path.split("\\/");
                for (int i=0;i<sourceStrArray.length;i++)
                {
                    if(i==sourceStrArray.length-1)
                        savepath+=sourceStrArray[i]+"/";

                }
                stringTxt(content,savepath,title);
                truepath=savepath+title+".txt";
                savefilepath();

                Intent intent1 = new Intent();
                intent1.setClass(AddtxtActivity.this,MainActivity.class);
                startActivity(intent1);
                Toast.makeText(AddtxtActivity.this, path+"222222", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void savefilepath()
    {
        FileService fileService1=new FileService(AddtxtActivity.this);
        Filename filename1=new Filename(title+".txt",truepath,fomate());
        fileService1.save(filename1);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String fomate(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日  hh时:mm分:ss秒");
        return simpleDateFormat.format(new Date());
    }

}
