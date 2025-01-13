package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.domain.dtos.CityResponseDTO;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.CityMapper;
import br.ifrn.edu.sisconf.repository.CityRepository;

public class CityServiceTest {
    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExistingCityById() {
        CountryState countryState = new CountryState("Pernambuco", "PE");
        City city = new City("Petrolina", countryState);
        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));
        when(cityMapper.toResponse(city)).thenReturn(new CityResponseDTO(
            city.getId(),
            city.getName(),
            city.getCountryState().getId()
        ));

        CityResponseDTO retrievedCity = cityService.getById(city.getId());
        assertEquals(retrievedCity, cityMapper.toResponse(city));
        assertNotNull(retrievedCity);
    }

    @Test
    void getExistingCityByInvalidId() {
        when(cityRepository.findById(-1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> cityService.getById(-1L)
        );
        assertEquals("Cidade com id -1 não encontrada", exception.getMessage());
    }
}
