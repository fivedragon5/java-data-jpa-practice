package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    void testMember() {
        // given
        Member member = new Member("fad2");

        // when
        Member savedMember = memberRepository.save(member);

        // then
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertEquals(findMember, member);
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember.getId(), member.getId());
    }
}