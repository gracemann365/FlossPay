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

    @Test
    void testBusinessLogicException() throws Exception {
        System.out.println("===> Test start: testBusinessLogicException");

        MvcResult result = mockMvc.perform(post("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"senderUpi\":\"alice@upi\",\"receiverUpi\":\"alice@upi\",\"amount\":100.0}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println("Response body: " + result.getResponse().getContentAsString());
        System.out.println("===> Test end: testBusinessLogicException");
    }

    @Test
    void testValidationException() throws Exception {
        System.out.println("===> Running testValidationException...");

        MvcResult result = mockMvc.perform(post("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"senderUpi\":\"badupi\",\"receiverUpi\":\"bob@upi\",\"amount\":100.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senderUpi").value("Invalid UPI ID format"))
                .andReturn();

        // Print the actual JSON response body from the test
        System.out.println("Response body: " + result.getResponse().getContentAsString());
        System.out.println("===> testValidationException completed");
    }

}
