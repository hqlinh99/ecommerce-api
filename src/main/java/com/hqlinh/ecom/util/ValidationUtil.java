package com.hqlinh.ecom.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.SneakyThrows;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ValidationUtil {
    @SneakyThrows
    public static <T> void validate(Map<String, Object> fields, Class<T> tClass) {
        BindingResult bindingResult = new BeanPropertyBindingResult(fields, fields.getClass().getSimpleName());
        execute(fields, bindingResult, tClass);
    }

    @SneakyThrows
    public static <T, S> void validate(T object, Class<S> tClass) {
        Map<String, Object> fields = ValueMapper.objectToMap(object);
        BindingResult bindingResult = new BeanPropertyBindingResult(fields, object.getClass().getSimpleName());
        execute(fields, bindingResult, tClass);

    }

    private static <S> void execute(Map<String, Object> fields, BindingResult bindingResult, Class<S> sClass) throws MethodArgumentNotValidException {
        Set<Set<ConstraintViolation<S>>> constraintsSet = new HashSet<>();
        fields.forEach((String key, Object value) -> {
            if (key.equals("price")) value = Long.parseLong(value.toString());
            if (key.equals("quantity")) value = Integer.parseInt(value.toString());
            constraintsSet.add(Validation.buildDefaultValidatorFactory().getValidator().validateValue(sClass, key, value));
        });
        if (!constraintsSet.isEmpty()) {
            constraintsSet.forEach(constraints -> {
                constraints.forEach(violation -> {
                    bindingResult.addError(new FieldError(
                            "Map<String, Object>",
                            violation.getPropertyPath().toString(),
                            violation.getMessage()
                    ));
                });
            });
            if (bindingResult.hasErrors())
                throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }
}
