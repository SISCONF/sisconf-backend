package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurUpdateRequestDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntrepreneurMapper {
    @Mapping(source = "person.address.city", target = "person.address.city.id")
    @Mapping(target = "stock", ignore = true)
    Entrepreneur toEntity(EntrepreneurCreateRequestDTO entrepreneurCreateRequestDTO);

    @Mapping(source = "person.address.city.id", target = "person.address.city")
    EntrepreneurResponseDTO toResponseDTO(Entrepreneur entrepreneur);

    List<EntrepreneurResponseDTO> toDTOList(List<Entrepreneur> entrepreneurs);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(source = "person.address.city", target = "person.address.city.id")
    void updateEntityFromDTO(EntrepreneurUpdateRequestDTO entrepreneurUpdateRequestDTO, @MappingTarget Entrepreneur entrepreneur);
}
