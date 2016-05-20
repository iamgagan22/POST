package ggn.ameba.post.activities;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.DateUtilsG;

public class ThemeInfoActivtiy extends BaseActivityG
{

    private TextView tvThemeName, tvEndDate, tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_info_activtiy);


        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvThemeName = (TextView) findViewById(R.id.tvThemeName);
        tvOverview = (TextView) findViewById(R.id.tvOverview);


        showDate();
    }

    SimpleDateFormat sdf = new SimpleDateFormat(DateUtilsG.G_FORMAT, Locale.US);

    private void showDate()
    {
        try
        {
            String date = getLocaldata().getThemeEndDate();
            if (date.contains("."))
            {
                date = date.substring(0, date.indexOf("."));
            }

            String created_at = sdf.format(DateUtilsG.dateServer(date));


            tvThemeName.setText("Theme Name : " + getLocaldata().getThemeName());
            tvEndDate.setText("End Date : " + created_at);

            tvOverview.setText("Overview : " + getLocaldata().getThemeOverview());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
