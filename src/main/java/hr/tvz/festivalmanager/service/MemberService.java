package hr.tvz.festivalmanager.service;

import hr.tvz.festivalmanager.entities.Member;
import hr.tvz.festivalmanager.entities.Person;
import hr.tvz.festivalmanager.repository.MemberRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servisna klasa za rad s članovima benda.
 */
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Pretražuje članove prema ulozi.
     *
     * @param role uloga člana
     * @return lista članova
     */
    public List<Member> findByRole(Member.Role role) {
        return memberRepository.getAllEntities().stream()
                .filter(member -> member.getRole() == role)
                .toList();
    }

    /**
     * Grupira članove prema ulozi.
     *
     * @return mapa članova po ulozi
     */
    public Map<Member.Role, List<Member>> groupByRole() {
        return memberRepository.getAllEntities().stream()
                .collect(Collectors.groupingBy(Member::getRole));
    }

    /**
     * Broji članove prema ulozi.
     *
     * @return mapa broja članova po ulozi
     */
    public Map<Member.Role, Long> countByRole() {
        return memberRepository.getAllEntities().stream()
                .collect(Collectors.groupingBy(Member::getRole, Collectors.counting()));
    }

    /**
     * Sortira članove prema datumu rođenja.
     *
     * @return lista članova
     */
    public List<Member> sortByDateOfBirth() {
        return memberRepository.getAllEntities().stream()
                .sorted(Comparator.comparing(Person::getDateOfBirth))
                .toList();
    }
}
