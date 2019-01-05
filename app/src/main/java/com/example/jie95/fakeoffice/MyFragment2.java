package com.example.jie95.fakeoffice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jie95.fakeoffice.FileSelected.FileService;
import com.example.jie95.fakeoffice.FileSelected.Filename;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyFragment2 extends Fragment {

    private SwipeRefreshLayout swiperereshlayout ;
    private int[]icons={R.drawable.txt,R.drawable.word,R.drawable.ppt,R.drawable.pdf,R.drawable.excel};
    public List<Map<String, ?>> filename;
    public ListView fileList;
    FileService fileService;
    AlertDialog deleteDiaryAlert;
    SimpleAdapter simpleAdapter;
    TextView temp;
    int id;
    private BoomMenuButton boomMenuButton;
    private int[] res={R.drawable.ic_add,R.drawable.ic_delete,R.drawable.ic_newtxt};
    private String[] text={"添加文件","删除指定文件","新建文件(TXT)"};

    public MyFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content_documents, container, false);
        initView(view);
        return view;
    }

    public void initView(View view)
    {
        swiperereshlayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperereshlayout);
        fileList = (ListView) view.findViewById(R.id.lv_open);
        swiperereshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //设置2秒的时间来执行以下事件
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        filename = getfile();
                        simpleAdapter = new SimpleAdapter(getContext(), filename, R.layout.list_item,
                                new String[] { "img","title" }, new int[] {R.id.img, R.id.id});

                        fileList.setAdapter(simpleAdapter);
                        //获取删除时需要的id

                        fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                           int position, long arg3) {
                                id = (Integer) filename.get(position).get("id");
                                final String items[] = {"还原", "彻底删除"};
                                AlertDialog dialog = new AlertDialog.Builder(getContext())
                                        .setIcon(R.mipmap.illust_67298328_20180222_002927)//设置标题的图片
                                        .setTitle("请选择操作")//设置对话框的标题
                                        .setMessage("还原或者彻底删除？")
                                        .setNegativeButton("还原", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Filename target=new Filename();
                                                List<Filename> list = fileService.getAllFile();
                                                for (int i = 0; i < list.size(); i++) {
                                                    Filename d = list.get(i);
                                                    if(d.getId()==id)
                                                    {
                                                        target=d;
                                                    }
                                                }
                                                fileService.save(target);
                                                fileService.delete_recycle(id);
                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton("彻底删除", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                    deleteDiaryDialog();
                                                    deleteDiaryAlert.show();
                                                dialog.dismiss();
                                            }
                                        }).create();
                                dialog.show();
                                return false;
                            }
                        });
                        swiperereshlayout.setRefreshing(false);
                    }
                }, 10);
            }
        });

    }

    String path;
    String title;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                path = getPath(getContext(), uri);
                String[] sourceStrArray = path.split("\\/");
                for (int i=0;i<sourceStrArray.length;i++)
                {
                    System.out.println(sourceStrArray[i]);
                    if(i==sourceStrArray.length-1)
                        title=sourceStrArray[i];
                }
                //tv.setText(path);
                savefilepath();
                //Toast.makeText(getActivity(),path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                //tv.setText(path);
                String[] sourceStrArray = path.split("\\/");
                for (int i=0;i<sourceStrArray.length;i++)
                {
                    if(i==sourceStrArray.length-1)
                        title=sourceStrArray[i];
                }
                savefilepath();
                //Toast.makeText(getActivity(), path+"222222", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
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

    @Override
    public void onResume() {
        super.onResume();
        fileList = (ListView) getView().findViewById(R.id.lv_open);

        filename = getfile();
        simpleAdapter = new SimpleAdapter(getContext(), filename, R.layout.list_item,
                new String[] { "title" }, new int[] { R.id.id});

        fileList.setAdapter(simpleAdapter);
        //获取删除时需要的id

        fileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                id = (Integer) filename.get(position).get("id");
                final String items[] = {"还原", "彻底删除"};
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setIcon(R.mipmap.illust_67298328_20180222_002927)//设置标题的图片
                        .setTitle("请选择操作")//设置对话框的标题
                        .setMessage("还原或者彻底删除？")
                        .setNegativeButton("还原", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Filename target=new Filename();
                                List<Filename> list = fileService.getAllFile();
                                for (int i = 0; i < list.size(); i++) {
                                    Filename d = list.get(i);
                                    if(d.getId()==id)
                                    {
                                        target=d;
                                    }
                                }
                                fileService.save(target);
                                fileService.delete_recycle(id);
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("彻底删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteDiaryDialog();
                                deleteDiaryAlert.show();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                return false;
            }
        });
    }

    public void onRestart() {

    }

    private List<Map<String, ?>> getfile() {
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
        Map<String, Object> item = null;
        fileService= new FileService(getActivity());
        List<Filename> list = fileService.getAllFile();
        for (int i = 0; i < list.size(); i++) {
            Filename d = list.get(i);

            String[] sourceStrArray = d.getTitle().split("\\.");
            item = new HashMap<String, Object>();
            //添加图片
            for (int j=0;j<sourceStrArray.length;j++)
            {
                if (j==sourceStrArray.length-1) {
                    //System.out.println("test:::::::::::"+sourceStrArray[j]);
                    if (sourceStrArray[j].equals("txt") )
                        item.put("img",icons[0]);
                    else if (sourceStrArray[j].equals("doc")||sourceStrArray[j].equals("docx"))
                        item.put("img",icons[1]);
                    else if(sourceStrArray[j].equals("ppt")||sourceStrArray[j].equals("pptx"))
                    {
                        item.put("img",icons[2]);
                    }
                    else if(sourceStrArray[j].equals("pdf"))
                    {item.put("img",icons[3]);}
                    else if(sourceStrArray[j].equals("xls")||sourceStrArray[j].equals("xlsx"))
                    {
                        item.put("img",icons[4]);
                    }
                }
            }

            item.put("id", d.getId());// ID
            item.put("title",d.getTitle());//title
            item.put("path", d.getPath());// 路径
            item.put("pubdate", d.getPubdate());// 日期
            data.add(item);
        }
        return data;
    }

    public void savefilepath()
    {
        FileService fileService1=new FileService(getActivity());
        Filename filename1=new Filename(title,path,fomate());
        fileService1.save(filename1);
    }
    private void deleteDiaryDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Are you sure to delete?");
        alertDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // System.out.print("id:"+id);
                        String target=null;
                        List<Filename> list = fileService.getAllFile();
                        for (int i = 0; i < list.size(); i++) {
                            Filename d = list.get(i);
                            if(d.getId()==id)
                            {
                                target=d.getPath();
                                break;
                            }
                        }
                        fileService.delete_recycle(id);
                        File file=new File(target);
                        if(file.exists()&&file.isFile())
                        {
                            if(file.delete())
                            {
                                Toast.makeText(getContext(), "删除单个文件" + target + "成功！", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "删除单个文件" + target + "失败......", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "删除单个文件" + target + "不存在！", Toast.LENGTH_SHORT).show();
                        }
                        //fileService.savetorecyle(target);
                        //onRestart();
                    }
                });
        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
        deleteDiaryAlert = alertDialog.create();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                break;
            case R.id.delete:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public String fomate(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日  hh时:mm分:ss秒");
        return simpleDateFormat.format(new Date());
    }
}