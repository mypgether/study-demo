package com.gether.bigdata.mongo;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Map;

/**
 * Created by myp on 2017/9/5.
 */
public class ProfilePojo {

  private ObjectId id;
  private String deviceId;
  private Date createTime;
  private Date modifyTime;
  // 不存储到db，外层展示
  private Map<String, Map<String, Object>> profile;

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Map<String, Map<String, Object>> getProfile() {
    return profile;
  }

  public void setProfile(Map<String, Map<String, Object>> profile) {
    this.profile = profile;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }


  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }
}