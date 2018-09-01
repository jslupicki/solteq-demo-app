package com.slupicki.solteq.demoapp.dao;

import com.slupicki.solteq.demoapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    User findByLoginAndPassword(String login, String password);

    User findByLogin(String login);
}
