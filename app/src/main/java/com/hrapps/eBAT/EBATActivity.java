package com.hrapps.eBAT;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.wallet.LineItem;
import com.hrapps.R;

import java.util.ArrayList;

import Model.ConfigModel;
import Model.RolesModel;
import Utility.CWIncturePreferences;

/**
 * Created by Harshitha.bshekharap on 11/16/2017.
 */

public class EBATActivity extends FragmentActivity {

    private ViewPager vpContainer;
    private ViewPagerTabAdapter vpTabAdapter;
    private TabLayout tab_layout;

    private ImageView back;
    ArrayList<RolesModel> rolesList = new ArrayList<>();

    boolean teamAssessment = false;
    boolean selfAssessment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_primary_dark));
        }

        rolesList = (ArrayList<RolesModel>) getIntent().getSerializableExtra("roles");

        if (CWIncturePreferences.isMatrixManager() == true) {
            teamAssessment = true;
            for (int r = 0; r < rolesList.size(); r++) {
                if (rolesList.get(r).getRole_name().contains("employee")) {
                    selfAssessment = true;
                    break;
                }
            }
        }//end of matrixManager = true
        else {
            for (int r = 0; r < rolesList.size(); r++) {
                if (rolesList.get(r).getRole_name().contains("manager")) {
                    teamAssessment = true;
                } else if (rolesList.get(r).getRole_name().contains("employee")) {
                    selfAssessment = true;
                }
            }
        }// end of matrixManager = false

        vpContainer = (ViewPager) findViewById(R.id.vpContainer);

        if (selfAssessment == true && teamAssessment == true) {
            vpTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(), new String[]{"Team Assessment", "Self Assessment"});

        } else if (selfAssessment && !teamAssessment) {
            vpTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(), new String[]{"Self Assessment"});

        } else if (teamAssessment && !selfAssessment) {
            vpTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(), new String[]{"Team Assessment"});
        }

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        vpContainer.setAdapter(vpTabAdapter);

        setupTabView();

        vpContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                TabLayout.Tab tab = tab_layout.getTabAt(position);
                tab.select();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * Set up and display tabs based on configuration of tabs saved in DB
     */

    private void setupTabView() {
        if (selfAssessment == true && teamAssessment == true) {
            tab_layout.addTab(tab_layout.newTab().setText("Team Assessment"));
            tab_layout.addTab(tab_layout.newTab().setText("Self Assessment"));
        } else if (selfAssessment && !teamAssessment) {
            tab_layout.addTab(tab_layout.newTab().setText("Self Assessment"));
        } else if (teamAssessment && !selfAssessment) {
            tab_layout.addTab(tab_layout.newTab().setText("Team Assessment"));
        }
        tab_layout.setTabGravity(tab_layout.GRAVITY_FILL);

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpContainer.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}
