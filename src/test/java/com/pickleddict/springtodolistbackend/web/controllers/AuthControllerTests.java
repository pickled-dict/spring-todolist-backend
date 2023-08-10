package com.pickleddict.springtodolistbackend.web.controllers;

import com.pickleddict.springtodolistbackend.dto.JwtResponseDto;
import com.pickleddict.springtodolistbackend.dto.LoginRequestDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTests extends AbstractMvcTest {
    private static final String SIGNUP_ROUTE = "/api/auth/signup";

    private static final String SIGNIN_ROUTE = "/api/auth/signin";

    private static final LoginRequestDto VALID_LOGIN = new LoginRequestDto("mail@mail.com", "password");

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    private void mockAuthentication() {
        when(authenticationService.authenticateUser(VALID_LOGIN)).thenReturn(
                ResponseEntity.ok(new JwtResponseDto("FAKEJWT", 12L, "mail@mail.com"))
        ).thenThrow(HttpClientErrorException.BadRequest.class);
    }

    // Signup route
    @Test
    public void testSignUpReturnsCreatedResponseWhenValid() throws Exception {
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
    public void testSignUpReturnsBadRequestResponseWithNoEmail() throws Exception {
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
    public void testSignUpReturnsBadRequestResponseWithNoPassword() throws Exception {
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
    public void testSignUpReturnsBadRequestResponseWithNoUsernameAndPassword() throws Exception {
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
    public void testSignInReturnsOkWhenRequestIsValid() throws Exception {
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
    public void testSignInReturnsBadRequestWhenPasswordDoesNotExist() throws Exception {
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
    public void testSignInReturnsBadRequestWhenEmailDoesNotExist() throws Exception {
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
    public void testSignInReturnsBadRequestWhenEmailAndPasswordDoesNotExist() throws Exception {
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
