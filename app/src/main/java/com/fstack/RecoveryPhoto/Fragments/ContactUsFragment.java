package com.fstack.RecoveryPhoto.Fragments;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.fstack.RecoveryPhoto.R;


/**
 * Created by mnaum on 8/20/2017.
 */

public class ContactUsFragment extends Fragment {
    private TextView email;
    private LinearLayout llContactNum, llWeb, llFiverr, llFacebook, llYoutube;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private AdRequest adRequest;


    public static String FACEBOOK_URL = "https://www.facebook.com/StudereGateway";
    public static String FACEBOOK_PAGE_ID = "792626421083384";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contact,null);

       // String mailText = "<a href='mailto:mail@studeregateway.com'</a>mail@studeregateway.com";
        //email.setText(Html.fromHtml(mailText));
        //email.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }


    public void displayInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
    }

    public void loadAd(View view){


        mAdView= view.findViewById(R.id.adView);


        MobileAds.initialize(getActivity(),  getResources().getString(R.string.banner_id));      //test ad
//        MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544/6300978111");
        adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


                mInterstitialAd = new InterstitialAd(getActivity());
                mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_id));      //test ad
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                mInterstitialAd.loadAd(adRequest);
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdLoaded() {
                        displayInterstitial();
                    }
                });




    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();


        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app

                ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo("com.facebook.katana",0);
                boolean appStatus = ai.enabled;
                if (appStatus) {
                    return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else {
                    return FACEBOOK_URL; //normal web url
                }
            } else { //older versions of fb app
                ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo("com.facebook.katana",0);
                boolean appStatus = ai.enabled;
                if (appStatus) {
                    return "fb://page/" + FACEBOOK_PAGE_ID;
                } else {
                    return FACEBOOK_URL; //normal web url
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }


    private void listeners() {

        /*
        llContactNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+923001234567"));
                startActivity(intent);
            }
        });


        llWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.studeregateway.com"));
                startActivity(browserIntent);
            }
        });
        llFiverr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fiverr.com/h_mateen"));
                startActivity(browserIntent);
            }
        });

        llFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getActivity());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        llYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/channel/UCAyRERoIt5A4WEzjBwQfY5A"));
                startActivity(intent);
            }
        });
        */

    }

    private void init(View view) {

        //llContactNum = view.findViewById(R.id.llContactNum);
       // email = view.findViewById(R.id.email);
       // llWeb = view.findViewById(R.id.llWeb);
       // llFiverr = view.findViewById(R.id.llFiverr);
       // llFacebook = view.findViewById(R.id.llFacebook);
       // llYoutube = view.findViewById(R.id.llYoutube);
    }
}
