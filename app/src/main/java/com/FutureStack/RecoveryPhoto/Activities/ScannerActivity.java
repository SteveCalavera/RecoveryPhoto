package com.FutureStack.RecoveryPhoto.Activities;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.FutureStack.RecoveryPhoto.Adapters.AdapterImage;
import com.FutureStack.RecoveryPhoto.AsyncTask.RecoverPhotosAsyncTask;
import com.FutureStack.RecoveryPhoto.AsyncTask.ScanImagesAsyncTask;
import com.FutureStack.RecoveryPhoto.Pojo.ImageData;
import com.FutureStack.RecoveryPhoto.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;

public class ScannerActivity extends AppCompatActivity {


    private ImageView ivLoading;
    private AdapterImage adapterImage;
    private MyDataHandler myDataHandler;
    private GridView gvAllPics;
    private ArrayList<ImageData> alImageData = new ArrayList();
    private TextView tvScannedPics;
    public static Toolbar toolbar;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_scanner);
        init();

    }
    public void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDataHandler = new MyDataHandler();
        gvAllPics = findViewById(R.id.gvGallery);
        adapterImage = new AdapterImage(this, this.alImageData);
        gvAllPics.setAdapter(this.adapterImage);
        tvScannedPics = findViewById(R.id.tvItems);
        ivLoading = findViewById(R.id.iv_start_scan_anim);
        ivLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scan_animation));
        new ScanImagesAsyncTask(this, this.myDataHandler).execute("all");
    }



    public void onBackPressed() {
        super.onBackPressed();
        final InterstitialAd mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getString(R.string.interstitial_id));
        mInterstitial.loadAd(new AdRequest.Builder().build());
        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub

                Random rand = new Random();

                int n = rand.nextInt(2);
                if(n==1) {
                    super.onAdLoaded();
                    if (mInterstitial.isLoaded()) {
                        mInterstitial.show();
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restore_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_restore) {

            repairingPictures(adapterImage.getSelectedItem());

//            ArrayList<ImageData> alSelectedItems = adapterImage.getSelectedItem();
//
//            for (int i=0;i<alSelectedItems.size();i++){
//                copy(alSelectedItems.get(i).getFilePath());
//            }
//
//            Toast.makeText(this, "Restored", Toast.LENGTH_SHORT).show();
//            toolbar.setVisibility(View.GONE);
//            adapterImage.setAllImagesUnseleted();
//            adapterImage.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void repairingPictures(ArrayList<ImageData> arrayList) {
//        new RecoverPhotosAsyncTask(this, arrayList, this.myDataHandler).execute(new String[0]);
        RecoverPhotosAsyncTask repairTask = new RecoverPhotosAsyncTask(this,arrayList,myDataHandler);
        repairTask.execute();
    }


    public class MyDataHandler extends Handler {

        class AnimationHandlerClass implements Animation.AnimationListener {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            AnimationHandlerClass() {
            }

            public void onAnimationEnd(Animation animation) {
                ivLoading.setVisibility(View.GONE);
            }
        }

//        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1000) {
                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(500);
                animation.setFillAfter(true);
                animation.setAnimationListener(new AnimationHandlerClass());
                alImageData.clear();
                alImageData.addAll((ArrayList) message.obj);
                adapterImage.notifyDataSetChanged();
                tvScannedPics.setVisibility(View.GONE);
                ivLoading.clearAnimation();
                ivLoading.startAnimation(animation);
            } else if (message.what == 2000) {
                adapterImage.setAllImagesUnseleted();
                adapterImage.notifyDataSetChanged();
            } else if (message.what == 3000) {
                TextView tvFoundItems = tvScannedPics;
                StringBuilder sbProgressMessage = new StringBuilder();
                sbProgressMessage.append(message.obj.toString());
                sbProgressMessage.append(" ");
                sbProgressMessage.append("files found");
                tvFoundItems.setText(sbProgressMessage.toString());
            }
        }
    }


    public void copy(String sourceFilePath){
        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/RestoredPhotos/"+System.currentTimeMillis()+".png");
        if (sourceFile.exists()) {
            try {
                copyFile(sourceFile,destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}






























































//    private void init() {
//        lv = findViewById(R.id.gvGallery);
//        alAllFiles = new ArrayList<>();
//        btnOk = findViewById(R.id.btnOk);
//        tvItems = findViewById(R.id.tvItems);
//
//        String strArr = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//        checkFileOfDirectory(getFileList(strArr));
//
//
////        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,alAllFiles);
//
////        AdapterImage adapterImage = new AdapterImage(ScannerActivity.this,alAllFiles);
////        Toast.makeText(ScannerActivity.this, ""+alAllFiles.size(), Toast.LENGTH_LONG).show();
////        lv.setAdapter(adapterImage);
//
//    }

//    private void listeners() {
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
//                String strData="";
//                for (int i = 0 ; i<alSelected.size() ; i++){
//                    strData+=alSelected.get(i)+"\n";
//                }
//                builder.setMessage(strData);
//                builder.create();
//                builder.show();
//            }
//        });
//
//    }

//    public File[] getFileList(String str) {
//        File file = new File(str);
//        if (!file.isDirectory()) {   // check with !
//            return null;
//        }
//        return file.listFiles();
//    }

//    public void checkFileOfDirectory(File[] fileArr) {
//        for (int i = 0; i < fileArr.length; i++) {
//            if (fileArr[i].isDirectory()) {
//                checkFileOfDirectory(getFileList(fileArr[i].getFilePath()));
//            } else {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//
//                fileName = fileArr[i].getFilePath();
//                if (fileName.endsWith(".exo") || fileName.endsWith(".mp3") || fileName.endsWith(".mp4") || fileName.endsWith(".pdf") || fileName.endsWith(".apk")
//                        || fileName.endsWith(".txt") || fileName.endsWith(".doc") || fileName.endsWith(".exi") || fileName.endsWith(".dat")
//                        || fileName.endsWith(".m4a") || fileName.endsWith(".json") || fileName.endsWith(".chck")) {
//                    //do nothing, just skip these files
//                    //
//                } else if (fileName.endsWith(".0")){
//                    alAllFiles.add(fileArr[i].getFilePath());
//                    items++;
//                    Toast.makeText(ScannerActivity.this, ""+items, Toast.LENGTH_SHORT).show();
//                    tvItems.setText(items+" items scanned");
//                }
//                BitmapFactory.decodeFile(fileArr[i].getFilePath(), options);
//            }
//        }
//    }
















