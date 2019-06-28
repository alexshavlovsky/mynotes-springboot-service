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

    static void mockGetRequest(MockMvc mockMvc, String path, ResultMatcher status, Object expected) throws Exception {
        perform(mockMvc, get(path), status, expected);
    }

    static void mockGetParamRequest(MockMvc mockMvc, String path, String paramName, String paramValue, ResultMatcher status, Object expected) throws Exception {
        perform(mockMvc, get(path).param(paramName, paramValue), status, expected);
    }

    static void mockPostRequest(MockMvc mockMvc, String path, Object request, ResultMatcher status, Object expected) throws Exception {
        perform(mockMvc, post(path).content(asJsonString(request)), status, expected);
    }

    static void mockPutRequest(MockMvc mockMvc, String path, Object request, ResultMatcher status, Object expected) throws Exception {
        perform(mockMvc, put(path).content(asJsonString(request)), status, expected);
    }

    static void mockDeleteRequest(MockMvc mockMvc, String path, ResultMatcher status, Object expected) throws Exception {
        perform(mockMvc, delete(path), status, expected);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void perform(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder, ResultMatcher status, Object expected) throws Exception {
        requestBuilder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        if (expected == null)
            mockMvc.perform(requestBuilder).andExpect(status);
        else
            mockMvc.perform(requestBuilder).andExpect(status).andExpect(content().json(asJsonString(expected)));
    }

}
