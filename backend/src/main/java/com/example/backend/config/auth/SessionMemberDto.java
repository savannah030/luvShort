package com.example.backend.config.auth;

import com.example.backend.domain.user.User;

import java.io.Serializable;

// 엔티티를 직접 직렬화하는 것은 지양해야함. 따라서 Dto를 따로 만들어 직렬화
public class SessionMemberDto implements Serializable {
    private String memberName;
    private String email;

    public SessionMemberDto(User user){
        this.memberName = user.getNickname();
        this.email = user.getEmail();
    }
}
