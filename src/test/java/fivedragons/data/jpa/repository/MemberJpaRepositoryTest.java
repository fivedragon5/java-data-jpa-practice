package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        // given
        Member member = new Member("fad");

        // when
        Member savedMember = memberJpaRepository.save(member);

        // then
        Member foundMember = memberJpaRepository.find(savedMember.getId());
        assertEquals(foundMember, member);
        assertEquals(foundMember.getUsername(), member.getUsername());
        assertEquals(foundMember.getId(), member.getId());
    }
}