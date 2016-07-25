package com.ryanst.penti.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ryanst.penti.R;
import com.ryanst.penti.constant.PentiConst;
import com.ryanst.penti.core.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.pentiwang, R.string.pentiwang);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        showFragment(PentiConst.FRAGMENT_TYPE_PENTI_WANG);
    }

    private void showFragment(String type) {

        setMainTitle(type);

        List<Fragment> fragments = fragmentManager.getFragments();
        Fragment targetFragment = fragmentManager.findFragmentByTag(type);
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    transaction.hide(fragment);
                }
            }
        }

        transaction.commitAllowingStateLoss();

        FragmentTransaction newTransaction = fragmentManager.beginTransaction();

        if (targetFragment == null) {
            targetFragment = NewsListFragment.getInstance(type);
            newTransaction.add(R.id.fl_content, targetFragment, type);
        } else {
            newTransaction.show(targetFragment);
        }

        newTransaction.commitAllowingStateLoss();
    }

    private void setMainTitle(String type) {
        switch (type) {
            case PentiConst.FRAGMENT_TYPE_PENTI_WANG:
                setTitle(PentiConst.PENTI_WANG);
                break;
            case PentiConst.FRAGMENT_TYPE_TUGUA:
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
            showFragment(PentiConst.FRAGMENT_TYPE_TUGUA);
        } else if (id == R.id.nav_pentiwang) {
            showFragment(PentiConst.FRAGMENT_TYPE_PENTI_WANG);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
