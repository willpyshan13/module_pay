package com.fjace.pay.service;


import com.fjace.pay.core.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 *
 */
@Service
public class ValidateService {

    @Autowired
    private Validator validator;

    public void validate(Object obj) {
        Set<ConstraintViolation<Object>> resultSet = validator.validate(obj);
        if (resultSet == null || resultSet.isEmpty()) {
            return;
        }
        resultSet.forEach(item -> {
            throw new BizException(item.getMessage());
        });
    }

}
