package com.example.backend.config.auth;

import com.example.backend.domain.user.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

// NOTE: OAuth2UserService를 구현한 CustomOAuth2UserService 클래스는
//  구글 로그인 이후 가져온 사용자의 정보(이름, email주소, 사진)들을 기반으로
//  가입 및 정보수정, 세션 저장 등의 기능을 지원
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        /**
         * NOTE: 현재 로그인 진행 중인 서비스를 구분하는 코드
         *  구글, 네이버, 카카오 등 인증 서버 구분
         *  1. registrationId : 인증 서버 id (userRequest.clientRegistration.registrationId)
         *  2. userNameAttributeKey : OAuth2 로그인 진행시 사용할 키 (userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName)
         *      cf) 구글의 기본코드는 "sub"
         *  3. OAuthAttributesDto : OAuth2User의 속성값 담을 클래스
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 1.
        String userNameAttributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //2.
        OAuthAttributesDto attributes = OAuthAttributesDto.of(registrationId, userNameAttributeKey, oAuth2User.getAttributes()); //3.

        // 속성값 가지고 User 엔티티 업데이트
        User user = saveOrUpdate(attributes);
        return null;
    }

    private User saveOrUpdate(OAuthAttributesDto attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                // 이미 가입한 사용자이면(해당 이메일을 쓰는 사용자가 있으면) 엔티티 업데이트
                .map(entity -> entity.update(attributes.getMemberName()))
                // 그렇지 않으면 속성값으로 새로운 엔티티 생성
                .orElse(attributes.toEntity());

        return userRepository.save(user);

    }

}
