package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

// 사용자 정의 커스텀 Repo, querydsl을 사용할때 주로 사용함
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
