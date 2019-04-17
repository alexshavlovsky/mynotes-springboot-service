package com.ctzn.springangularsandbox.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class RestTestUtil {

    private RestTestUtil() {
        throw new AssertionError();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void mockGetRequest(MockMvc mockMvc, String path, Long id, ResultMatcher status, Object expected) throws Exception {
        String fullPath = id == null ? path : path + id;
        MockHttpServletRequestBuilder requestBuilder = get(fullPath)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        if (expected == null)
            mockMvc.perform(requestBuilder).andExpect(status);
        else
            mockMvc.perform(requestBuilder).andExpect(status).andExpect(content().json(asJsonString(expected)));
    }

    static void mockPostRequest(MockMvc mockMvc, String path, Object DTO, ResultMatcher status, Object expected) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(path)
                .content(asJsonString(DTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        if (expected == null)
            mockMvc.perform(requestBuilder).andExpect(status);
        else
            mockMvc.perform(requestBuilder).andExpect(status).andExpect(content().json(asJsonString(expected)));
    }

    static void mockPutRequest(MockMvc mockMvc, String path, Long id, Object DTO, ResultMatcher status, Object expected) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = put(path + id)
                .content(asJsonString(DTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        if (expected == null)
            mockMvc.perform(requestBuilder).andExpect(status);
        else
            mockMvc.perform(requestBuilder).andExpect(status).andExpect(content().json(asJsonString(expected)));
    }

    static void mockDeleteRequest(MockMvc mockMvc, String path, Long id, ResultMatcher status) throws Exception {
        mockMvc.perform(delete(path + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status);
    }

}
