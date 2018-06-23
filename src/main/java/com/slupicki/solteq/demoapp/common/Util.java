package com.slupicki.solteq.demoapp.common;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import java.util.Collection;
import java.util.List;
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
        final HorizontalLayout horizontalLayout = new HorizontalLayout(
                label,
                addBtn
        );
        return horizontalLayout;
    }
}
