package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.input.VentaInputDTO;
import _DAM.Cine_V2.dto.output.VentaOutputDTO;
import _DAM.Cine_V2.modelo.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { EntradaMapper.class })
public interface VentaMapper {

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "entradas", source = "entradas")
    VentaOutputDTO toOutputDTO(Venta venta);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "entradas", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "importeTotal", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Venta toEntity(VentaInputDTO ventaInputDTO);
}
