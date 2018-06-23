package com.slupicki.solteq.demoapp.gui;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChartsTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ChartsTab.class);

    public ChartsTab() {
        super();
        addComponent(new Label("Charts - work in progress"));
    }

    public void refresh() {
        log.info("Refresh {}", this.getClass().getSimpleName());
    }
}
