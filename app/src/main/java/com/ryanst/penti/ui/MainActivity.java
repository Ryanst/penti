package com.ryanst.penti.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ryanst.penti.R;
import com.ryanst.penti.constant.PentiConst;
import com.ryanst.penti.core.BaseActivity;
import com.ryanst.penti.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navView;
    private FragmentManager fragmentManager;

    ActivityMainBinding binding;
    private String selectedType = PentiConst.TYPE_PENTI_WANG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        fragmentManager = getSupportFragmentManager();
        showFragment(PentiConst.TYPE_PENTI_WANG);
    }

    private void initView() {

        toolbar = binding.layoutContentMain.toolbar;
        drawer = binding.drawerLayout;
        navView = binding.navView;

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.pentiwang, R.string.pentiwang);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
    }

    private void showFragment(String type) {
        setMainTitle(type);
        Fragment targetFragment = fragmentManager.findFragmentByTag(type);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (targetFragment == null) {
            targetFragment = NewsListFragment.getInstance(type);
            transaction.add(R.id.fl_content, targetFragment, type);
        } else {
            transaction.replace(R.id.fl_content, targetFragment, type);
        }
        transaction.commit();

        ((NewsListFragment) targetFragment).setRefresh();
    }

    private void setMainTitle(String type) {
        switch (type) {
            case PentiConst.TYPE_PENTI_WANG:
                setTitle(PentiConst.PENTI_WANG);
                break;
            case PentiConst.TYPE_TUGUA:
                setTitle(PentiConst.TUGUA);
                break;
            default:
                setTitle(PentiConst.PENTI_WANG);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            NewsListFragment fragment = (NewsListFragment) fragmentManager.findFragmentByTag(selectedType);
            fragment.setRefresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tugua) {
            selectedType = PentiConst.TYPE_TUGUA;
            showFragment(PentiConst.TYPE_TUGUA);
        } else if (id == R.id.nav_pentiwang) {
            selectedType = PentiConst.TYPE_PENTI_WANG;
            showFragment(PentiConst.TYPE_PENTI_WANG);
//        } else if (id == R.id.nav_setting) {
//
//        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
