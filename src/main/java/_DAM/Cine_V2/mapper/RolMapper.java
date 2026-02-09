package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.input.RolInputDTO;
import _DAM.Cine_V2.dto.output.RolOutputDTO;
import _DAM.Cine_V2.modelo.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RolMapper {
    RolOutputDTO toOutputDTO(Rol rol);

    @Mapping(target = "id", ignore = true)
    Rol toEntity(RolInputDTO rolInputDTO);
}
