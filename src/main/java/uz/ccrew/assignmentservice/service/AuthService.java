package uz.ccrew.assignmentservice.service;

import uz.ccrew.assignmentservice.dto.user.UserDTO;
import uz.ccrew.assignmentservice.dto.auth.LoginDTO;
import uz.ccrew.assignmentservice.dto.auth.RegisterDTO;
import uz.ccrew.assignmentservice.dto.auth.LoginResponseDTO;

public interface AuthService {
    UserDTO register(RegisterDTO dto);

    LoginResponseDTO login(LoginDTO loginRequest);

    String refresh();
}