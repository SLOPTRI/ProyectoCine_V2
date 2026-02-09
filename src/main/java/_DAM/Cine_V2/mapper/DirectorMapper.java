package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.input.DirectorInputDTO;
import _DAM.Cine_V2.dto.output.DirectorOutputDTO;
import _DAM.Cine_V2.modelo.Director;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DirectorMapper {
    DirectorOutputDTO toOutputDTO(Director director);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peliculas", ignore = true)
    Director toEntity(DirectorInputDTO directorInputDTO);
}
