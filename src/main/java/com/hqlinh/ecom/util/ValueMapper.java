package com.hqlinh.ecom.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;

import java.util.Map;

public class ValueMapper {
    public static String jsonAsString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //objectToMap
    public static <T> Map<String, Object> objectToMap(T obj){
        return new ObjectMapper().convertValue(obj, Map.class);
    }
}
