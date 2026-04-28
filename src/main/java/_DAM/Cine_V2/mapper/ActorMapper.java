package _DAM.Cine_V2.mapper;

import _DAM.Cine_V2.dto.actor.ActorInputDTO;
import _DAM.Cine_V2.dto.actor.ActorOutputDTO;
import _DAM.Cine_V2.modelo.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActorMapper {
    ActorOutputDTO toDTO(Actor actor);

    Actor toEntity(ActorInputDTO actorInputDTO);

    @Mapping(target = "id", ignore = true)
    void update(ActorInputDTO actorInputDTO, @MappingTarget Actor actor);
}
