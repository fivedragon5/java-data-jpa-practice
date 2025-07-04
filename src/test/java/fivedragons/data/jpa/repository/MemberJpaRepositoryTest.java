package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    void testMember() {
        // given
        Member member = new Member("fad");

        // when
        Member savedMember = memberJpaRepository.save(member);

        // then
        Member foundMember = memberJpaRepository.findById(savedMember.getId()).get();
        assertEquals(foundMember, member);
        assertEquals(foundMember.getUsername(), member.getUsername());
        assertEquals(foundMember.getId(), member.getId());
    }

    @Test
    void basicCRUD() {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // then
        Member foundMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member foundMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertEquals(member1, foundMember1);
        assertEquals(member2, foundMember2);

        List<Member> all = memberJpaRepository.findAll();
        assertEquals(2, all.size());

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        assertEquals(0, deletedCount);
    }

    @Test
    void basicCRUD_2() {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // then
        Member foundMember1 = memberRepository.findById(member1.getId()).get();
        Member foundMember2 = memberRepository.findById(member2.getId()).get();
        assertEquals(member1, foundMember1);
        assertEquals(member2, foundMember2);

        List<Member> all = memberRepository.findAll();
        assertEquals(2, all.size());

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertEquals(0, deletedCount);
    }
}