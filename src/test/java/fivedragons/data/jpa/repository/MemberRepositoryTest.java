package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.dto.MemberDto;
import fivedragons.data.jpa.entity.Member;
import fivedragons.data.jpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    @PersistenceContext EntityManager em;

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

    @Test
    void bulkAgePlus() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int result = memberRepository.bulkAgePlus(20);
        // bulk 쿼리는 영속성 컨텍스트에 반영되지 않으므로, flush()와 clear()를 호출하여 영속성 컨텍스트를 초기화해야 함
        // @Modifying 어노테이션을 사용하면 clearAutomatically = true 옵션을 추가하여 자동으로 영속성 컨텍스트를 초기화할 수 있음
        Member member5 = memberRepository.findByUsername("member5").get(0);
        System.out.println("member5 = " + member5);

        // then
        assertEquals(result, 3);
    }

    @Test
    void findMemberLazy() {
        // given
        // member1 -> teamA | member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when N + 1 문제
        // @EntityGraph를 사용하지 않으면, 프록시 객체가 생성되고, 실제 데이터를 가져오기 위해 추가 쿼리가 발생함
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member = " + member);
            // 프록시 객체를 넣어둠
            System.out.println("member.teamClass1 = " + member.getTeam().getClass());
            System.out.println("member.getTeam() = " + member.getTeam().getName());
        }

        // then
    }

    @Test
    void findMemberFetchJoin() {
        // given
        // member1 -> teamA | member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            System.out.println("member = " + member);
            // 프록시 객체를 넣어둠
            System.out.println("member.teamClass1 = " + member.getTeam().getClass());
            System.out.println("member.getTeam() = " + member.getTeam().getName());
        }

        // then
    }

    @Test
    void queryHint() {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();

        // then
    }

    @Test
    void lock() {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        List<Member> findMember = memberRepository.findLockByUsername("member1");

        // then
    }
    
    @Test
    void callCustom() {
        // given
        memberRepository.findMemberCustom();
        // when
        // then
    }

    @Test
    void specBasic() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        // then
        Assertions.assertEquals(result.size(), 1);
    }

    @Test
    void queryByExample() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Member member = new Member("m1");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);

        // then
        assertEquals(result.size(), 1);
    }

    @Test
    void projections() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        // then
        Assertions.assertEquals(result.get(0).getUsername(), m1.getUsername() + " " + m1.getAge());
    }

    @Test
    void projectionsDto() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<UsernameOnlyDto> result = memberRepository.findProjectionsDtoByUsername("m1");

        for (UsernameOnlyDto o : result) {
            System.out.println("usernameOnly = " + o.getUsername());
        }

        // then
        Assertions.assertEquals(result.get(0).getUsername(), m1.getUsername());
    }

    @Test
    void nestedClosedProjections() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<NestedClosedProjections> result = memberRepository.findNestedClosedProjectionsDtoByUsername("m1");

        for (NestedClosedProjections o : result) {
            System.out.println("userName = " + o.getUsername() + " teamName = " + o.getTeam().getName());
        }
    }

    @Test
    void nativeQuery() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Member result = memberRepository.findByNativeQuery("m1");

        // then
        Assertions.assertEquals(result.getUsername(), m1.getUsername());
    }

    @Test
    void findByNativeProjection() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        for (MemberProjection m : result) {
            System.out.println("memberProjection = " + m.getUserName() + " teamName = " + m.getTeamName());
        }

        // then
    }
}