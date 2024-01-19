package com.org.rjankowski.ms.payment.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.rjankowski.ms.payment.CustomerApiClient;
import com.org.rjankowski.ms.payment.data.Customer;
import com.org.rjankowski.ms.payment.data.SaleRequest;
import com.org.rjankowski.ms.payment.service.SaleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SaleResourceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private SaleService saleService;

    @MockBean
    private CustomerApiClient customerApiClient;

    @Test(expected = Exception.class)
    public void whenCustomerDoesNotExist_ThenThrowException() throws Exception {
        Mockito.when(customerApiClient.getCustomer(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        SaleRequest saleRequest = new SaleRequest(1L, 1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String saleRequestString = objectMapper.writeValueAsString(saleRequest);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saleRequestString));
    }

    @Test(expected = Exception.class)
    public void whenCartDoesNotExist_ThenThrowException() throws Exception {
        Mockito.when(customerApiClient.getCustomer(Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(
                        new Customer(1L, "Jan", "Kowalski", Boolean.TRUE, Collections.emptyList()),
                        HttpStatus.NOT_FOUND));

        SaleRequest saleRequest = new SaleRequest(1L, 4L);
        ObjectMapper objectMapper = new ObjectMapper();
        String saleRequestString = objectMapper.writeValueAsString(saleRequest);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/sale")
                .contentType(MediaType.APPLICATION_JSON)
                .content(saleRequestString));
    }

    @Test
    public void whenCustomerAndCartExist_ThenProcess() throws Exception {
        Mockito.when(customerApiClient.getCustomer(Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(
                        new Customer(1L, "Jan", "Kowalski", Boolean.TRUE, Collections.emptyList()),
                        HttpStatus.NOT_FOUND));

        SaleRequest saleRequest = new SaleRequest(1L, 1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String saleRequestString = objectMapper.writeValueAsString(saleRequest);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saleRequestString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
