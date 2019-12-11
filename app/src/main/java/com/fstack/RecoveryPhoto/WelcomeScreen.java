package com.fstack.RecoveryPhoto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fstack.RecoveryPhoto.Activities.SplashScreen;

public class WelcomeScreen extends AppCompatActivity {

    private RelativeLayout rlOuter;
    private CheckBox cbAgree;
    private TextView tvPrivacyPolicy;
    private Button btnAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_screen);



        init();

        boolean isAgree = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Agreed", false);
        if (isAgree){
            startActivity(new Intent(WelcomeScreen.this, SplashScreen.class));
            WelcomeScreen.this.finish();
        } else {
            rlOuter.setVisibility(View.VISIBLE);
        }


//        String strPrivacyPolicy = "I have read and agree to the <a href='http://kdevstudio.blogspot.com/2018/04/privacy-policy.html'</a>Privacy Policy";
//        tvPrivacyPolicy.setText(Html.fromHtml(strPrivacyPolicy));
//        tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());


        String txtTermsCondition = getResources().getString(R.string.i_have_read_and_agree)+  " <a href='"+getResources().getString(R.string.url_privacy)+"'</a>"+getResources().getString(R.string.privacy_policy);
        tvPrivacyPolicy.setLinkTextColor(getResources().getColor(R.color.colorContent));



        Spannable s = (Spannable) Html.fromHtml(txtTermsCondition);
        for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }
        tvPrivacyPolicy.setText(s);

//        tvTerms.setText(Html.fromHtml(txtTermsCondition));

        tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        listeners();
    }

    private void init() {
        rlOuter = findViewById(R.id.rlOuter);
        cbAgree = findViewById(R.id.cbAgree);
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        btnAgree = findViewById(R.id.btnAgree);
    }

    private void listeners() {
        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnAgree.setVisibility(View.VISIBLE);
                } else {
                    btnAgree.setVisibility(View.GONE);
                }
            }
        });

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("Agreed", true);
                editor.apply();
                editor.commit();
                startActivity(new Intent(WelcomeScreen.this, SplashScreen.class));
                WelcomeScreen.this.finish();
            }
        });
    }
}
