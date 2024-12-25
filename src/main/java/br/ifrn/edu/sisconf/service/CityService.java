package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.dtos.CityResponseDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.mapper.CityMapper;
import br.ifrn.edu.sisconf.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    public CityResponseDTO getById(Long id) {
        var city = cityRepository.findById(id).orElseThrow(() -> new BusinessException(String.format("Cidade com id %d não encontrada", id)));
        return cityMapper.toResponse(city);
    }
}
