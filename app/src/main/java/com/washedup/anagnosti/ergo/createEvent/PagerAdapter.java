package com.washedup.anagnosti.ergo.createEvent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfPages;

    public PagerAdapter(FragmentManager fm, int numberOfPages) {
        super(fm);
        this.mNoOfPages = numberOfPages;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SliderInfoFragment sif = new SliderInfoFragment();
                return sif;
            case 1:
                SliderLinksFragment slf = new SliderLinksFragment();
                return slf;
            case 2:
                SliderDaysFragment sdf = new SliderDaysFragment();
                return sdf;
            case 3:
                SliderRolesFragment srf = new SliderRolesFragment();
                return srf;
            case 4:
                SliderPeopleFragment spf = new SliderPeopleFragment();
                return spf;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNoOfPages;
    }
}
