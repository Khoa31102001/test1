package tech.dut.fasto.common.mapper;

import org.mapstruct.Mapper;
import tech.dut.fasto.common.domain.Location;
import tech.dut.fasto.common.dto.LocationDto;

@Mapper(componentModel = "spring")
public interface UserInformationMapper extends DtoMapper<LocationDto, Location> {
}
