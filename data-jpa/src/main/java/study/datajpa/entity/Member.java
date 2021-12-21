package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id", "username", "age"}) // 연관관계 필드는 toString 사용 하지 않기
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) // JPA에서 XToOne관계는 무조건 LAZY로딩으로 한다.
    @JoinColumn(name = "team_id") // FK name, N쪽에서 FK를 가지고 있는다
    private Team team;

    // protected Member(){} // JPA를 사용하면 Entity에 있어야함!!! => @NoArgsConstructor(access = AccessLevel.PROTECTED)로 대체
    public Member(String username){
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null){
            changeTeam(team);
        }
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this); // 반대쪽에서도 넣어준다.
    }
}
