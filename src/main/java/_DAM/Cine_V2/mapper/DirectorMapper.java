package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.director.DirectorInputDTO;
import _DAM.Cine_V2.dto.director.DirectorOutputDTO;
import _DAM.Cine_V2.modelo.Director;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DirectorMapper {
    DirectorOutputDTO toDTO(Director director);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peliculas", ignore = true)
    Director toEntity(DirectorInputDTO directorInputDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peliculas", ignore = true)
    void update(DirectorInputDTO directorInputDTO, @MappingTarget Director director);
}
