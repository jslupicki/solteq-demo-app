package com.slupicki.solteq.demoapp.common;

import com.slupicki.solteq.demoapp.gui.MainGui;
import com.slupicki.solteq.demoapp.model.User;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface Util {

    static <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    static <T> void refreshGrid(Grid<T> grid, Collection<T> items) {
        grid.setItems(items);
        grid.recalculateColumnWidths();
    }

    static HorizontalLayout captionAndAddButton(String caption, Button.ClickListener clickListener) {
        HorizontalLayout result = new HorizontalLayout();
        final Label label = new Label(caption);
        result.addComponent(label);
        if (User.Access.ADMIN.equals(Util.currentUserAccess())) {
            final Button addBtn = new Button("Add");
            addBtn.addClickListener(clickListener);
            result.addComponent(addBtn);
        }
        return result;
    }

    static HorizontalLayout search(Consumer<String> searchListener) {
        final TextField searchTf = new TextField();
        final Button searchBtn = new Button("Search");
        searchBtn.addClickListener(event ->
                searchListener.accept(searchTf.getValue())
        );
        return new HorizontalLayout(
                searchTf,
                searchBtn
        );
    }

    static Button editButton(Button.ClickListener clickListener) {
        Button button = new Button(VaadinIcons.EDIT);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(clickListener);
        return button;
    }

    static Button deleteButton(Button.ClickListener clickListener) {
        Button button = new Button(VaadinIcons.DEL);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(clickListener);
        return button;
    }

    static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            return myHash;
        } catch (Exception e) {
            return password;
        }
    }

    static User currentUser() {
        return ((MainGui) UI.getCurrent()).getUser();
    }

    static User.Access currentUserAccess() {
        if (currentUser() != null) {
            return currentUser().getAccess();
        }
        return User.Access.NONE;
    }

    static Component userBar() {
        String login = currentUser() != null ? currentUser().getLogin() : "";
        User.Access access = currentUserAccess();
        return new Label(
                String.format("User: %s, Access: %s", login, access.name())
        );
    }
}
