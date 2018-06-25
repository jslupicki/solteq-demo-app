package com.slupicki.solteq.demoapp.common;

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
        final Label label = new Label(caption);
        final Button addBtn = new Button("Add");
        addBtn.addClickListener(clickListener);
        return new HorizontalLayout(
                label,
                addBtn
        );
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

}
