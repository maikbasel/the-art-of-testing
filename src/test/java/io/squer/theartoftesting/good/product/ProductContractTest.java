package io.squer.theartoftesting.good.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.exporters.inmemory.InMemorySpanExporter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ProductContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void shouldCreateNewProduct() throws Exception {
        var payload = new ProductRequest("Baz", 150.5);

        var result = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn();
        var location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Baz"))
                .andExpect(jsonPath("$.basePrice").value(150.5));
    }
}
