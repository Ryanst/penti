package com.ryanst.penti.network;

import com.ryanst.penti.bean.News;

import java.util.List;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class GetListResponse extends BaseResponse {

    private MoreInfo moreInfo;

    private List<News> newsList;

    public MoreInfo getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(MoreInfo moreInfo) {
        this.moreInfo = moreInfo;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public static class MoreInfo {

        /**
         * more : true
         * unsupported : false
         * nextPageToken : 146771982014677203860363196764
         */

        private boolean more;
        private boolean unsupported;
        private String nextPageToken;

        public boolean isMore() {
            return more;
        }

        public void setMore(boolean more) {
            this.more = more;
        }

        public boolean isUnsupported() {
            return unsupported;
        }

        public void setUnsupported(boolean unsupported) {
            this.unsupported = unsupported;
        }

        public String getNextPageToken() {
            return nextPageToken;
        }

        public void setNextPageToken(String nextPageToken) {
            this.nextPageToken = nextPageToken;
        }
    }


}
