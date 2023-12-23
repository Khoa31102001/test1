package tech.dut.fasto.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.fasto.common.domain.UserInformation;
import tech.dut.fasto.web.rest.user.information.dto.response.UserProfileDtoResponse;


@SuppressWarnings("unused")
@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
    @Query(value = "select new tech.dut.fasto.web.rest.user.information.dto.response.UserProfileDtoResponse(u.id,uf.id, uf.firstName, uf.lastName, uf.gender, uf.birthday, uf.userImage ) from User as u inner join" +
            " UserInformation as uf on u.id = uf.user.id where u.id = :userId")
    UserProfileDtoResponse getProfileUser(@Param("userId") Long userId);
}