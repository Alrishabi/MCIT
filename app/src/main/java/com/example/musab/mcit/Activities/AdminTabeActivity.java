package com.example.musab.mcit.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.example.musab.mcit.AdminFragments.InfoAdminFragment;
import com.example.musab.mcit.AdminFragments.QuestionnaireFragment;
import com.example.musab.mcit.AdminFragments.RecyclerAdminFragment;
import com.example.musab.mcit.AdminFragments.VoteAdminFragment;
import com.example.musab.mcit.R;
import com.example.musab.mcit.UserFragments.BroadCastFrag;

public class AdminTabeActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int[] tabIcons = {
            R.drawable.ic_tabe_speaker,
            R.drawable.ic_my_broadcast_tabe_icon,
            R.drawable.ic_vote_tabe_icon,
            R.drawable.ic_qution_tabe_icon,
            R.drawable.your_info_tabe_icon
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_tabe_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
            }
        });
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    BroadCastFrag broadCastFrag= new BroadCastFrag();
                    return broadCastFrag;
                case 1:
                    RecyclerAdminFragment recyclerAdminFragment= new RecyclerAdminFragment();
                    return recyclerAdminFragment;
                case 2:
                    VoteAdminFragment voteAdminFragment = new VoteAdminFragment();
                    return voteAdminFragment;
                case 3:
                    QuestionnaireFragment questionnaireFragment = new QuestionnaireFragment();
                    return questionnaireFragment;
                case 4:
                    InfoAdminFragment infoAdminFragment = new InfoAdminFragment();
                    return infoAdminFragment;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.broadcast_tabe_lable);
                case 1:
                    return getResources().getString(R.string.my_broadcast_tabe_lable);
                case 2:
                    return getResources().getString(R.string.voting_tabe_lable);
                case 3:
                    return getResources().getString(R.string.questionnaire_tabe_lable);
                case 4:
                    return getResources().getString(R.string.admin_info_tabe_lable);
            }
            return null;
        }
    }
}
