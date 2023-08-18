package com.pickleddict.springtodolistbackend.web.controllers;

import com.pickleddict.springtodolistbackend.controllers.TodoController;
import com.pickleddict.springtodolistbackend.dto.TodoDto;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import com.pickleddict.springtodolistbackend.models.Todo;
import com.pickleddict.springtodolistbackend.services.TodoService;
import com.pickleddict.springtodolistbackend.web.AbstractMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.UnexpectedException;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TodoController.class)
public class TodoControllerTests extends AbstractMvcTest {
    private static final String ROUTE = "/api/todo";

    private static final Long TODOLIST_ID = 111L;

    private static final Long TODO_ID = 1L;

    private static final TodoDto VALID_TODO = new TodoDto("some content");

    private static final TodoDto INVALID_TODO = new TodoDto();

    @MockBean
    TodoService todoService;

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void CreateTodo_ValidBodyAndAuth_OkResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(VALID_TODO);
        mockCreateTodo();

        ResultActions result = performCreateTodoRequest(requestBody);

        result.andDo(print()).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void CreateTodo_InvalidBodyValidAuth_BadRequestResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(INVALID_TODO);
        mockCreateTodo();

        ResultActions result = performCreateTodoRequest(requestBody);

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void CreateTodo_ValidBodyInvalidAuth_UnauthorizedResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(VALID_TODO);
        mockCreateTodo();

        ResultActions result = performCreateTodoRequest(requestBody);

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void GetTodoById_ValidIdValidAuth_OkResponse() throws Exception {
        mockGetTodo();

        ResultActions result = performGetTodoRequest();

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void GetTodoById_InvalidIdValidAuth_BadRequestResponse() throws Exception {
        mockGetTodo();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(ROUTE)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void GetTodoById_ValidIdInvalidAuth_UnauthorizedResponse() throws Exception {
        mockGetTodo();

        ResultActions result = performGetTodoRequest();

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void UpdateTodo_ValidBodyAndAuth_OkResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(VALID_TODO);
        mockUpdateTodo();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(ROUTE + "/" + TODO_ID)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void UpdateTodo_InvalidBodyValidAuth_BadRequestResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(INVALID_TODO);
        mockUpdateTodo();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(ROUTE + "/" + TODO_ID)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void UpdateTodo_ValidBodyInvalidAuth_UnauthorizedResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(VALID_TODO);
        mockUpdateTodo();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(ROUTE + "/" + TODO_ID)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void DeleteTodo_ValidUser_OkResponse() throws Exception {
        when(todoService.deleteTodo(TODO_ID))
                .thenReturn(ResponseEntity.ok(new MessageResponse("ok")))
                .thenThrow(HttpClientErrorException.BadRequest.class);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete(ROUTE + "/" + TODO_ID)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void DeleteTodo_InvalidUser_UnauthorizedResponse() throws Exception {
        when(todoService.deleteTodo(TODO_ID))
                .thenReturn(ResponseEntity.ok(new MessageResponse("ok")))
                .thenThrow(HttpClientErrorException.BadRequest.class);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete(ROUTE + "/" + TODO_ID)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    private void mockCreateTodo() throws UnexpectedException {
        when(todoService.createTodo(VALID_TODO, TODOLIST_ID))
                .thenReturn(ResponseEntity.ok(new Todo()))
                .thenThrow(HttpClientErrorException.BadRequest.class);
    }

    private void mockGetTodo() {
        when(todoService.getTodoById(TODO_ID))
                .thenReturn(ResponseEntity.ok(new Todo()))
                .thenThrow(HttpClientErrorException.BadRequest.class);
    }

    private void mockUpdateTodo() {
        when(todoService.updateTodo(VALID_TODO, TODO_ID))
                .thenReturn(ResponseEntity.ok(new Todo()))
                .thenThrow(HttpClientErrorException.BadRequest.class);
    }

    private ResultActions performCreateTodoRequest(String requestBody) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post("/api/todolist/" + TODOLIST_ID + "/todo")
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions performGetTodoRequest() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(ROUTE + "?id=" + TODO_ID)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
