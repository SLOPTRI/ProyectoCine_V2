package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.funcion.FuncionInputDTO;
import _DAM.Cine_V2.dto.funcion.FuncionOutputDTO;
import _DAM.Cine_V2.modelo.Funcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FuncionMapper {

    @Mapping(target = "peliculaId", source = "pelicula.id")
    @Mapping(target = "salaId", source = "sala.id")
    FuncionOutputDTO toDTO(Funcion funcion);

    @Mapping(target = "pelicula", ignore = true)
    @Mapping(target = "sala", ignore = true)
    @Mapping(target = "entradas", ignore = true)
    @Mapping(target = "id", ignore = true)
    Funcion toEntity(FuncionInputDTO funcionInputDTO);

    @Mapping(target = "pelicula", ignore = true)
    @Mapping(target = "sala", ignore = true)
    @Mapping(target = "entradas", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(FuncionInputDTO funcionInputDTO, @MappingTarget Funcion funcion);
}
