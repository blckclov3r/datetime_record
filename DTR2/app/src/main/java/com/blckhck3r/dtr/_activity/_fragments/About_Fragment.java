package com.blckhck3r.dtr._activity._fragments;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blckhck3r.dtr.R;
import com.blckhck3r.dtr._activity._activity.main_activity.SplashScreen;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

import in.codeshuffle.typewriterview.TypeWriterView;

public class About_Fragment extends Fragment {
    LinearLayout qr_code;
    LinearLayout administrator;
    LinearLayout links;
    TypeWriterView typeWriterView;
    LinearLayout aboutArea;
    public About_Fragment() {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.about_fragment,container,false);
        qr_code = (LinearLayout) view.findViewById(R.id.qr_code);
        typeWriterView=(TypeWriterView)view.findViewById(R.id.typeWriterView);
        administrator = (LinearLayout) view.findViewById(R.id.administrator);
        links = (LinearLayout) view.findViewById(R.id.links);
        aboutArea = (LinearLayout) view.findViewById(R.id.aboutArea);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                typeWriterView.animateText("About");
                typeWriterView.setDelay(50);
            }
        });
        typeWriterView.setWithMusic(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Sequent.origin(aboutArea).anim(getActivity(), Animation.FADE_IN_DOWN).duration(444).
                        delay(100).start();
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Sequent.origin(qr_code).anim(getActivity(), Animation.FADE_IN_LEFT).duration(1111).
                        delay(777).start();
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Sequent.origin(administrator).anim(getActivity(), Animation.FADE_IN_UP).duration(1000).
                        delay(50).start();
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Sequent.origin(links).anim(getActivity(), Animation.BOUNCE_IN).duration(2000).
                        delay(3050).start();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
