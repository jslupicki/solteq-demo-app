package com.slupicki.solteq.demoapp;

import com.slupicki.solteq.demoapp.dao.UserRepository;
import com.slupicki.solteq.demoapp.model.User;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoAppApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoAppApplication.class);


    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("UserRepository is empty - initialize");
            userRepository.saveAll(List.of(
                    new User("admin", "admin", User.Access.ADMIN),
                    new User("user", "user", User.Access.REGULAR)
            ));
        } else {
            log.info("UserRepository have users - do nothing");
        }
    }
}
