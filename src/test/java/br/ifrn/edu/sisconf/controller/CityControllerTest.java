package br.ifrn.edu.sisconf.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.repository.CityRepository;
import br.ifrn.edu.sisconf.repository.CountryStateRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CityControllerTest {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryStateRepository countryStateRepository;
    
    @Autowired
    private MockMvc mockMvc;

    private CountryState countryState;
    private City city;

    @BeforeEach
    public void setUp() {
        // Needed because Flyway migrations which creates cities and country states are ran during tests
        cityRepository.deleteAll();
        countryStateRepository.deleteAll();

        countryState = new CountryState("Estado Teste", "TE", null);
        countryState = countryStateRepository.save(countryState);

        city = new City("Cidade Teste", countryState);
        city = cityRepository.save(city);
    }

    @AfterEach
    public void tearDown() {
        cityRepository.deleteAll();
        countryStateRepository.deleteAll();
    }

    @Test
    public void shouldReturnNotFoundWhenInvalidCityId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cities/{id}", -1L)).andExpect(
            MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void shouldReturnCityWhenValidCityId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/cities/%d", city.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
