package com.hrapps.eBAT;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import Fragments.SelfAssessmentFragment;
import Fragments.TeamAssessmentFragment;

/**
 * Created by Harshitha.bshekharap on 11/16/2017.
 */

public class ViewPagerTabAdapter extends FragmentPagerAdapter {
    String[] tabs;

    public ViewPagerTabAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        this.tabs = tabs;
    }


    @Override
    public Fragment getItem(int index) {

        if (tabs[index].equals("Team Assessment"))
            // Top Rated fragment activity
            return new TeamAssessmentFragment();
        else if (tabs[index].equals("Self Assessment"))
            // Games fragment activity
            return new SelfAssessmentFragment();


        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return tabs.length;
    }

}

