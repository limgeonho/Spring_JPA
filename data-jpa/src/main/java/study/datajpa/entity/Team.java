package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id", "name"}) // 연관관계 필드는 toString 사용 하지 않기
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // FK가 없는 쪽에서 mappedBy작성, spring에서는 양쪽 모두 작성해준다
    private List<Member> members = new ArrayList<>();

    public Team(String name){
        this.name = name;
    }
}

