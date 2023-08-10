package com.pickleddict.springtodolistbackend.web.controllers;

import com.pickleddict.springtodolistbackend.web.AbstractMvcTest;
import com.pickleddict.springtodolistbackend.controllers.AuthController;
import com.pickleddict.springtodolistbackend.dto.SignupRequestDto;
import com.pickleddict.springtodolistbackend.security.jwt.AuthEntryPointJwt;
import com.pickleddict.springtodolistbackend.security.jwt.JwtUtils;
import com.pickleddict.springtodolistbackend.security.services.UserDetailsServiceImpl;
import com.pickleddict.springtodolistbackend.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTests extends AbstractMvcTest {
    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    public void testSignUpReturnsCreatedResponseWhenValid() throws Exception {
        SignupRequestDto signupRequestDto = new SignupRequestDto("email@mail.com", "password");
        String requestBody = objectMapper.writeValueAsString(signupRequestDto);

         mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signup")
                        .content(requestBody)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                 .andDo(print())
                 .andExpect(status().isCreated());
    }

    @Test
    public void testSignUpReturnsBadRequestResponseWithNoEmail() throws Exception {
        SignupRequestDto signupRequest = new SignupRequestDto();
        signupRequest.setPassword("password");
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signup")
                        .content(requestBody)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testSignUpReturnsBadRequestResponseWithNoPassword() throws Exception {
        SignupRequestDto loginRequestDto = new SignupRequestDto();
        loginRequestDto.setEmail("mail@mail.com");
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signup")
                        .content(requestBody)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSignUpReturnsBadRequestResponseWithNoUsernameAndPassword() throws Exception {
        SignupRequestDto loginRequestDto = new SignupRequestDto();
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signup")
                        .content(requestBody)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
