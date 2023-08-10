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
    public void testCreatingATodoListWithValidBodyRespondsWithOk() throws Exception {
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
    public void testCreatingATodoListWithInvalidBodyRespondsWithBadRequest() throws Exception {
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
    public void testGettingAllTodoListsRespondsWithOk() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(ROUTE + "/all")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testGettingTodoListByIdRespondsWithOk() throws Exception {
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
    public void testGettingTodoListByIdWithNoQueryParamRespondsWithBadRequest() throws Exception {
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
    public void testUpdateTodoListWithValidRequestReturnsOk() throws Exception {
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
    public void testUpdateTodoListWithInvalidRequestReturnsBadRequest() throws Exception {
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
    public void testDeleteTodoListWithValidIdReturnsOk() throws Exception {
        mockDeleteTodoList();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete(ROUTE + "/" + TODOLIST_ID)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andDo(print()).andExpect(status().isOk());
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
