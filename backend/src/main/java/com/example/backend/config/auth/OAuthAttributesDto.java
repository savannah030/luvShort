package com.example.backend.config.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributesDto {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String memberName;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributesDto(Map<String, Object> attributes, String nameAttributeKey, String memberName, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.memberName = memberName;
        this.email = email;
        this.picture = picture;
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
                .picture( (String) attributes.get("picture"))
                .build();
    }

}
