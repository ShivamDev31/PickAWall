package com.droidacid.pickawall;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.droidacid.pickawall.adapter.NavDrawerListAdapter;
import com.droidacid.pickawall.app.AppController;
import com.droidacid.pickawall.picasa.model.Category;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
    //private Toolbar toolbar;
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Navigation drawer title
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private List<Category> albumList;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);*/

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slider_menu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // Getting the albums from SHared Prefs
        albumList = AppController.getInstance().getPrefManager().getCategories();

        // Insert "Recently Added" in navigation drawer fist position
        Category recentAlbum = new Category(null, getString(R.string.nav_drawer_recently_added));
        albumList.add(0, recentAlbum);

        // Loop through the albums and add them to navigation drawer adapter

        for (Category cat : albumList) {
            navDrawerItems.add(new NavDrawerItem(cat.getId(), cat.getTitle()));
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // Setting the navigation drawer list adapter

        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // Enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(
                android.R.color.transparent)));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);

                // Calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);

                // calling onPrepareOptionsMenu() to hide from action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            // on first time display view for first navigation item
            displayView(0);
        }
    }


    /*
     * Navigation drawer menu item click listener
     */
    public class SlideMenuClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // on item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {
            case R.id.action_settings:
                // Selected settings menu item
                // launch Settings activity
                //startActivity(Intent(MainActivity.this,
                //        SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Called when inValidateOptionsMenu() is triggered


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If nav drawer is opened, hide tha action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    // Displaying fragment view for selected nav drawer list item
    private void displayView(int position) {
        // Update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                // Recently added item, selected
                // dont pass album id to grid fragment
                //fragment = GridFragment.newInstance(null);
                fragment = GridFragment.newInstance(null);
                break;
            default:
                // selected wallpaper category
                // send album id to home fragment to list all the wallpapers
                String albumId = albumList.get(position).getId();
                fragment = GridFragment.newInstance(albumId);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(albumList.get(position).getTitle());
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // Error in creating fragment
            L.l(TAG, "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    // When using the ActionBarDrawerToggle, you must call it during
    // onPostCreate() and onConfigurationChanged()


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after the onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
