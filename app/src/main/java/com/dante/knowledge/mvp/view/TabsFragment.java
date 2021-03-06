package com.dante.knowledge.mvp.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dante.knowledge.R;
import com.dante.knowledge.ui.BaseFragment;
import com.dante.knowledge.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * A tab fragment {@link Fragment} contains different news fragment.
 */
public class TabsFragment extends BaseFragment {

    private static final int SMOOTHSCROLL_TOP_POSITION = 50;
    public static final int TYPE_ZHIHU = 1024;
    public static final int TYPE_FRESH = 1025;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabs;
    public static final String MENU_NEWS = "news";
    public static final String MENU_PIC = "pic";
    public static final String MENU_SECRET = "secret";
    public static final String MENU_H = "h";

    private List<RecyclerFragment> fragments = new ArrayList<>();
    private TabPagerAdapter adapter;
    private String menuType;

    public static TabsFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constants.TYPE, type);
        TabsFragment fragment = new TabsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_news_tab;
    }

    @Override
    protected void initViews() {
        adapter = new TabPagerAdapter(getChildFragmentManager());
        initFragments();
        pager.setAdapter(adapter);
        if (MENU_PIC.equals(menuType)) {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        tabs.setupWithViewPager(pager);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                scrollToTop(fragments.get(tab.getPosition()).getRecyclerView());
            }
        });
    }

    private void initFragments() {
        menuType = getArguments().getString(Constants.TYPE);

        List<String> mTitles;
        if (MENU_PIC.equals(menuType)) {
            String[] titles = new String[]{
                    getString(R.string.gank),
                    getString(R.string.db_rank),
                    getString(R.string.db_leg),
                    getString(R.string.db_silk),
                    getString(R.string.db_breast),
                    getString(R.string.db_butt)};
            mTitles = Arrays.asList(titles);
            fragments.add(PictureFragment.newInstance(PictureFragment.TYPE_GANK));
            fragments.add(PictureFragment.newInstance(PictureFragment.TYPE_DB_RANK));
            fragments.add(PictureFragment.newInstance(PictureFragment.TYPE_DB_LEG));
            fragments.add(PictureFragment.newInstance(PictureFragment.TYPE_DB_SILK));
            fragments.add(PictureFragment.newInstance(PictureFragment.TYPE_DB_BREAST));
            fragments.add(PictureFragment.newInstance(PictureFragment.TYPE_DB_BUTT));
            if (fragments.size() != titles.length) {
                throw new IllegalArgumentException("You need add all fragments in "+getClass().getSimpleName());
            }

        } else if (MENU_SECRET.equals(menuType)) {
            String[] titles = new String[]{getString(R.string.h_beauty), getString(R.string.h_selfie), getString(R.string.h_exposure), getString(R.string.h_original)};
            mTitles = Arrays.asList(titles);
            fragments.add(HFragment.newInstance(HFragment.TYPE_H_BEAUTY));
            fragments.add(HFragment.newInstance(HFragment.TYPE_H_SELFIE));
            fragments.add(HFragment.newInstance(HFragment.TYPE_H_EXPOSURE));
            fragments.add(HFragment.newInstance(HFragment.TYPE_H_ORIGINAL));

        } else {
            mTitles = new ArrayList<>();
            fragments.add(new ZhihuFragment());
            fragments.add(new FreshFragment());
            mTitles.add(getString(R.string.zhihu_news));
            mTitles.add(getString(R.string.fresh_news));
        }
        adapter.setFragments(fragments, mTitles);
    }

    private void scrollToTop(RecyclerView list) {
        if (null != list) {
            int lastPosition;
            if (MENU_PIC.equals(menuType)) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) list.getLayoutManager();
                lastPosition = manager.findLastVisibleItemPositions(
                        new int[manager.getSpanCount()])[1];

            } else {
                LinearLayoutManager manager = (LinearLayoutManager) list.getLayoutManager();
                lastPosition = manager.findLastVisibleItemPosition();
            }
            if (lastPosition < SMOOTHSCROLL_TOP_POSITION) {
                list.smoothScrollToPosition(0);
            } else {
                list.scrollToPosition(0);
            }
        }
    }

    @Override
    protected void initData() {

    }


    public static class TabPagerAdapter extends FragmentPagerAdapter {

        private List<RecyclerFragment> fragments;
        private List<String> titles;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setFragments(List<RecyclerFragment> fragments, List<String> titles) {
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public float getPageWidth(int position) {
            return super.getPageWidth(position);
        }
    }

}
