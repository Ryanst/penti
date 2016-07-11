package com.ryanst.penti.bean;

import java.util.List;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class News {

    /**
     * sourceID : d1b01607-873f-400e-bd2f-332e5b9ce7f6_1
     * posttime : 昨天 13:58
     * contentType : news
     * contentID : d682e3f11d5a40c9900a66ea24a61d07_1
     * entryID : d682e3f11d5a40c9900a66ea24a61d07_1
     * breif : <p>免责申明：</p><p>以下内容，有可能引起内心冲突或愤怒等不适症状。若有此症状自觉被误导者，请绕行。若按捺不住看后症状特别明显，可自行前往CCAV等欢乐频道进行综合调理。其余，概不负责 。</p><p>本文转摘的各类事件，均来自于公开发表的国内媒体报道。引用的个人或媒体评论旨在传播各种声音，并不代表我们认同或反对其观点。</p><p>欢迎转载，转载请保证原文的完整性（不得随意增删内容，或篡改图卦名称等），请注明来源和链接。...</p>
     * shareContent : 好文分享：“【喷嚏图卦20160710】即使是中产阶级人群也开始对未来失去安全感，为养老和孩子的未来担心”。网易云阅读的订阅源《喷嚏网》不错， 值得一看~
     * canShareByMail : true
     * title : 【喷嚏图卦20160710】即使是中产阶级人群也开始对未来失去安全感，为养老和孩子的未来担心
     * source : http://www.dapenti.com/blog/more.asp?name=xilei&id=112870
     * sourceURL : http://www.dapenti.com/blog/more.asp?name=xilei&id=112870
     * isTopic : false
     * shareUrl : http://yuedu.163.com/c/d1b01607-873f-400e-bd2f-332e5b9ce7f6_1/d682e3f11d5a40c9900a66ea24a61d07_1
     * images : ["http://easyread.ph.126.net/9AYe_i3zcQ-UqVPgb87XXA==/8796093022735135159.jpg","http://easyread.ph.126.net/hI6Gy5bJdNCheCF1YaETdA==/8796093022735134201.jpg?center=635,273","http://easyread.ph.126.net/5xhJGaYUeX3Yu6tZYd89dQ==/8796093022735135163.jpg?center=311,171"]
     * read : unread
     * favor : unstar
     * summaryImage : {"summaryImageURL":"http://easyread.ph.126.net/hI6Gy5bJdNCheCF1YaETdA==/8796093022735134201.jpg?center=635,273","summaryImageHeight":371,"summaryImageWidth":660}
     */

    private String sourceID;
    private String posttime;
    private String contentType;
    private String contentID;
    private String entryID;
    private String breif;
    private String shareContent;
    private boolean canShareByMail;
    private String title;
    private String source;
    private String sourceURL;
    private boolean isTopic;
    private String shareUrl;
    private String read;
    private String favor;
    /**
     * summaryImageURL : http://easyread.ph.126.net/hI6Gy5bJdNCheCF1YaETdA==/8796093022735134201.jpg?center=635,273
     * summaryImageHeight : 371
     * summaryImageWidth : 660
     */

    private SummaryImageBean summaryImage;
    private List<String> images;

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getEntryID() {
        return entryID;
    }

    public void setEntryID(String entryID) {
        this.entryID = entryID;
    }

    public String getBreif() {
        return breif;
    }

    public void setBreif(String breif) {
        this.breif = breif;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public boolean isCanShareByMail() {
        return canShareByMail;
    }

    public void setCanShareByMail(boolean canShareByMail) {
        this.canShareByMail = canShareByMail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public boolean isIsTopic() {
        return isTopic;
    }

    public void setIsTopic(boolean isTopic) {
        this.isTopic = isTopic;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getFavor() {
        return favor;
    }

    public void setFavor(String favor) {
        this.favor = favor;
    }

    public SummaryImageBean getSummaryImage() {
        return summaryImage;
    }

    public void setSummaryImage(SummaryImageBean summaryImage) {
        this.summaryImage = summaryImage;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public static class SummaryImageBean {
        private String summaryImageURL;
        private int summaryImageHeight;
        private int summaryImageWidth;

        public String getSummaryImageURL() {
            return summaryImageURL;
        }

        public void setSummaryImageURL(String summaryImageURL) {
            this.summaryImageURL = summaryImageURL;
        }

        public int getSummaryImageHeight() {
            return summaryImageHeight;
        }

        public void setSummaryImageHeight(int summaryImageHeight) {
            this.summaryImageHeight = summaryImageHeight;
        }

        public int getSummaryImageWidth() {
            return summaryImageWidth;
        }

        public void setSummaryImageWidth(int summaryImageWidth) {
            this.summaryImageWidth = summaryImageWidth;
        }
    }
}
