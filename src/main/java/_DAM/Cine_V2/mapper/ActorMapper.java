package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.input.ActorInputDTO;
import _DAM.Cine_V2.dto.output.ActorOutputDTO;
import _DAM.Cine_V2.modelo.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActorMapper {
    ActorOutputDTO toOutputDTO(Actor actor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peliculas", ignore = true)
    Actor toEntity(ActorInputDTO actorInputDTO);
}
