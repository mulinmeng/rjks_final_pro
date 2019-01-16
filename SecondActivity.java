package com.example.zhangwenqiang.rjks_final_pro;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.roger.catloadinglibrary.CatLoadingView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.example.zhangwenqiang.rjks_final_pro.BaseActivity.currentIp;

public class SecondActivity extends FragmentActivity {
    private final int HANDLER_MSG_TELL_RECV = 0x124;
    CatLoadingView mView;

    //Post URLS
    public final static String EXTRA_MESSAGE = "com.coderefer.uploadfiletoserver";
    private String SERVER_URL = "http://"+ currentIp +":3000/uusapp/addimage/";

    // Request codes

    private String token="Token 1cc2410e045e3bcd48f0ebef7a3aff1b8f184d49";
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static int PICTURE_CAPTURE_PERMISSION = 222;
    private static final String TAG = MainActivity.class.getSimpleName();


    // Storage related
    private Uri mCurrentImageUri;
    public static final String FILE_NAME = "temp.jpg";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;


    // GUI items
    Button bUpload;
    TextView tUploadStatus;
    TextView tvFileName;
    Button bCaptureImg;
    Button bGoToGallery;
    Button shiBie;



    Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            };

    Button.OnClickListener mShangchuanOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.show(getSupportFragmentManager(), "");
                    //Intent intent = new Intent(SecondActivity.this,ListView.class);
                    //intent.putExtra("extra_data","3d,3s,4d");
                    //startActivity(intent);
                    startNetThread("47.100.200.169",8889,"test");
                }
            };

    Button.OnClickListener mUploadPicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    test();
                }
            };

    Button.OnClickListener mGoToGallery =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://47.100.200.169:3000/simplegui/"));
                    startActivity(intent);
                }
            };

    protected void onCreate(Bundle savedInstanceState) {
        // Set Up
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_image);
        bUpload = (Button) findViewById(R.id.upload_the_image);
        shiBie = (Button)findViewById(R.id.shibie);
        bCaptureImg = (Button) findViewById(R.id.do_image_capture);
        tvFileName = (TextView) findViewById(R.id.current_img_file_name);
        tUploadStatus = (TextView) findViewById(R.id.upload_status);
        bGoToGallery =(Button) findViewById(R.id.go_to_gallery);
        shiBie.setOnClickListener(mShangchuanOnClickListener);
        bCaptureImg.setOnClickListener(mTakePicOnClickListener);
        bUpload.setOnClickListener(mUploadPicOnClickListener);
        bGoToGallery.setOnClickListener(mGoToGallery);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        mView = new CatLoadingView();
        mView.setCanceledOnTouchOutside(false);

        Intent intent = getIntent();
        String temp = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        if(temp!=null){
            this.token = temp;
        }
        // Start procedures
        askPermissions();
    }


    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (actionCode) {
            case ACTION_TAKE_PHOTO_B:
                mCurrentImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentImageUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            default:
                break;
        }
        startActivityForResult(intent, actionCode);
    }

    public static File getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File rootDataDir = context.getFilesDir();
            File copyFile = new File( rootDataDir + File.separator + fileName);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(contentUri);
                OutputStream outputStream = new FileOutputStream(copyFile);
                try {
                    IoUtils.copyStream(inputStream, outputStream);
                }catch (java.lang.Exception e){
                    e.printStackTrace();
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return copyFile;
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }
    private void test() {
        File selectedFilePath = getFilePathFromURI(this,mCurrentImageUri);
        lubanImageList(selectedFilePath);
    }

    //图片上传部分
    private void uploadImg(String selectedFilePath) {
//        final String selectedFilePath = getFilePathFromURI(this,mCurrentImageUri);

        tvFileName.setText("Upload started");



        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new UploadImageTask().execute(selectedFilePath);
        } else {
            tUploadStatus.setText("No network connection available.");
        }

    }

    private class UploadImageTask extends AsyncTask<String, Void, String> {
        private final String U_TAG = UploadImageTask.class.getSimpleName();


        @Override
        protected String doInBackground(String... paths) {
            try {
                int resp = uploadFile(paths[0]);
                return "Server response " + resp;
            } catch (Exception e) {
                return "Unable to upload image";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            tUploadStatus.setText( result);
            tUploadStatus.setText("");
        }

        public int uploadFile(final String selectedFilePath) {


            int serverResponseCode = 0;

            HttpURLConnection connection;
            DataOutputStream dataOutputStream;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "921b1508a0b342f5bb06dfa40ae1f55d";


            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File selectedFile = new File(selectedFilePath);
            String[] parts = selectedFilePath.split("/");
            final String fileName = parts[parts.length - 1];

            if (!selectedFile.isFile()) {

                Log.e(U_TAG,"Source File Doesn't Exist: " + selectedFilePath);
                return 0;
            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    URL url = new URL(SERVER_URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);//Allow Inputs
                    connection.setDoOutput(true);//Allow Outputs
                    connection.setUseCaches(false);//Don't use a cached Copy
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("uploaded_file", selectedFilePath);
                    connection.setRequestProperty("Authorization",token);
                    //creating new dataoutputstream
                    dataOutputStream = new DataOutputStream(connection.getOutputStream());
                    String dispName= "image";
                    //String dispName= "uploaded_file";
                    //writing bytes to data outputstream
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\""+ dispName +"\";filename=\""
                            + fileName + "\"" + lineEnd);
                    dataOutputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);

                    dataOutputStream.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();
                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];

                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0) {

                        try {

                            //write the bytes read from inputstream
                            dataOutputStream.write(buffer, 0, bufferSize);
                        } catch (OutOfMemoryError e) {
                            Toast.makeText(SecondActivity.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                        }
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    try {
                        serverResponseCode = connection.getResponseCode();
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(SecondActivity.this, "Memory Insufficient!", Toast.LENGTH_SHORT).show();
                    }
                    String serverResponseMessage = connection.getResponseMessage();

                    Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    //response code of 200 indicates the server status OK
                    if (serverResponseCode == 200) {
                        Log.e(U_TAG,"File Upload completed.\n\n " + fileName);
                    }

                    //closing the input and output streams
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e(U_TAG,"File Not Found");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e(U_TAG,"URL Error!");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(U_TAG,"Cannot Read/Write File");
                }
                //dialog.dismiss();
                return serverResponseCode;
            }

        }
    }



    private void lubanImageList(File oldFile) {
        Luban.with(this) // 初始化
                .load(oldFile) // 要压缩的图片
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File newFile) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        // 获取返回的图片地址 newfile
                        String newPath=newFile.getAbsolutePath();
                        uploadImg(newPath);
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch(); // 启动压缩
    }



    private void askPermissions() {
        ArrayList<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.INTERNET);
        }

        if (permissions.size() > 0) {
            String[] permiss = permissions.toArray(new String[0]);

            ActivityCompat.requestPermissions(SecondActivity.this, permiss,
                    PICTURE_CAPTURE_PERMISSION);
        } else {
        }
    }


    public class ReturnDigest extends Thread {

        /** 消息摘要 */
        private String digest;

        @Override
        public void run() {
            try {
//                String contentAsString = "not initialized";
                //创建客户端对象
                Socket socket = new Socket("47.100.200.169",8889);
                //获取客户端对象的输出流
                OutputStream outputStream = socket.getOutputStream();
                //把内容以字节流的形式写入(data).getBytes();
//                    outputStream.write(data.getBytes());
                //刷新流管道
                outputStream.flush();
                System.out.println("打印客户端中的内容：" + socket);
                //拿到客户端输入流
                InputStream is = socket.getInputStream();

                byte[] bytes = new byte[1024];
                //回应数据
                int n = is.read(bytes);
//                contentAsString = new String(bytes, 0, n);
                digest = new String(bytes, 0, n);
//                System.out.println(contentAsString);
                //关闭流
                is.close();
                //关闭客户端
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取消息摘要
         * @return 消息摘要字节数组
         */
        public String getDigest() {
            return this.digest;
        }

    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            String data_d = msg.obj.toString();
            mView.isHidden();
            Intent intent = new Intent(SecondActivity.this,ListView.class);
            intent.putExtra("extra_data",data_d);
            startActivity(intent);
        }
    };



    private void startNetThread(final String host, final int port, final String data) {

        new Thread() {
            public void run() {
                try {
                    String contentAsString = "not initialized";
                    //创建客户端对象
                    Socket socket = new Socket(host,port);
                    //获取客户端对象的输出流
                    OutputStream outputStream = socket.getOutputStream();
                    //把内容以字节流的形式写入(data).getBytes();
//                    outputStream.write(data.getBytes());
                    //刷新流管道
                    outputStream.flush();
                    System.out.println("打印客户端中的内容：" + socket);
                    //拿到客户端输入流
                    InputStream is = socket.getInputStream();

                    byte[] bytes = new byte[1024];
                    //回应数据
                    int n = is.read(bytes);
                    Message msg = mHandler.obtainMessage(HANDLER_MSG_TELL_RECV, new String(bytes, 0, n));
                    msg.sendToTarget();
                    //关闭流
                    is.close();
                    //关闭客户端
                    socket.close();
                    //connect
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //启动线程
        }.start();
    }

}

