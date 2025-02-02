package br.ifrn.edu.sisconf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sisconf.domain.dtos.CountryStateResponseDTO;
import br.ifrn.edu.sisconf.mapper.CountryStateMapper;
import br.ifrn.edu.sisconf.repository.CountryStateRepository;

@Service
public class CountryStateService {
    @Autowired
    private CountryStateMapper countryStateMapper;

    @Autowired
    private CountryStateRepository countryStateRepository;

    public List<CountryStateResponseDTO> getAll() {
        var countryStates = countryStateRepository.findAll();
        return countryStateMapper.toDTOList(countryStates);
    }
}
