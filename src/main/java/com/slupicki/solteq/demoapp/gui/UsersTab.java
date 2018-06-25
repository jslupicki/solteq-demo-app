package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.common.Util;
import com.slupicki.solteq.demoapp.dao.UserRepository;
import com.slupicki.solteq.demoapp.model.User;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(UsersTab.class);

    private final UserRepository repository;

    private List<User> users;

    private final Grid<User> userGrid = new Grid<>();

    public UsersTab(UserRepository userRepository) {
        super();
        this.repository = userRepository;
        users = Util.iterableToList(repository.findAll());

        final HorizontalLayout gridCaptionLayout = Util.captionAndAddButton("Users", e -> {
            User user = new User("", "", User.Access.REGULAR);
            editUser(user);
        });

        userGrid.addColumn(user -> users.indexOf(user) + 1).setExpandRatio(0);
        userGrid.addColumn(User::getLogin).setCaption("Login").setExpandRatio(1);
        userGrid.addColumn(User::getAccess).setCaption("Access").setExpandRatio(2);
        userGrid.addComponentColumn(user -> Util.editButton(event -> editUser(user))).setExpandRatio(0);
        userGrid.addComponentColumn(user -> Util.deleteButton(event -> deleteUser(user))).setExpandRatio(0);
        userGrid.setFrozenColumnCount(1);

        refreshGrid(userGrid);

        addComponent(gridCaptionLayout);
        addComponent(userGrid);
    }

    public void refresh() {
        refreshGrid();
    }

    private void editUser(User u) {
        new UserEditor(u,
                (userToSave, editor) -> {
                    log.info("Save edit: {}", u);
                    repository.save(userToSave);
                    refreshGrid();
                },
                (userCanceled, editor) -> log.info("Cancel edit: {}", u)
        ).show();
    }

    private void deleteUser(User u) {
        new YesNoWindow("Are you sure?",
                event -> {
                    log.info("Delete: {}", u);
                    repository.delete(u);
                    refreshGrid();
                },
                event -> log.info("NO delete: {}", u)
        ).show();
    }

    private void refreshGrid() {
        refreshGrid(userGrid);
    }

    private void refreshGrid(Grid<User> grid) {
        users = Util.iterableToList(repository.findAll());
        Util.refreshGrid(grid, users);
    }
}
