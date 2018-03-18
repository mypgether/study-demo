package com.gether.bigdata.validation;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;


public class BValValidationTest extends BaseValidationTest {

  // user:52698
  // 141917
  @Test
  public void test() {
    long start = System.currentTimeMillis();
    for (int j = 0; j < TIMES; j++) {
      Set<ConstraintViolation<Object>> violations = validator.validate(validationObj);
      if (!violations.isEmpty()) {

      }
    }
    System.out.println(System.currentTimeMillis() - start);
  }

  @Override
  public Validator getValidator() {
    return MyValidatorFactory.SINGLE_INSTANCE.getValidator();
  }
}