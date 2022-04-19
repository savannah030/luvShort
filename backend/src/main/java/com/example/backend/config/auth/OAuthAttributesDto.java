package com.example.backend.config.auth;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributesDto {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String memberName;
    private String email;

    @Builder
    public OAuthAttributesDto(Map<String, Object> attributes, String nameAttributeKey, String memberName, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.memberName = memberName;
        this.email = email;
    }

    /**
     * @param registerId            인증 서버 id (TODO 나중에 구글 말고 다른 인증 서버 사용할 때 더 구현할 예정)
     * @param nameAttributeKey      로그인 시 사용할 키
     * @param attributes            OAuth2User 속성값 저장
     * @return                      구글 ...
     */
    public static OAuthAttributesDto of(String registerId, String nameAttributeKey, Map<String, Object> attributes) {
        return ofGoogle(nameAttributeKey, attributes);
    }

    public static OAuthAttributesDto ofGoogle(String nameAttributeKey, Map<String, Object> attributes){
        return OAuthAttributesDto.builder()
                .attributes(attributes) // 속성값 전체(Map 컬렉션에 저장)
                .nameAttributeKey(nameAttributeKey)
                .memberName( (String) attributes.get("name"))
                .email( (String) attributes.get("email"))
                .build();
    }

    // 해당 이메일로 가입되지 않은 경우 User 엔티티 새로 생성
    public User toEntity(){
        return User.builder()
                .nickname(memberName)
                .email(email)
                .roleType(RoleType.NORMAL)
                .build();
    }

}
