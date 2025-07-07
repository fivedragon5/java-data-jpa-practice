package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.entity.Member;
import org.junit.jupiter.api.Assertions;
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
    void findByUsernameAndAgeGraterThen() {
        // given
        Member member1 = new Member("fad", 10);
        Member member2 = new Member("fad", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("fad", 15);
        
        // then
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getUsername(), "fad");
        assertEquals(result.get(0).getAge(), 20);
    }
    
    @Test
    void testNamedQuery() {
        // given
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 10);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        // when
        List<Member> result = memberJpaRepository.findByUsername("AA");
        Member findMemer = result.get(0);
        
        // then
        Assertions.assertEquals(findMemer, m1);
    }

    @Test
    void findByUsername() {
        // given
        memberJpaRepository.save(new Member("AA1", 10));
        memberJpaRepository.save(new Member("AA2", 10));
        memberJpaRepository.save(new Member("AA3", 10));
        memberJpaRepository.save(new Member("AA4", 10));
        memberJpaRepository.save(new Member("AA5", 10));

        int age = 10;
        int offset = 1;
        int limit = 3;


        // when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // then
        assertEquals(members.size(), 3);
        assertEquals(totalCount, 5);
    }


}