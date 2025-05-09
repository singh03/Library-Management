package com.project.Library.Management.member.service;

import com.project.Library.Management.exception.ResourceNotFoundException;
import com.project.Library.Management.member.dto.MemberDto;
import com.project.Library.Management.member.entity.Member;
import com.project.Library.Management.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
    }

    public Member addMember(MemberDto memberDTO) {
        Member member = new Member(memberDTO.getName());
        return memberRepository.save(member);
    }

    public Member updateMember(UUID id, MemberDto memberDTO) {
        Member member = getMemberById(id);
        member.setName(memberDTO.getName());
        return memberRepository.save(member);
    }

    public void deleteMember(UUID id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }
}
