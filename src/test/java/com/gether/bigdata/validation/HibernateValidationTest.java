package com.gether.bigdata.validation;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


public class HibernateValidationTest extends BaseValidationTest {

    //user:169399
    //482430
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
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }
}