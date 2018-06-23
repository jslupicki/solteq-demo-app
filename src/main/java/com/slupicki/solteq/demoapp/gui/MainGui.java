package com.slupicki.solteq.demoapp.gui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;

@SpringUI
@Push
public class MainGui extends UI {

    private static final Logger log = LoggerFactory.getLogger(MainGui.class);

    private final EmployeesTab employeesTab;
    private final ChartsTab chartsTab;
    private final UsersTab usersTab;

    @Autowired
    public MainGui(EmployeesTab employeesTab, ChartsTab chartsTab, UsersTab usersTab) {
        this.employeesTab = employeesTab;
        this.chartsTab = chartsTab;
        this.usersTab = usersTab;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(employeesTab, "Employees");
        tabSheet.addTab(chartsTab, "Charts");
        tabSheet.addTab(usersTab, "Users");
        tabSheet.addSelectedTabChangeListener(event -> {
            TabSheet eventTabSheet = event.getTabSheet();
            Component selectedTab = eventTabSheet.getSelectedTab();
            TabSheet.Tab tab = eventTabSheet.getTab(selectedTab);
            int tabPosition = eventTabSheet.getTabPosition(tab);
            log.info("Selected {} tab", tab.getCaption());
            switch (tabPosition) {
                case 0:
                    employeesTab.refresh();
                    break;
                case 1:
                    chartsTab.refresh();
                    break;
                case 2:
                    usersTab.refresh();
                    break;
                default:
                    log.error("Unknown tab: {}", tab.getCaption());
                    break;
            }
        });

        setContent(tabSheet);
    }

    @WebServlet(urlPatterns = "/*", name = "EmpleyeeGUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainGui.class, productionMode = false)
    public static class EmpleyeeGUIServlet extends VaadinServlet {
    }
}
