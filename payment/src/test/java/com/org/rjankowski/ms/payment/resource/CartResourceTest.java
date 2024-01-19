package com.org.rjankowski.ms.payment.resource;

import com.org.rjankowski.ms.payment.data.Cart;
import com.org.rjankowski.ms.payment.repository.CartRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CartResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CartRepository cartRepository;

    @Test
    public void testList() throws Exception {
        Mockito.when(cartRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetById() throws Exception {
        Mockito.when(cartRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Cart(1L, Collections.emptyList())));

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
