package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // JpaRepository<Member, Long> => JpaRepository<엔티티타입, 엔티티의 PK타입>

    // JPA에 만들어져 있지 않은 나만의 조회기능을 만드려면?
    // List<Member> findByUsername(String username); // findByUsername은은 존재지 않지만 스프링데이터가 만들어줘서 작동함...신기...

    // findByUsernameAndAgeGreaterThan => 필드명과 맞춰줘야함..!!
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername") // 스프링데이터가 알아서 Member.findByUsername에 연결해줌.. // @Query(name = "Member.findByUsername") 주석처리해도 작동함
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age") // JPQL을 repository에 직접 사용하는 방법(막강함... 실무에서 많이 사용함)
    List<Member> findUser(@Param("username") String username, @Param("age") int age); // JPQL에서 오타나 문법오류가 나타나도 compile과정에서 알 수 있음!!(정적쿼리) => 동적쿼리는? => queryDsl

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO를 조회하는 방법 => 반드시 new operation을 작성한다!!! 마치 생성자로 MemberDto()에 매칭!!
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // collection을 in으로 조회할때 사용하는 방법
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional
}
