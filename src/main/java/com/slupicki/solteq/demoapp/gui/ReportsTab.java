package com.slupicki.solteq.demoapp.gui;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReportsTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ReportsTab.class);

    public ReportsTab() {
        super();
        addComponent(new Label("Reports - work in progress"));
    }

    public void refresh() {
        log.info("Refresh {}", this.getClass().getSimpleName());
    }
}
