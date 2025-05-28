package io.squer.theartoftesting.good.product;

import io.opentelemetry.exporters.inmemory.InMemorySpanExporter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ProductContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemorySpanExporter exporter;

    @Test
    void shouldRespondWithProductForKnownProductId() throws Exception {
        mockMvc.perform(get("/products/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void shouldRespondWithNotFoundForUnknownProductId() throws Exception {
        mockMvc.perform(get("/products/{id}", "99"))
                .andExpect(status().isNotFound());
    }
}
