package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
}
