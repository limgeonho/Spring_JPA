package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor // private final로 선언된 것들을 포함하는 생성자 자동 생성
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 도메인 클래스 컨버터(잘안씀..)
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @PostConstruct // spring이 실행되면서 먼저 실행
    public void init(){
        memberRepository.save(new Member("userA"));
    }

    @GetMapping("/members")
    public Page<MemberDto> list(Pageable pageable){
//        직접 페이징 시작 숫자를 1로 설정하는 방법!!!
//        PageRequest request = PageRequest.of(1,2);
//        Page<Member> page = memberRepository.findAll(request);
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return map;
    }
//    @GetMapping("/members")
//    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable){
//        Page<Member> page = memberRepository.findAll(pageable);
//        return page;
//    }
}
