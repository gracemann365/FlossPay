package com.openpay.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test for business logic exception
    @Test
    void testBusinessLogicException() throws Exception {
        System.out.println("===> Test start: testBusinessLogicException");

        MvcResult result = mockMvc.perform(post("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Idempotency-Key", "test-key-biz-err") // you need to pass a unique key for each test
                .content("{\"senderUpi\":\"alice@upi\",\"receiverUpi\":\"alice@upi\",\"amount\":100.0}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println("Response body: " + result.getResponse().getContentAsString());
        System.out.println("===> Test end: testBusinessLogicException");
    }

    // Test for validation exception
    @Test
    void testValidationException() throws Exception {
        System.out.println("===> Running testValidationException...");

        MvcResult result = mockMvc.perform(post("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Idempotency-Key", "test-key-biz-err") // you need to pass a unique key for each test
                .content("{\"senderUpi\":\"badupi\",\"receiverUpi\":\"bob@upi\",\"amount\":100.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senderUpi").value("Invalid UPI ID format"))
                .andReturn();

        // Print the actual JSON response body from the test
        System.out.println("Response body: " + result.getResponse().getContentAsString());
        System.out.println("===> testValidationException completed");
    }

    // Test for duplicate idempotency key
    @Test
    void testDuplicateIdempotencyKey() throws Exception {
        System.out.println("===> Running testDuplicateIdempotencyKey...");

        String json = "{\"senderUpi\":\"alice@upi\",\"receiverUpi\":\"bob@upi\",\"amount\":100.0}";
        String idempotencyKey = java.util.UUID.randomUUID().toString(); // always unique!

        // First request: should succeed
        MvcResult firstResult = mockMvc.perform(post("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Idempotency-Key", idempotencyKey)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("First response body: " + firstResult.getResponse().getContentAsString());

        // Second request with same key: should fail with 400
        MvcResult secondResult = mockMvc.perform(post("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Idempotency-Key", idempotencyKey)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Duplicate request"))
                .andReturn();

        System.out.println("Second response body: " + secondResult.getResponse().getContentAsString());
        System.out.println("===> testDuplicateIdempotencyKey completed");
    }

}
