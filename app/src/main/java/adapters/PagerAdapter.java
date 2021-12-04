package adapters;

/**
 * Created by JANANI.N on 03-09-2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import Fragments.CherriesFragment;
import Fragments.InboxFragment;
import Fragments.ReportsFragment;

public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InboxFragment();
            case 1:
                return new ReportsFragment();
            case 2:
                return new CherriesFragment();
        }
        return null;
    }


    @Override
    public int getCount() {
        return 3;
    }
}