package com.project.Library.Management.member;

import com.project.Library.Management.exception.ResourceNotFoundException;
import com.project.Library.Management.member.dto.MemberDto;
import com.project.Library.Management.member.entity.Member;
import com.project.Library.Management.member.repository.MemberRepository;
import com.project.Library.Management.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member1;
    private Member member2;
    private UUID memberId;
    private MemberDto memberDTO;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();

        member1 = new Member("John Doe");
        member1.setMemberId(memberId);

        member2 = new Member("Jane Smith");
        member2.setMemberId(UUID.randomUUID());

        memberDTO = new MemberDto();
        memberDTO.setName("New Member");
    }

    @Test
    void getAllMembers_ShouldReturnAllMembers() {
        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2));

        List<Member> members = memberService.getAllMembers();

        assertEquals(2, members.size());
        assertEquals("John Doe", members.get(0).getName());
        assertEquals("Jane Smith", members.get(1).getName());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberById_WithValidId_ShouldReturnMember() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));

        Member member = memberService.getMemberById(memberId);

        assertNotNull(member);
        assertEquals(memberId, member.getMemberId());
        assertEquals("John Doe", member.getName());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void getMemberById_WithInvalidId_ShouldThrowException() {
        UUID invalidId = UUID.randomUUID();
        when(memberRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            memberService.getMemberById(invalidId);
        });
        verify(memberRepository, times(1)).findById(invalidId);
    }

    @Test
    void addMember_ShouldSaveAndReturnMember() {
        Member newMember = new Member(memberDTO.getName());
        when(memberRepository.save(any(Member.class))).thenReturn(newMember);

        Member savedMember = memberService.addMember(memberDTO);

        assertNotNull(savedMember);
        assertEquals("New Member", savedMember.getName());
        assertEquals(0, savedMember.getBorrowedBooks().size());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void updateMember_WithValidId_ShouldUpdateAndReturnMember() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member updatedMember = memberService.updateMember(memberId, memberDTO);

        assertNotNull(updatedMember);
        assertEquals(memberId, updatedMember.getMemberId());
        assertEquals("New Member", updatedMember.getName());
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void deleteMember_WithValidId_ShouldDeleteMember() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));
        doNothing().when(memberRepository).delete(member1);

        memberService.deleteMember(memberId);

        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).delete(member1);
    }
}