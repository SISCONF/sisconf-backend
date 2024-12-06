package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonMapper {
    Person toEntity(PersonCreateRequestDTO personCreateRequestDto);
}
