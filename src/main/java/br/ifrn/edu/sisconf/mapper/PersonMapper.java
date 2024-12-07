package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {
    Person toEntity(PersonCreateRequestDTO personCreateRequestDto);
}
