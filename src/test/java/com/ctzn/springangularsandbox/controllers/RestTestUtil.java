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

    static void mockGetRequest(MockMvc mockMvc, String path, Long id, ResultMatcher status, Object expected) throws Exception {
        performWithStatusAndExpected(mockMvc,
                setMediaTypes(get(joinPath(path, id))),
                status, expected);
    }

    static void mockPostRequest(MockMvc mockMvc, String path, Object DTO, ResultMatcher status, Object expected) throws Exception {
        performWithStatusAndExpected(mockMvc,
                setMediaTypes(post(path).content(asJsonString(DTO))),
                status, expected);
    }

    static void mockPutRequest(MockMvc mockMvc, String path, Long id, Object DTO, ResultMatcher status, Object expected) throws Exception {
        performWithStatusAndExpected(mockMvc,
                setMediaTypes(put(joinPath(path, id)).content(asJsonString(DTO))),
                status, expected);
    }

    static void mockDeleteRequest(MockMvc mockMvc, String path, Long id, ResultMatcher status) throws Exception {
        performWithStatusAndExpected(mockMvc,
                setMediaTypes(put(joinPath(path, id))),
                status, null);

        mockMvc.perform(setMediaTypes(delete(joinPath(path, id)))).andExpect(status);
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

    private static MockHttpServletRequestBuilder setMediaTypes(MockHttpServletRequestBuilder builder) {
        return builder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    }

    private static void performWithStatusAndExpected(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder, ResultMatcher status, Object expected) throws Exception {
        if (expected == null)
            mockMvc.perform(requestBuilder).andExpect(status);
        else
            mockMvc.perform(requestBuilder).andExpect(status).andExpect(content().json(asJsonString(expected)));
    }

}
