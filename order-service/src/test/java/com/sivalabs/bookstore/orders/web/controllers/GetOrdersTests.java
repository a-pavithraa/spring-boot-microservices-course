package com.sivalabs.bookstore.orders.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.bookstore.orders.AbstractIT;
import com.sivalabs.bookstore.orders.WithCognitoMockUser;
import org.junit.jupiter.api.Test;

class GetOrdersTests extends AbstractIT {

    @Test
    @WithCognitoMockUser(username = "user")
    void shouldGetOrdersSuccessfully() throws Exception {
        mockMvc.perform(get("/api/orders")).andExpect(status().isOk());
    }
}
