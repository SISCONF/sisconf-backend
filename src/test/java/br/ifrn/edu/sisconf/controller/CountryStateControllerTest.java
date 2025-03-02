package br.ifrn.edu.sisconf.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.mapper.CityMapper;
import br.ifrn.edu.sisconf.mapper.CountryStateMapper;
import br.ifrn.edu.sisconf.repository.CityRepository;
import br.ifrn.edu.sisconf.repository.CountryStateRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CountryStateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CountryStateRepository countryStateRepository;

    @Autowired
    private CountryStateMapper countryStateMapper;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    private CountryState countryState;
    private City firstCity;
    private City secondCity;

    @BeforeEach
    public void setUp() {
        // Needed because Flyway migrations which creates cities and country states are ran during tests
        cityRepository.deleteAll();
        countryStateRepository.deleteAll();

        countryState = new CountryState("Pernambuco", "PE", null);
        countryStateRepository.save(countryState);

        firstCity = new City("Teste 1", countryState);
        secondCity = new City("Teste 2", countryState);

        cityRepository.saveAll(new ArrayList<City>(Arrays.asList(firstCity, secondCity)));

    }

    @Test
    public void shouldReturnCountryStateList() throws Exception{
        String expectedResponse = objectMapper.writeValueAsString(
            countryStateMapper.toDTOList(List.of(countryState))
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/country-states")).andExpect(
            MockMvcResultMatchers.status().isOk()
        ).andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    public void shouldListCitiesWhenValidCityId() throws Exception {
        String expectedResponse = objectMapper.writeValueAsString(
            cityMapper.toDTOList(List.of(firstCity, secondCity))
        );

        mockMvc.perform(MockMvcRequestBuilders.get(
            "/api/country-states/{id}/cities", 
            countryState.getId()
        )).andExpect(
            MockMvcResultMatchers.status().isOk()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2))
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$").isArray()
        ).andExpect(
            MockMvcResultMatchers.content().json(expectedResponse)
        );
    }

    @Test
    public void shouldNotListCitiesWhenInvalidCityId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
            "/api/country-states/{id}/cities", 
            -1L
        )).andExpect(
            MockMvcResultMatchers.status().isNotFound()
        );
    }
}
