package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.dto.MemberDto;
import fivedragons.data.jpa.entity.Member;
import fivedragons.data.jpa.entity.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

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

    @Test
    void findByUsernameAndAgeGraterThen() {
        // given
        Member member1 = new Member("fad", 10);
        Member member2 = new Member("fad", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("fad", 15);

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
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<Member> result = memberRepository.findByUsername("AA");
        Member findMemer = result.get(0);

        // then
        Assertions.assertEquals(findMemer, m1);
    }

    @Test
    void findUser() {
        // given
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<Member> result = memberRepository.findUser("AA", 10);
        Member findMemer = result.get(0);

        // then
        Assertions.assertEquals(m1, findMemer);
    }

    @Test
    void findUsernameList() {
        // given
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<String> result = memberRepository.findUsernameList();

        // then
        Assertions.assertEquals(result.size(), 2);
    }

    @Test
    void findMemberDto() {
        // given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AA", 10, team);
        memberRepository.save(m1);

        // when
        List<MemberDto> result = memberRepository.findMemberDto();
        MemberDto memberDto = result.get(0);

        // then
        assertEquals(memberDto.getUsername(), m1.getUsername());
        assertEquals(memberDto.getId(), m1.getId());
        assertEquals(memberDto.getTeamName(), team.getName());
    }

    @Test
    void findByNames() {
        // given
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AA", "BB"));

        // then
        Assertions.assertEquals(result.size(), 2);
    }
}