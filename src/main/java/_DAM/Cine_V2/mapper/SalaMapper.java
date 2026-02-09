package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.input.SalaInputDTO;
import _DAM.Cine_V2.dto.output.SalaOutputDTO;
import _DAM.Cine_V2.modelo.Sala;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SalaMapper {
    SalaOutputDTO toOutputDTO(Sala sala);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "funciones", ignore = true)
    Sala toEntity(SalaInputDTO salaInputDTO);
}
