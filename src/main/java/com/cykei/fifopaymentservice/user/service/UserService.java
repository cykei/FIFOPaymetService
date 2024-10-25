package com.cykei.fifopaymentservice.user.service;

import com.cykei.fifopaymentservice.user.User;
import com.cykei.fifopaymentservice.user.UserDetailsImpl;
import com.cykei.fifopaymentservice.user.mapper.UserMapper;
import com.cykei.fifopaymentservice.user.repository.UserRepository;
import com.cykei.fifopaymentservice.user.service.dto.JwtToken;
import com.cykei.fifopaymentservice.user.service.dto.UserRequest;
import com.cykei.fifopaymentservice.user.service.dto.UserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public void signUp(UserSignUpRequest signUpRequest) {
        User user = userMapper.mapToEntity(signUpRequest);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        user.updatePassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public JwtToken login(UserRequest userRequest) {
        String username = userRequest.getEmail();
        String password = userRequest.getPassword();

        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, user.getUserId());

        return jwtToken;
    }

    @Transactional
    public void updatePassword(long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getEmail().equals(userRequest.getEmail())) {
            user.updatePassword(userRequest.getPassword());
            userRepository.save(user);
        }
    }

}
