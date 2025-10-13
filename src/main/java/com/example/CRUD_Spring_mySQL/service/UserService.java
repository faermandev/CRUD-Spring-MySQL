package com.example.CRUD_Spring_mySQL.service;

import com.example.CRUD_Spring_mySQL.domain.User;
import com.example.CRUD_Spring_mySQL.dto.UserDTO;
import com.example.CRUD_Spring_mySQL.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> listAll() {
        return repo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getById(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toDTO(user);
    }

    @Transactional
    public UserDTO create(UserDTO dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        User user = toEntity(dto);
        return toDTO(repo.save(user));
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return toDTO(repo.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Usuário não encontrado");
        repo.deleteById(id);
    }

    private UserDTO toDTO(User u) {
        return new UserDTO(u.getId(), u.getName(), u.getEmail());
    }

    private User toEntity(UserDTO dto) {
        return new User(dto.getId(), dto.getName(), dto.getEmail(), null, null);
    }
}
