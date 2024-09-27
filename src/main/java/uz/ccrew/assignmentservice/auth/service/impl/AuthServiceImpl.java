package uz.ccrew.assignmentservice.auth.service.impl;

import uz.ccrew.assignmentservice.auth.service.AuthService;
import uz.ccrew.assignmentservice.user.User;
import uz.ccrew.assignmentservice.base.AuthUtil;
import uz.ccrew.assignmentservice.user.UserRole;
import uz.ccrew.assignmentservice.user.dto.UserDTO;
import uz.ccrew.assignmentservice.auth.dto.LoginDTO;
import uz.ccrew.assignmentservice.user.UserMapper;
import uz.ccrew.assignmentservice.auth.dto.RegisterDTO;
import uz.ccrew.assignmentservice.security.jwt.JWTService;
import uz.ccrew.assignmentservice.exp.AlreadyExistException;
import uz.ccrew.assignmentservice.user.UserRepository;
import uz.ccrew.assignmentservice.auth.dto.LoginResponseDTO;
import uz.ccrew.assignmentservice.security.user.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthUtil authUtil;
    private final UserMapper userMapper;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDTO register(RegisterDTO dto) {
        Optional<User> optional = userRepository.findByLogin(dto.login().toLowerCase());
        if (optional.isPresent()) {
            throw new AlreadyExistException("Login is already existing");
        }
        User user = User.builder()
                .login(dto.login().toLowerCase())
                .password(passwordEncoder.encode(dto.password()))
                .role(UserRole.CUSTOMER)
                .credentialsModifiedDate(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public LoginResponseDTO login(final LoginDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.login().toLowerCase(), loginRequest.password()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new LoginResponseDTO(jwtService.generateAccessToken(userDetails.getUsername()), jwtService.generateRefreshToken(userDetails.getUsername()));
    }

    @Override
    public String refresh() {
        User user = authUtil.loadLoggedUser();
        return jwtService.generateAccessToken(user.getLogin());
    }
}