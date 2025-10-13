package com.example.CRUD_Spring_mySQL.service;

import com.example.CRUD_Spring_mySQL.domain.User;
import com.example.CRUD_Spring_mySQL.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<User> listAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public User create(User u) {
        if (repo.existsByEmail(u.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        return repo.save(u);
    }

    @Transactional
    public User update(Long id, User data) {
        User u = getById(id);
        u.setName(data.getName());
        u.setEmail(data.getEmail());
        return repo.save(u);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Usuário não encontrado");
        repo.deleteById(id);
    }
}
