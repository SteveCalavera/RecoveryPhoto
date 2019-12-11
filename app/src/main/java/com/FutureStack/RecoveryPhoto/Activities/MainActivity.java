package com.FutureStack.RecoveryPhoto.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.FutureStack.RecoveryPhoto.R;
import com.FutureStack.RecoveryPhoto.Utils.AppConsts;
import com.FutureStack.RecoveryPhoto.Utils.Config;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
   // private RelativeLayout rlParent;
    private CardView cvScan, cvRestored;
    private FloatingActionButton share, more, rate;
   //private Button cvScan, cvRestored;
    private AdView mAdView;
    private View header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadAd();
        listeners();
        checkRunTimePermission();

    }


    public void loadAd(){


        mAdView= findViewById(R.id.adView);
        //mAdViewheader= header.findViewById(R.id.adView);
        MobileAds.initialize(MainActivity.this, getResources().getString(R.string.banner_id));      //test ad
//        MobileAds.initialize(MainActivity.this, "ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        //mAdViewheader.loadAd(adRequest);
        final InterstitialAd mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getString(R.string.interstitial_id));
        mInterstitial.loadAd(new AdRequest.Builder().build());
        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub

                    super.onAdLoaded();
                    if (mInterstitial.isLoaded()) {
                        mInterstitial.show();
                    }

            }
        });
    }


    private void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header=navigationView.getHeaderView(0);


        //rlParent = findViewById(R.id.rlParent);
        cvScan = findViewById(R.id.cvScan);
        cvRestored = findViewById(R.id.cvRestored);

        share = findViewById(R.id.fl_share);
        rate = findViewById(R.id.fl_rate);
        more = findViewById(R.id.fl_more);



    }
    private void listeners(){


        cvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanForPhotos();
            }
        });
        cvRestored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File fileDirectory = new File(Config.IMAGE_RECOVER_DIRECTORY);
                if (!fileDirectory.exists()){
                    fileDirectory.mkdirs();
                }
                scanForRestoredPhotos();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Recover Deleted Photos";
                String shareSub = "Let me recommend you this application\n\nRecover Deleted Photos\n";
                shareSub += "https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName();
                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.more_app_link)));
                startActivity(intent);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        });






    }




    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_scan) {

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                if (checkSelfPermission(AppConsts.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(AppConsts.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(AppConsts.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED

                ) {
                    requestPermissions(AppConsts.PERMISSIONS, AppConsts.requestForPermission);
                } else {
                    scanForPhotos();
                }
            }

        }

        else if (id == R.id.nav_restored) {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(checkSelfPermission(AppConsts.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(AppConsts.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(AppConsts.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED){

                    requestPermissions(AppConsts.PERMISSIONS,AppConsts.requestForPermission);
                } else {

                    File fileDirectory = new File(Config.IMAGE_RECOVER_DIRECTORY);
                    if (!fileDirectory.exists()){
                        fileDirectory.mkdirs();
                    }
                    scanForRestoredPhotos();
                }
            }

        } else if (id == R.id.nav_contact_us) {
            /*
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainFrame, new ContactUsFragment());
            transaction.addToBackStack(null).commit();
            */
            Intent intent = new Intent(this, AboutActivity.class);
            this.startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "Recovery Deleted Photos";
            String shareSub = "Let me recommend you this application\n\nRecover Deleted Photos\n";
            shareSub += "https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName();
            myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
            startActivity(Intent.createChooser(myIntent, "Share using"));

        } else if (id == R.id.nav_rate) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getApplicationContext().getPackageName()));
            startActivity(intent);

        }else if(id == R.id.home_page){
            Intent intent = new Intent(this, MainActivity.class);
            MainActivity.this.startActivity(intent);
        }
        else if (id == R.id.nav_more_apps) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.more_app_link)));
            startActivity(intent);

        } else if (id == R.id.nav_privacy_policy) {


            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_privacy)));
            startActivity(browserIntent);

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void scanForPhotos(){

        startActivity(new Intent(MainActivity.this,ScannerActivity.class));

    }
    private void scanForRestoredPhotos(){

        startActivity(new Intent(MainActivity.this,RestoredScannerActivity.class));

    }

    private void checkRunTimePermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(AppConsts.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(AppConsts.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(AppConsts.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED

            ){

                requestPermissions(AppConsts.PERMISSIONS,AppConsts.requestForPermission);
            }else{
                // allowed
                //rlParent.setVisibility(View.VISIBLE);
            }
        } else {
            // less than Marshmallow
            //rlParent.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConsts.requestForPermission: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                        if(checkSelfPermission(AppConsts.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                                || checkSelfPermission(AppConsts.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
                                || checkSelfPermission(AppConsts.PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED
                        ){
                            Toast.makeText(this, "Permission must be required for this app", Toast.LENGTH_SHORT).show();
                            requestPermissions(AppConsts.PERMISSIONS, AppConsts.requestForPermission);
                        }else{
                            //allowed
                          //  rlParent.setVisibility(View.VISIBLE);

                        }
                    }
                } else {
                }

                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
