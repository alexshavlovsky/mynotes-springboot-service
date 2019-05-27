package com.ctzn.mynotesservice.controllers;

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

    static void mockGetRequest(MockMvc mockMvc, String path, Long id, ResultMatcher status, Object expected) throws Exception {
        perform(mockMvc, get(joinPath(path, id)), status, expected);
    }

    static void mockPostRequest(MockMvc mockMvc, String path, Object request, ResultMatcher status, Object expected) throws Exception {
        perform(mockMvc, post(path).content(asJsonString(request)), status, expected);
    }

    static void mockPutRequest(MockMvc mockMvc, String path, Long id, Object request, ResultMatcher status, Object expected) throws Exception {
        perform(mockMvc, put(joinPath(path, id)).content(asJsonString(request)), status, expected);
    }

    static void mockDeleteRequest(MockMvc mockMvc, String path, Long id, ResultMatcher status) throws Exception {
        perform(mockMvc, delete(joinPath(path, id)), status, null);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String joinPath(String path, Long id) {
        return id == null ? path : String.join("/", path, id.toString());
    }

    private static void perform(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder, ResultMatcher status, Object expected) throws Exception {
        requestBuilder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        if (expected == null)
            mockMvc.perform(requestBuilder).andExpect(status);
        else
            mockMvc.perform(requestBuilder).andExpect(status).andExpect(content().json(asJsonString(expected)));
    }

}
