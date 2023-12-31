package com.pickleddict.springtodolistbackend.web.controllers;

import com.pickleddict.springtodolistbackend.controllers.TodoListController;
import com.pickleddict.springtodolistbackend.dto.TodoListDto;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import com.pickleddict.springtodolistbackend.models.TodoList;
import com.pickleddict.springtodolistbackend.services.TodoListService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TodoListController.class)
public class TodoListControllerTests extends AbstractMvcTest {
    private static final String ROUTE = "/api/todolist";

    private static final TodoListDto VALID_TODO_LIST = new TodoListDto("some title");

    private static final TodoListDto INVALID_TODO_LIST = new TodoListDto();

    private static final Long TODOLIST_ID = 111L;

    @MockBean
    TodoListService todoListService;

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void CreateTodoList_ValidBodyValidUser_OkResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(VALID_TODO_LIST);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(ROUTE)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void CreateTodoList_InvalidBodyValidUser_BadRequestResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(INVALID_TODO_LIST);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(ROUTE)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void CreateTodoList_ValidBodyInvalidUser_OkResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(VALID_TODO_LIST);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(ROUTE)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void GetAllTodoLists_ValidUser_OkResponse() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(ROUTE + "/all")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void GetAllTodoLists_InvalidUser_UnauthorizedResponse() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(ROUTE + "/all")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void GetTodoListById_ValidIdAndUser_OkResponse() throws Exception {
        getTodoByIdMock();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(ROUTE + "?id=111")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void GetTodoListById_InvalidIdValidUser_BadRequestResponse() throws Exception {
        getTodoByIdMock();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(ROUTE)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void GetTodoListById_ValidIdInvalidUser_UnauthorizedResponse() throws Exception {
        getTodoByIdMock();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(ROUTE + "?id=111")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void UpdateTodoList_ValidRequestAndUser_OkResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new TodoListDto("updated todo"));
        mockUpdateTodoList();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(ROUTE + "/" + TODOLIST_ID)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void UpdateTodoList_InvalidRequestValidUser_BadRequestResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(INVALID_TODO_LIST);
        mockUpdateTodoList();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(ROUTE + "/" + TODOLIST_ID)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void UpdateTodoList_ValidRequestInvalidUser_UnauthorizedResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new TodoListDto("updated todo"));
        mockUpdateTodoList();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .put(ROUTE + "/" + TODOLIST_ID)
                .content(requestBody)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    public void DeleteTodoList_ValidIdAndUser_OkResponse() throws Exception {
        mockDeleteTodoList();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete(ROUTE + "/" + TODOLIST_ID)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void DeleteTodoList_ValidIdInvalidUser_UnauthorizedResponse() throws Exception {
        mockDeleteTodoList();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete(ROUTE + "/" + TODOLIST_ID)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isUnauthorized());
    }

    private void getTodoByIdMock() {
        when(todoListService.getTodoListById(any(Long.class))).thenReturn(
                ResponseEntity.ok(new TodoList("some title"))
        ).thenThrow(HttpClientErrorException.BadRequest.class);
    }

    private void mockUpdateTodoList() {
        when(todoListService.updateTodoList(TODOLIST_ID, VALID_TODO_LIST))
                .thenReturn(ResponseEntity.ok(new TodoList()))
                .thenThrow(HttpClientErrorException.BadRequest.class);
    }

    private void mockDeleteTodoList() {
        when(todoListService.deleteTodoList(TODOLIST_ID))
                .thenReturn(ResponseEntity.ok(new MessageResponse("ok")))
                .thenThrow(HttpClientErrorException.BadRequest.class);
    }
}
