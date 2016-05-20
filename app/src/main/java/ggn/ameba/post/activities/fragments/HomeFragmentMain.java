package ggn.ameba.post.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.DateUtilsG;
import ggn.ameba.post.activities.SplashActivity;
import ggn.ameba.post.activities.ThemeInfoActivtiy;
import ggn.ameba.post.widget.CountDownView;
import ggn.ameba.post.widget.TimerListener;

public class HomeFragmentMain extends BaseFragmentG implements TimerListener
{

    private FrameLayout frameContainer;

    CountDownView countdownview;

    private LinearLayout themeLayout;

    private TextView tvFood, tvMarquee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main, container, false);

        countdownview = (CountDownView) view.findViewById(R.id.countdownview);

        frameContainer = (FrameLayout) view.findViewById(R.id.container);
        themeLayout = (LinearLayout) view.findViewById(R.id.themeLayout);
        tvFood = (TextView) view.findViewById(R.id.tvFood);
        tvMarquee = (TextView) view.findViewById(R.id.tvMarquee);

        getChildFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


        tvFood.setText(getLocaldata().getThemeName());
        themeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), ThemeInfoActivtiy.class));
            }
        });


        tvMarquee.setText(getLocaldata().getMarqueeText());
        tvMarquee.setSelected(true);


        return view;
    }

    @Override
    public void onResume()
    {
        countdownview.setInitialTime(DateUtilsG.timeLeft(getLocaldata().getThemeEndDate()));
        countdownview.start();
        countdownview.setListener(this);
        super.onResume();
    }

    @Override
    public void timerElapsed()
    {
        countdownview.reset();

        startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finish();


    }
}
