package com.pickleddict.springtodolistbackend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickleddict.springtodolistbackend.WebSecurityConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import(WebSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
public abstract class AbstractMvcTest {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;
}
