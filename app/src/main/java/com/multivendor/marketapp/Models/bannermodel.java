package com.multivendor.marketapp.Models;

import java.util.List;

public class bannermodel {

    public class bannerresp{
        public String success;
        public  banneresult result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public banneresult getResult() {
            return result;
        }

        public void setResult(banneresult result) {
            this.result = result;
        }
    }

    public class banneresult{
        public List<singleBannerresp> banner1list;
        public List<singleBannerresp> banner2list;


        public List<singleBannerresp> getBanner1list() {
            return banner1list;
        }

        public void setBanner1list(List<singleBannerresp> banner1list) {
            this.banner1list = banner1list;
        }

        public List<singleBannerresp> getBanner2list() {
            return banner2list;
        }

        public void setBanner2list(List<singleBannerresp> banner2list) {
            this.banner2list = banner2list;
        }
    }
    public class singleBannerresp {
        private String id;
        private String image;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
