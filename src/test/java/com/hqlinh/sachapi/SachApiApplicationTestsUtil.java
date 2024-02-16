package com.hqlinh.sachapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SachApiApplicationTestsUtil {

    private ModelMapper mapper;
    @BeforeEach
    public void setup() {
        this.mapper = new ModelMapper();
    }

    @Test
    public void mapperTest() {

    }

}
