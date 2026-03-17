package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.CityMapper;
import br.ifrn.edu.sisconf.mapper.CountryStateMapper;
import br.ifrn.edu.sisconf.repository.CountryStateRepository;
import br.ifrn.edu.sisconf.util.CityTestUtil;
import br.ifrn.edu.sisconf.util.CountryStateTestUtil;

public class CountryStateServiceTest {
    @Mock
    private CountryStateMapper countryStateMapper;

    @Mock
    private CityMapper cityMapper;

    @Mock
    private CountryStateRepository countryStateRepository;

    @InjectMocks
    private CountryStateService countryStateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllShouldReturnListOfCountryStates() {
        var countryState = new CountryState("Pernambuco", "PE", null);
        var countryStateList = new ArrayList<CountryState>(Arrays.asList(countryState));
        var expectedDTOList = CountryStateTestUtil.toDTOList(countryStateList);
        countryState.setId(1L);
        when(countryStateRepository.findAll()).thenReturn(
            countryStateList
        );
        when(countryStateMapper.toDTOList(countryStateList)).thenReturn(
            expectedDTOList
        );
        
        var actualDTOList = countryStateService.getAll();
        assertEquals(expectedDTOList, actualDTOList);
        assertEquals(expectedDTOList.size(), actualDTOList.size());
    }

    @Test
    public void getAllCitiesByCountryStateIdInvalidWhenCountryStateDoesNotExist() {
        assertThrows(
            ResourceNotFoundException.class,
            () -> countryStateService.getAllCitiesByCountryStateId(-1L)
        );
    }

    @Test
    public void getAllCitiesByCountryStateIdShouldReturnAllCities() {
        var countryState = new CountryState(
            "Pernambuco",
            "PE",
            null
        );
        countryState.setId(1L);
        var cities = new ArrayList<City>(
            Arrays.asList(
                new City("Cidade 1", countryState),
                new City("Cidade 2", countryState)
            )
        );
        countryState.setCities(cities);
        var expectedDTOList = CityTestUtil.toDTOList(countryState.getCities());
        when(countryStateRepository.findById(countryState.getId())).thenReturn(
            Optional.of(countryState)
        );
        when(cityMapper.toDTOList(countryState.getCities())).thenReturn(
            expectedDTOList
        );

        var actualDTOList = countryStateService.getAllCitiesByCountryStateId(
            countryState.getId()
        );

        assertEquals(expectedDTOList, actualDTOList);
        assertEquals(expectedDTOList.size(), actualDTOList.size());
    }
}
