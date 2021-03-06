package com.peteleco.appet.ProjetoEspecifico.MenuInicial.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.peteleco.appet.R;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final static String TAG = "SectionsPagerAdapter";

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_done, R.string.tab_text_doing, R.string.tab_text_todo,
        R.string.tab_text_ideas};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG,"getItem position: "+position);
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // esse método é chamado quando um fragment é criado
        return PlaceholderFragment.newInstance(position+1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return TAB_TITLES.length;
    }
}