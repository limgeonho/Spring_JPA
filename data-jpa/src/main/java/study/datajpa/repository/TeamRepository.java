package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

// @Repository // @Repository 생략가능
public interface TeamRepository extends JpaRepository<Team, Long> {
}
