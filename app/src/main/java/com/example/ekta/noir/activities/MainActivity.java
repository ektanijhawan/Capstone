package com.example.ekta.noir.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ekta.noir.R;
import com.example.ekta.noir.adapters.DrawerListAdapter;
import com.example.ekta.noir.fragments.FavouriteFragment;
import com.example.ekta.noir.fragments.FavouriteFragmentNew;
import com.example.ekta.noir.fragments.ImageFragment;
import com.example.ekta.noir.fragments.UnsplashFragment;
import com.example.ekta.noir.utils.Constants;
import com.example.ekta.noir.utils.Utils;
import com.facebook.FacebookSdk;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

public class MainActivity extends BaseActivity {
    DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    String[] mDrawerTitles;

    private CharSequence mTitle;
    private RelativeLayout mDrawerRelativeLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private static final int ADD = 0;
    private static final int UNSPLASH = 1;
    private static final int FAVOURITE = 2;

    private ImageView imageTabUnsplash, imageTabYourOwn, imageTabFav;

    private MaterialRippleLayout rippleUnsplash, rippleAdd, rippleFav;

    private Menu menu;
    CircleImageView mUserProfilePicture;
    Integer navigationOptionsLoggedin[];
    Integer navigationOptionsGuest[];

    DrawerListAdapter mDrawerListAdapter;
    TextView mUserName;
    TextView mUserEmail;
    Boolean mUserLoggedIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        mToolbar = (Toolbar) findViewById(R.id.tbMain);
        mViewPager = (ViewPager) findViewById(R.id.vpMain);
        imageTabUnsplash = (ImageView) findViewById(R.id.ivTabUnsplash);
        imageTabYourOwn = (ImageView) findViewById(R.id.ivTabYourOwn);
        imageTabFav = (ImageView) findViewById(R.id.ivTabFavourite);
        rippleUnsplash = (MaterialRippleLayout) findViewById(R.id.mrl1);
        rippleAdd = (MaterialRippleLayout) findViewById(R.id.mrl2);
        rippleFav = (MaterialRippleLayout) findViewById(R.id.mrl3);
        mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.rl_navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mUserProfilePicture = (CircleImageView) findViewById(R.id.iv_profile_image);
        mUserName = (TextView) findViewById(R.id.tv_user_name);
        mUserEmail = (TextView) findViewById(R.id.tv_user_email);
        navigationOptionsLoggedin = new Integer[]{
                R.drawable.ic_profileicon,

        };
        navigationOptionsGuest = new Integer[]{
                R.drawable.ic_profileicon,

        };
        setDataInDrawer();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mToolbar.setTitle("Noir");

        imageTabUnsplash.setColorFilter(getResources().getColor(R.color.colorWhite));
        imageTabFav.setColorFilter(getResources().getColor(R.color.tabIcons));

        rippleUnsplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.setCurrentItem(0, true);
                imageTabUnsplash.setColorFilter(getResources().getColor(R.color.colorWhite));
                imageTabYourOwn.setColorFilter(getResources().getColor(R.color.tabIcons));
                imageTabFav.setColorFilter(getResources().getColor(R.color.tabIcons));
            }
        });
        rippleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.setCurrentItem(1, true);

                imageTabUnsplash.setColorFilter(getResources().getColor(R.color.tabIcons));
                imageTabYourOwn.setColorFilter(getResources().getColor(R.color.colorWhite));
                imageTabFav.setColorFilter(getResources().getColor(R.color.tabIcons));
            }
        });
        rippleFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.setCurrentItem(2, true);

                imageTabUnsplash.setColorFilter(getResources().getColor(R.color.tabIcons));
                imageTabYourOwn.setColorFilter(getResources().getColor(R.color.tabIcons));
                imageTabFav.setColorFilter(getResources().getColor(R.color.colorWhite));
            }
        });

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(" ");

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    imageTabUnsplash.setColorFilter(getResources().getColor(R.color.colorWhite));
                    imageTabYourOwn.setColorFilter(getResources().getColor(R.color.tabIcons));
                    imageTabFav.setColorFilter(getResources().getColor(R.color.tabIcons));
//                    menu.setGroupVisible(R.id.gShuffle, false);
                }
                if(position == 1){
                    imageTabUnsplash.setColorFilter(getResources().getColor(R.color.tabIcons));
                    imageTabYourOwn.setColorFilter(getResources().getColor(R.color.colorWhite));
                    imageTabFav.setColorFilter(getResources().getColor(R.color.tabIcons));
//                    menu.setGroupVisible(R.id.gShuffle, true);
                }
                if (position == 2){
                    imageTabUnsplash.setColorFilter(getResources().getColor(R.color.tabIcons));
                    imageTabYourOwn.setColorFilter(getResources().getColor(R.color.tabIcons));
                    imageTabFav.setColorFilter(getResources().getColor(R.color.colorWhite));
//                    menu.setGroupVisible(R.id.gShuffle, false);
                }
            }
        });
    }
    public void setDataInDrawer() {


        if (Utils.getUserProfileSharedPrefs(getApplicationContext(), Constants.USER_NAME).equals(Constants.EMPTY)) {

            mDrawerTitles = getResources().getStringArray(R.array.navigation_options_guest);

            mDrawerListAdapter = new DrawerListAdapter(MainActivity.this, mDrawerTitles, navigationOptionsGuest);
            mDrawerList.setAdapter(mDrawerListAdapter);

            mUserProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
            mUserName.setText(" Guest");
            mUserEmail.setText(" ");
        } else {
            mDrawerTitles = getResources().getStringArray(R.array.navigation_options_logged_in);

            mDrawerListAdapter = new DrawerListAdapter(MainActivity.this, mDrawerTitles, navigationOptionsLoggedin);
            mDrawerList.setAdapter(mDrawerListAdapter);


            String userName = Utils.getUserProfileSharedPrefs(MainActivity.this, Constants.USER_NAME);
            String userEmail = Utils.getUserProfileSharedPrefs(MainActivity.this, Constants.USER_EMAIL);
            String userProfileUrl = Utils.getUserProfileSharedPrefs(MainActivity.this, Constants.USER_PROFILE_URL);


            Glide.with(this).load(userProfileUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mUserProfilePicture);
            mUserName.setText(userName);
            mUserEmail.setText(userEmail);

        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){

                case ADD:
                    fragment = ImageFragment.newInstance("", "");
                    break;

                case UNSPLASH:
                    fragment = UnsplashFragment.newInstance("", "");
                    break;

                case FAVOURITE:
//                    fragment = FavouriteFragment.newInstance("", "");
                    fragment = FavouriteFragmentNew.newInstance("", "");

                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;


        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        if ((Utils.getUserProfileSharedPrefs(getApplicationContext(), Constants.USER_NAME).equals(Constants.EMPTY))) {

            switch (position) {
                case 0:
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
            }
        } else {

            switch (position) {

                case 0:

                    LoginActivity loginActivity = new LoginActivity();
                    Boolean signed_out = signOut();
                    if (signed_out) {
                        Utils.setUserProfileSharedPreference(getApplicationContext(), Constants.USER_NAME, Constants.EMPTY);
                        Utils.setUserProfileSharedPreference(getApplicationContext(), Constants.USER_EMAIL, Constants.EMPTY);
                        Utils.setUserProfileSharedPreference(getApplicationContext(), Constants.USER_PROFILE_URL, Constants.EMPTY);
                        setDataInDrawer();
                        Toast.makeText(this, "logged out", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }

        }

        // Highlight the selected item, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerRelativeLayout);
    }
}
