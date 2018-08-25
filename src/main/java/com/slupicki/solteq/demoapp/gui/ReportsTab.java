package com.slupicki.solteq.demoapp.gui;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReportsTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ReportsTab.class);

    public ReportsTab() {
        super();
        refresh();
    }

    public void refresh() {
        log.info("Refresh {}", this.getClass().getSimpleName());
        removeAllComponents();
        addComponent(new Label("Reports - work in progress"));
    }
}
