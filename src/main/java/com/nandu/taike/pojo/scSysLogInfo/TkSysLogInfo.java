package com.nandu.taike.pojo.scSysLogInfo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author TK
 * @since 2019-08-22
 */
@TableName("TK_SYS_LOG_INFO")
public class TkSysLogInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String tkId;

    /**
     * 用户名
     */
    private String tkUserName;

    /**
     * IP地址
     */
    private String tkIp;

    /**
     * 访问接口名字
     */
    private String tkMethod;

    /**
     * 访问的类名
     */
    private String tkClassName;

    /**
     * 访问时间
     */
    private LocalDateTime tkCreateTime;

    /**
     * 主机名
     */
    private String tkRemoteHost;

    /**
     * 访问的URL
     */
    private String tkUrl;

    public String getTkId() {
        return tkId;
    }

    public void setTkId(String tkId) {
        this.tkId = tkId;
    }

    public String getTkUserName() {
        return tkUserName;
    }

    public void setTkUserName(String tkUserName) {
        this.tkUserName = tkUserName;
    }

    public String getTkIp() {
        return tkIp;
    }

    public void setTkIp(String tkIp) {
        this.tkIp = tkIp;
    }

    public String getTkMethod() {
        return tkMethod;
    }

    public void setTkMethod(String tkMethod) {
        this.tkMethod = tkMethod;
    }

    public String getTkClassName() {
        return tkClassName;
    }

    public void setTkClassName(String tkClassName) {
        this.tkClassName = tkClassName;
    }

    public LocalDateTime getTkCreateTime() {
        return tkCreateTime;
    }

    public void setTkCreateTime(LocalDateTime tkCreateTime) {
        this.tkCreateTime = tkCreateTime;
    }

    public String getTkRemoteHost() {
        return tkRemoteHost;
    }

    public void setTkRemoteHost(String tkRemoteHost) {
        this.tkRemoteHost = tkRemoteHost;
    }

    public String getTkUrl() {
        return tkUrl;
    }

    public void setTkUrl(String tkUrl) {
        this.tkUrl = tkUrl;
    }
}
