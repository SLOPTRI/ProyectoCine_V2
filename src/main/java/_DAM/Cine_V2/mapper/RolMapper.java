package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.rol.RolInputDTO;
import _DAM.Cine_V2.dto.rol.RolOutputDTO;
import _DAM.Cine_V2.modelo.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RolMapper {
    RolOutputDTO toDTO(Rol rol);

    @Mapping(target = "id", ignore = true)
    Rol toEntity(RolInputDTO rolInputDTO);

    @Mapping(target = "id", ignore = true)
    void update(RolInputDTO rolInputDTO, @MappingTarget Rol rol);
}
