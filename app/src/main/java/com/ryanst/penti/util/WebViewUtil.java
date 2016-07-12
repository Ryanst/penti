package com.ryanst.penti.util;

import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by kevin on 16/3/18.
 */
public class WebViewUtil {
    private static final int FULL_PERCENT = 100;

    /**
     * webview  设置方法
     *
     * @param webView
     * @param listener
     * @param progressBarListener
     */
    public static void setWebViewSettings(final WebView webView,
                                          final JsListener listener,
                                          final ProgressBarListener progressBarListener) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultFontSize(38);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        }
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setScrollContainer(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.setTransitionGroup(true);
        }
        
        webView.addJavascriptInterface(new JsClient(listener), "js");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBarListener != null) {
                    progressBarListener.hideProgressBar(false, newProgress);
                }
            }
        });
    }


    public static class MyWebViewClient extends WebViewClient {
        private ProgressBarListener progressBarListener;

        public MyWebViewClient(ProgressBarListener progressBarListener) {
            this.progressBarListener = progressBarListener;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBarListener != null) {
                progressBarListener.hideProgressBar(true, FULL_PERCENT);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            view.loadUrl("file:///android_asset/error_page.html");
            if (progressBarListener != null) {
                progressBarListener.hideProgressBar(true, FULL_PERCENT);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            view.loadUrl("file:///android_asset/error_page.html");
            if (progressBarListener != null) {
                progressBarListener.hideProgressBar(true, FULL_PERCENT);
            }
        }
    }

    public static class JsClient {
        private JsListener listener;

        public JsClient(JsListener listener) {
            this.listener = listener;
        }

        @JavascriptInterface
        public void reloadPage() {
            if (listener != null) {
                listener.reload();
            }
        }
    }

    public interface JsListener {
        void reload();

    }

    public interface ProgressBarListener {
        void hideProgressBar(boolean hide, int progress);
    }

}
