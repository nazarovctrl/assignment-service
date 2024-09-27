package uz.ccrew.assignmentservice.auth.service;

import uz.ccrew.assignmentservice.user.dto.UserDTO;
import uz.ccrew.assignmentservice.auth.dto.LoginDTO;
import uz.ccrew.assignmentservice.auth.dto.RegisterDTO;
import uz.ccrew.assignmentservice.auth.dto.LoginResponseDTO;

public interface AuthService {
    UserDTO register(RegisterDTO dto);

    LoginResponseDTO login(LoginDTO loginRequest);

    String refresh();
}