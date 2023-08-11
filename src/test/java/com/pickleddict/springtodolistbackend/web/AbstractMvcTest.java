package com.pickleddict.springtodolistbackend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickleddict.springtodolistbackend.security.jwt.AuthEntryPointJwt;
import com.pickleddict.springtodolistbackend.security.jwt.JwtUtils;
import com.pickleddict.springtodolistbackend.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractMvcTest {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockBean
    public UserDetailsServiceImpl userDetailsService;

    @MockBean
    public AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    public JwtUtils jwtUtils;
}
