package com.fstack.RecoveryPhoto.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.fstack.RecoveryPhoto.Pojo.ImageData;
import com.fstack.RecoveryPhoto.Utils.Config;
import com.fstack.RecoveryPhoto.Utils.MediaScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecoverPhotosAsyncTask extends AsyncTask<String, String, String> {
    private final String TAG = getClass().getName();
    private ArrayList<ImageData> alImageData;
    private Context mContext;
    private Handler handler;
    private ProgressDialog progressDialog;

    public RecoverPhotosAsyncTask(Context context, ArrayList<ImageData> alImageData, Handler handler) {
            this.mContext = context;
            this.handler = handler;
            this.alImageData = alImageData;
            }

    protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(this.mContext);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setMessage("Restoring");
            this.progressDialog.show();
            }

    protected String doInBackground(String... strAr) {


            for (int strArr = 0; strArr < this.alImageData.size(); strArr++) {
            File sourceFile = new File((this.alImageData.get(strArr)).getFilePath());
            File fileDirectory = new File(Config.IMAGE_RECOVER_DIRECTORY);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Config.IMAGE_RECOVER_DIRECTORY);
            stringBuilder.append(File.separator);
            stringBuilder.append(getFileName(strArr));
            File destinationFile = new File(stringBuilder.toString());
            try {
            if (!destinationFile.exists()) {
                fileDirectory.mkdirs();
            }
            copy(sourceFile, destinationFile);
            if (VERSION.SDK_INT >= 19) {
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(destinationFile));
            this.mContext.sendBroadcast(intent);
            }
            new MediaScanner(this.mContext, destinationFile);
            } catch (IOException e) {
            e.printStackTrace();
            }
            }
            return null;
    }

        public void copy(File file, File file2) throws IOException {
            FileChannel source = null;
            FileChannel destination = null;

            source = new FileInputStream(file).getChannel();
            destination = new FileOutputStream(file2).getChannel();

            source.transferTo(0, source.size(), destination);
            if (source != null) {
                source.close();
            }
            if (destination!=null){
                destination.close();
            }
        }


        public String getFileName(int i) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(simpleDateFormat.format(date));
                stringBuilder.append(i);
                stringBuilder.append(".png");
                return String.valueOf(stringBuilder.toString());
                }

        protected void onPostExecute(String str) {
            Toast.makeText(mContext, "Restored successfully", Toast.LENGTH_SHORT).show();
                if (this.progressDialog != null) {
                this.progressDialog.cancel();
                this.progressDialog = null;
                }
                if (this.handler != null) {
                Message obtain = Message.obtain();
                obtain.what = Config.REPAIR;
                obtain.obj = str;
                this.handler.sendMessage(obtain);
                }
                super.onPostExecute(str);
        }
}
