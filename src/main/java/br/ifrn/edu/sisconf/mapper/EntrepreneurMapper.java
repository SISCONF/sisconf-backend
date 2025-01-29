package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurResponseDTO;

import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PersonMapper.class})
public interface EntrepreneurMapper {
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "person.id", ignore = true)
    @Mapping(target = "person.createdAt", ignore = true)
    @Mapping(target = "person.updatedAt", ignore = true)
    @Mapping(target = "person.keycloakId", ignore = true)
    @Mapping(target = "person.address.id", ignore = true)
    @Mapping(target = "person.address.createdAt", ignore = true)
    @Mapping(target = "person.address.updatedAt", ignore = true)
    @Mapping(target = "person.entrepreneur", ignore = true)
    @Mapping(target = "person.customer", ignore = true)
    @Mapping(target = "person.address.city.id", source = "person.address.city")
    Entrepreneur toEntity(EntrepreneurRequestDTO entrepreneurRequestDTO);

    @Mapping(source = "person.address.city.id", target = "person.address.city")
    EntrepreneurResponseDTO toResponseDTO(Entrepreneur entrepreneur);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "person.address.id", ignore = true)
    @Mapping(target = "person.address.createdAt", ignore = true)
    @Mapping(target = "person.address.updatedAt", ignore = true)
    @Mapping(target = "person.id", ignore = true)
    @Mapping(target = "person.createdAt", ignore = true)
    @Mapping(target = "person.updatedAt", ignore = true)
    @Mapping(target = "person.keycloakId", ignore = true)
    @Mapping(target = "person.customer", ignore = true)
    @Mapping(target = "person.entrepreneur", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(source = "person.address.city", target = "person.address.city.id")
    void updateEntityFromDTO(EntrepreneurRequestDTO entrepreneurRequestDTO, @MappingTarget Entrepreneur entrepreneur);
}