package com.wuxiangknow.rest.bean;

import java.io.Serializable;

/**
 * @Desciption 版本
 * @Author WuXiang
 * @Date 2019/3/18 14:53
 */
public class Version implements Serializable{


    private static final long serialVersionUID = 7232108712788058525L;
    private String version;
    private String url;
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
