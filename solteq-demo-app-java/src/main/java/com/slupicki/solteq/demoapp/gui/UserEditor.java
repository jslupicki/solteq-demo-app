package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.model.User;
import com.vaadin.ui.*;
import io.vavr.collection.List;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class UserEditor extends Window {

    private static final Logger log = LoggerFactory.getLogger(UserEditor.class);

    public UserEditor(
            User user,
            BiConsumer<User, UserEditor> saveListener,
            BiConsumer<User, UserEditor> cancelListener
    ) {
        super();
        init(user, saveListener, cancelListener);
    }

    public UserEditor(
            String caption,
            User user,
            BiConsumer<User, UserEditor> saveListener,
            BiConsumer<User, UserEditor> cancelListener
    ) {
        super(caption);
        init(user, saveListener, cancelListener);
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    private void init(
            User user,
            BiConsumer<User, UserEditor> saveListener,
            BiConsumer<User, UserEditor> cancelListener
    ) {
        setWidth(20, Unit.EM);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        final TextField loginTf = new TextField();
        loginTf.setCaption("Login");
        loginTf.setValue(user.getLogin());
        final PasswordField passwordPf = new PasswordField();
        passwordPf.setCaption("Password");
        passwordPf.setValue("");
        final RadioButtonGroup<String> accessGr = new RadioButtonGroup<>();
        accessGr.setItems(
                List.of(User.Access.values())
                        .map(Enum::name)
                        .asJava()
        );
        accessGr.setSelectedItem(user.getAccess().name());

        Button saveBtn = new Button("Save", event -> {
            if (Strings.isBlank(passwordPf.getValue())) {
                Notification.show("Don't set empty password", Notification.Type.WARNING_MESSAGE);
            } else {
                user.setLogin(loginTf.getValue());
                user.setPassword(passwordPf.getValue());
                user.setAccess(User.Access.valueOf(accessGr.getValue()));
                saveListener.accept(user, this);
                close();
            }
        });
        Button cancelBtn = new Button("Cancel", event -> {
            cancelListener.accept(user, this);
            close();
        });
        final HorizontalLayout buttonLayout = new HorizontalLayout(
                cancelBtn,
                saveBtn
        );
        content.addComponent(loginTf);
        content.addComponent(passwordPf);
        content.addComponent(accessGr);
        content.addComponent(buttonLayout);
        setContent(content);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }
}
