package com.dife.api.repository;

import com.dife.api.model.Member;
import com.dife.api.model.MemberBlock;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockMemberRepository extends JpaRepository<MemberBlock, Long> {

	Optional<MemberBlock> findByMemberAndBlacklistedMember(Member member, Member blacklistedMember);
}
