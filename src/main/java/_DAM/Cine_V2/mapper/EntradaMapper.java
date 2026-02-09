package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.input.EntradaInputDTO;
import _DAM.Cine_V2.dto.output.EntradaOutputDTO;
import _DAM.Cine_V2.modelo.Entrada;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntradaMapper {

    @Mapping(target = "funcionId", source = "funcion.id")
    @Mapping(target = "ventaId", source = "venta.id")
    EntradaOutputDTO toOutputDTO(Entrada entrada);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "funcion", ignore = true)
    @Mapping(target = "venta", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Entrada toEntity(EntradaInputDTO entradaInputDTO);
}
