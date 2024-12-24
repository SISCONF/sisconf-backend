package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurResponseDTO;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.EntrepreneurMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrepreneurService {
    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    private EntrepreneurMapper entrepreneurMapper;

    @Autowired
    private PersonService personService;

    public EntrepreneurResponseDTO getById(Long id) {
        Entrepreneur entrepreneur = entrepreneurRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Empreendedor com id %d não existe", id)
                ));
        return entrepreneurMapper.toResponseDTO(entrepreneur);
    }

    public List<EntrepreneurResponseDTO> getAll() {
        return entrepreneurMapper.toDTOList(entrepreneurRepository.findAll());
    }
}
