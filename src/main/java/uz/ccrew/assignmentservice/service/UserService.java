package uz.ccrew.assignmentservice.service;

import uz.ccrew.assignmentservice.dto.user.UserDTO;
import uz.ccrew.assignmentservice.dto.user.UserUpdateDTO;

import org.springframework.data.domain.Page;

public interface UserService {
    UserDTO get();

    UserDTO getById(Long userId);

    UserDTO update(UserUpdateDTO dto);

    UserDTO updateById(Long userId, UserUpdateDTO dto);

    void deleteById(Long userId);

    Page<UserDTO> getList(int page, int size);
}