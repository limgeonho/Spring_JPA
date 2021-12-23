package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom { // 내가 직접 만든 repo MemberRepositoryCustom상속
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

//    @Query(value = "select m from Member m left join m.team t",
//        countQuery = "select count(m) from Member m"
//    )
    Page<Member> findByAge(int age, Pageable pageable); // countQuery는 따로 작성할 수 있음(최적화 가능)

    @Modifying(clearAutomatically = true)// clearAutomatically = true : 영속성 컨텍스트를 clear해주는 것을 알아서 해주는 기능 //수정하는 Query에서는 반드시 @Modifying사용하자!!
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team") // join fetch을 사용해서 Member을 조회할 때 team에 관련된 사항을 Proxy가 아닌 바로 Team을 함께 한번에 조회한다.
    List<Member> findMemberFetchJoin();

    //위의 내용을 JPQL을 직접 작성하지 않고 spring data Jpa에서 사용하는 방법!! => findAll()을 오버라이드한다
    @Override
    @EntityGraph(attributePaths = {"team"}) // @EntityGraph(attributePaths = {"team"})을 하면 fetch join을 사용한 것처럼 Member을 조회할 때 team도 함께 조회해온다.
    List<Member> findAll();

    // JPQL하고 EntityGraph를 함께 사용가능
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
