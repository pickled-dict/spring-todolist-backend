package com.pickleddict.springtodolistbackend.web.controllers;

import com.pickleddict.springtodolistbackend.WebSecurityConfig;
import com.pickleddict.springtodolistbackend.dto.JwtResponseDto;
import com.pickleddict.springtodolistbackend.dto.LoginRequestDto;
import com.pickleddict.springtodolistbackend.web.AbstractMvcTest;
import com.pickleddict.springtodolistbackend.controllers.AuthController;
import com.pickleddict.springtodolistbackend.dto.SignupRequestDto;
import com.pickleddict.springtodolistbackend.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(WebSecurityConfig.class)
public class AuthControllerTests extends AbstractMvcTest {
    private static final String SIGNUP_ROUTE = "/api/auth/signup";

    private static final String SIGNIN_ROUTE = "/api/auth/signin";

    private static final LoginRequestDto VALID_LOGIN = new LoginRequestDto("mail@mail.com", "password");

    @MockBean
    private AuthenticationService authenticationService;

    private void mockAuthentication() {
        when(authenticationService.authenticateUser(VALID_LOGIN)).thenReturn(
                ResponseEntity.ok(new JwtResponseDto("FAKEJWT", 12L, "mail@mail.com"))
        ).thenThrow(HttpClientErrorException.BadRequest.class);
    }

    // Signup route
    @Test
    public void SignUp_ValidRequestBody_CreatedResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new SignupRequestDto("email@mail.com", "password"));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGNUP_ROUTE)
                        .content(requestBody)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isCreated());
    }

    @Test
    public void Signup_NoEmail_BadRequestResponse() throws Exception {
        SignupRequestDto signupRequest = new SignupRequestDto();
        signupRequest.setPassword("password");
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGNUP_ROUTE)
                        .content(requestBody)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void SignUp_NoPassword_BadRequestResponse() throws Exception {
        SignupRequestDto loginRequestDto = new SignupRequestDto();
        loginRequestDto.setEmail("mail@mail.com");
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGNUP_ROUTE)
                        .content(requestBody)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void SignUp_NoEmailNoPassword_BadRequestResponse() throws Exception {
        SignupRequestDto loginRequestDto = new SignupRequestDto();
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGNUP_ROUTE)
                        .content(requestBody)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    // signin route
    @Test
    public void SignIn_ValidRequest_OkResponse() throws Exception {
        mockAuthentication();
        String loginRequestBody = objectMapper.writeValueAsString(VALID_LOGIN);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(SIGNIN_ROUTE)
                .content(loginRequestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void SignIn_NoPassword_BadRequestResponse() throws Exception {
        mockAuthentication();
        LoginRequestDto badLoginRequest = new LoginRequestDto();
        badLoginRequest.setEmail("mail@mail.com");
        String badLoginRequestBody = objectMapper.writeValueAsString(badLoginRequest);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(SIGNIN_ROUTE)
                .content(badLoginRequestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isBadRequest());
    }


    @Test
    public void SignIn_NoEmail_BadRequestResponse() throws Exception {
        mockAuthentication();
        LoginRequestDto badLoginRequest = new LoginRequestDto();
        badLoginRequest.setPassword("password");
        String badLoginRequestBody = objectMapper.writeValueAsString(badLoginRequest);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(SIGNIN_ROUTE)
                .content(badLoginRequestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void SignIn_NoEmailNoPassword_BadRequestResponse() throws Exception {
        mockAuthentication();
        LoginRequestDto badLoginRequest = new LoginRequestDto();
        String badLoginRequestBody = objectMapper.writeValueAsString(badLoginRequest);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(SIGNIN_ROUTE)
                .content(badLoginRequestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isBadRequest());
    }
}
