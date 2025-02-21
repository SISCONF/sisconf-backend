package br.ifrn.edu.sisconf.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifrn.edu.sisconf.SecurityTestConfig;
import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.mapper.CityMapper;
import br.ifrn.edu.sisconf.repository.CityRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
public class CityControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private MockMvc mockMvc;

    private City city;

    @BeforeEach
    public void setUp() {
        city = cityRepository.findAll().getFirst();
    }

    @Test
    public void shouldReturnNotFoundWhenInvalidCityId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/{id}", -1L)).andExpect(
            MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void shouldReturnCityWhenValidCityId() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(
            cityMapper.toResponse(city)
        );
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/cities/%d", city.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }
}
