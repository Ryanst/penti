package com.ryanst.penti.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ryanst.penti.R;
import com.ryanst.penti.core.BaseActivity;
import com.ryanst.penti.databinding.ActivityAboutBinding;

/**
 * Created by zhengjuntong on 7/12/16.
 */
public class AboutActivity extends BaseActivity {

    private ActionBar actionBar;
    ActivityAboutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        setSupportActionBar(binding.toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initView();
    }

    private void initView() {

        CharSequence pentiwangLink =Html.fromHtml("<font color='#407FF3'><a href=\"http://www.dapenti.com\">dapenti.com</a></font>");
        binding.contentAbout.tvPentiwang.setMovementMethod(LinkMovementMethod.getInstance());
        binding.contentAbout.tvPentiwang.setText(pentiwangLink);

        CharSequence githubLink = Html.fromHtml("<font color='#407FF3'><a href=\"http://www.github.com/Ryanst/penti\">www.github.com/Ryanst/penti</a></font>");
        binding.contentAbout.tvPentiGithub.setMovementMethod(LinkMovementMethod.getInstance());
        binding.contentAbout.tvPentiGithub.setText(githubLink);

        CharSequence zhengjtLink = Html.fromHtml("<font color='#407FF3'><a href=\"http://zhengjt.com\">zhengjt.com</a></font>");
        binding.contentAbout.tvZhengjtLink.setMovementMethod(LinkMovementMethod.getInstance());
        binding.contentAbout.tvZhengjtLink.setText(zhengjtLink);

    }

    public void fabOnClick(View view) {
        Uri uri = Uri.parse(String.format(this.getString(R.string.market), this.getPackageName()));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(this.getPackageManager()) != null)
            this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
