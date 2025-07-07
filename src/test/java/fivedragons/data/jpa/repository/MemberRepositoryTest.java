package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.dto.MemberDto;
import fivedragons.data.jpa.entity.Member;
import fivedragons.data.jpa.entity.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Test
    void returnType() {
        // given
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<Member> result = memberRepository.findListByUsername("!@#$!@#");
        Member result2 = memberRepository.findMemberByUsername("!@#@!#");
        Optional<Member> result3 = memberRepository.findOptionalByUsername("!@#@!#@!#");

        // then
        Assertions.assertEquals(result, List.of()); // 반환 타입이 컬렉션 이고 값이 없을 경우, empty Collection 반환
        Assertions.assertEquals(result2, null); // 단건 조회일 경우 null 반환
        Assertions.assertEquals(result3, Optional.empty()); // Optional 타입일 경우 Optional.empty() 반환
    }

    @Test
    void findByAge() {
        // given
        memberRepository.save(new Member("AA1", 10));
        memberRepository.save(new Member("AA2", 10));
        memberRepository.save(new Member("AA3", 10));
        memberRepository.save(new Member("AA4", 10));
        memberRepository.save(new Member("AA5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> result = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> toMap = result.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        // then
        assertEquals(toMap.getContent().size(), 3);
        assertEquals(toMap.getTotalElements(), 5);
        assertEquals(toMap.getNumber(), 0);
        assertEquals(toMap.getTotalPages(), 2);
        assertEquals(toMap.isFirst(), true);
        assertEquals(toMap.hasNext(), true);
        List<MemberDto> content = toMap.getContent();
        assertEquals(content.get(0).getUsername(), "AA5");
    }

    @Test
    void findSliceByAge() {
        // given
        memberRepository.save(new Member("AA1", 10));
        memberRepository.save(new Member("AA2", 10));
        memberRepository.save(new Member("AA3", 10));
        memberRepository.save(new Member("AA4", 10));
        memberRepository.save(new Member("AA5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Slice<Member> result = memberRepository.findSliceByAge(age, pageRequest);

        // then
        assertEquals(result.getContent().size(), 3);
//        assertEquals(result.getTotalElements(), 5);
        assertEquals(result.getNumber(), 0);
//        assertEquals(result.getTotalPages(), 2);
        assertEquals(result.isFirst(), true);
        assertEquals(result.hasNext(), true);
        List<Member> content = result.getContent();
        assertEquals(content.get(0).getUsername(), "AA5");
    }

    @Test
    void findListByAge() {
        // given
        memberRepository.save(new Member("AA1", 10));
        memberRepository.save(new Member("AA2", 10));
        memberRepository.save(new Member("AA3", 10));
        memberRepository.save(new Member("AA4", 10));
        memberRepository.save(new Member("AA5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        List<Member> result = memberRepository.findListByAge(age, pageRequest);

        // then
        assertEquals(result.get(0).getUsername(), "AA5");
    }
}