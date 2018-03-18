package com.gether.bigdata.validation;

import org.junit.Before;

import javax.validation.Validator;

public abstract class BaseValidationTest {


  int TIMES = 10000000;


  Validator validator;

  UserReq userReq;

  Object validationObj = null;

  @Before
  public void before() {
    validator = getValidator();

    userReq = new UserReq();
    userReq.setAge(1);
    userReq.setRemark("remark");
    userReq.setUid(100L);
    userReq.setUserName("username");
    validationObj = userReq;
  }

  public abstract Validator getValidator();
}