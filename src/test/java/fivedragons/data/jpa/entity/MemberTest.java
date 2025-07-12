package fivedragons.data.jpa.entity;

import fivedragons.data.jpa.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired MemberRepository memberRepository;

    @Test
    void testEntity() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        // when

        // then
        List<Member> members =  em.createQuery("select m from Member m join fetch m.team", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

    @Test
    void jpaEventBaseEntity() throws Exception {
        // given
        Member member = new Member("member1");
        memberRepository.save(member); // @PrePersist

        Thread.sleep(1000);
        member.setUsername("member2");

        em.flush(); // @PreUpdate
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        System.out.println("findMemberCreateDate = " + findMember.getCreateDate());
        System.out.println("findMemberLastModifiedDate = " + findMember.getLastModifiedDate());
        System.out.println("findMemberCreatedBy = " + findMember.getCreatedBy());
        System.out.println("findMemberLastModifiedBy = " + findMember.getLastModifiedBy());
    }
}