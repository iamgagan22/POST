package com.ariseden.post.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariseden.post.UtillsG.GlobalConstantsG;
import com.ariseden.post.WebService.SuperAsyncG;
import com.ariseden.post.adapter.HomeModel;
import com.ariseden.post.adapter.IdCardModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import com.ariseden.post.R;
import com.ariseden.post.UtillsG.CallBackG;
import com.ariseden.post.UtillsG.UtillG;
import com.ariseden.post.adapter.HomeAdapter;
import com.ariseden.post.widget.RoundedCornersGaganImg;

public class ViewPostActivity extends BaseActivityG
{
    private RecyclerView           recyclerview;
    private RoundedCornersGaganImg imgProfile;
    private TextView               tvName, tvEmail, tvStatusLine;

    private View viewIdCard;

    private IdCardModel     idCardModel;
    private List<HomeModel> listHome;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        listHome = new ArrayList<>();
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        idCardModel = (IdCardModel) getIntent().getSerializableExtra("data");

        showIDcard();


        mLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerview.setLayoutManager(mLayoutManager);


        homeAdapter = new HomeAdapter(ViewPostActivity.this, listHome);
        recyclerview.setAdapter(homeAdapter);


        showData();
    }


    private void showIDcard()
    {
        viewIdCard = findViewById(R.id.viewIdCard);

        imgProfile = (RoundedCornersGaganImg) viewIdCard.findViewById(R.id.imgProfile);
        imgProfile.setImageUrlRound(ViewPostActivity.this, idCardModel.getProfilePic());

        tvName = (TextView) viewIdCard.findViewById(R.id.tvName);
        tvName.setText(idCardModel.getName());


        tvStatusLine = (TextView) viewIdCard.findViewById(R.id.tvStatusLine);
        tvStatusLine.setVisibility(View.VISIBLE);

//        String tagLIne = idCardModel.getTagLine().equals("null") ? "status" : idCardModel.getTagLine();


        if (idCardModel.getTagLine().equals("null") || idCardModel.getTagLine().equals(""))
        {
            new SuperAsyncG(GlobalConstantsG.URL + "Customer/GetCustomerStatus?CustomerId=" + idCardModel.getUserId(), new HashMap<String, String>(), new CallBackG<String>()
            {
                @Override
                public void callBack(String response)
                {
//                    "Status": "success",
//                        "Message": "hello123"
                    try
                    {
                        JSONObject jboj = new JSONObject(response);

                        if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                        {
                            if (jboj.getString(GlobalConstantsG.Message).equals("null") || jboj.getString(GlobalConstantsG.Message).equals(""))
                            {
                                tvStatusLine.setText("Status");

                            }
                            else
                            {
                                tvStatusLine.setText(jboj.getString(GlobalConstantsG.Message));
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).execute();
        }
        else
        {
            tvStatusLine.setText(idCardModel.getTagLine());
        }


        TextView tv = (TextView) viewIdCard.findViewById(R.id.tvPostId);
        tv.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Gnawhard.otf"));

        tvEmail = (TextView) viewIdCard.findViewById(R.id.tvEmail);
//        tvEmail.setText(idCardModel.getEmail());

        ((ImageView) viewIdCard.findViewById(R.id.img_eye)).setVisibility(View.INVISIBLE);
        ((ImageView) viewIdCard.findViewById(R.id.imgedit)).setVisibility(View.INVISIBLE);
    }


    private HomeAdapter                homeAdapter;
    private StaggeredGridLayoutManager mLayoutManager;


    private void showData()
    {

        showProgress();

        new SuperAsyncG(GlobalConstantsG.URL + "theme/GetMyThemePost?CustomerId=" + idCardModel.getUserId(), new HashMap<String, String>(), new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {
                    cancelProgress();
                    JSONObject jboj = new JSONObject(response);

                    if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                    {
                        JSONArray jsonArray = new JSONArray(jboj.getString(GlobalConstantsG.Message));


                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            HomeModel homemodel = new HomeModel();

                            JSONObject jInner = jsonArray.getJSONObject(i);
                            homemodel.setImagePath(jInner.getString("ImagePath"));
                            homemodel.setCreatedDate(jInner.getString("CreatedDate"));
                            homemodel.setCustomerId(jInner.getString("CustomerId"));
                            homemodel.setThemePostId(jInner.getString("ThemePostId"));
                            homemodel.setThemeId(jInner.getString("ThemeID"));

                            listHome.add(homemodel);
                        }

                        Collections.reverse(listHome);

                        homeAdapter.notifyDataSetChanged();

                    }
                    else
                    {
                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), ViewPostActivity.this, true);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


    public void baCk(View view)
    {
        finish();
    }
}
