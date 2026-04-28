package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.sala.SalaInputDTO;
import _DAM.Cine_V2.dto.sala.SalaOutputDTO;
import _DAM.Cine_V2.modelo.Sala;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SalaMapper {
    SalaOutputDTO toDTO(Sala sala);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "funciones", ignore = true)
    Sala toEntity(SalaInputDTO salaInputDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "funciones", ignore = true)
    void update(SalaInputDTO salaInputDTO, @MappingTarget Sala sala);
}
