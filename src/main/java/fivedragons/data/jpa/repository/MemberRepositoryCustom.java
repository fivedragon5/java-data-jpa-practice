package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
