package com.example.mat.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.mat.entity.constant.MemberRole;
import com.example.mat.entity.shin.Member;

import jakarta.transaction.Transactional;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Member member = Member.builder()
                    .userid("userid" + i)
                    .nickname("nickname" + i)
                    .password(passwordEncoder.encode("1111"))
                    .username("username" + i)
                    .address("경기도 고양시 일산동구 oo 아파트" + i + "번지")
                    .role(MemberRole.MEMBER)
                    .build();
            memberRepository.save(member);
        });
    }
}
