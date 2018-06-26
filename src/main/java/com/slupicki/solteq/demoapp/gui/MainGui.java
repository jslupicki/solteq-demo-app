package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.common.Util;
import com.slupicki.solteq.demoapp.dao.UserRepository;
import com.slupicki.solteq.demoapp.model.User;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
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
    private final ReportsTab reportsTab;
    private final ExportsTab exportsTab;

    private final UserRepository userRepository;

    private User user;

    @Autowired
    public MainGui(
            EmployeesTab employeesTab,
            ChartsTab chartsTab,
            UsersTab usersTab,
            ReportsTab reportsTab,
            ExportsTab exportsTab,
            UserRepository userRepository
    ) {
        this.employeesTab = employeesTab;
        this.chartsTab = chartsTab;
        this.usersTab = usersTab;
        this.reportsTab = reportsTab;
        this.exportsTab = exportsTab;
        this.userRepository = userRepository;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(employeesTab, "Employees");
        tabSheet.addTab(chartsTab, "Charts");
        tabSheet.addTab(reportsTab, "Reports");
        tabSheet.addTab(exportsTab, "Exports");
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
                    reportsTab.refresh();
                    break;
                case 3:
                    exportsTab.refresh();
                    break;
                case 4:
                    usersTab.refresh();
                    break;
                default:
                    log.error("Unknown tab: {}", tab.getCaption());
                    break;
            }
        });

        FormLayout loginLayout = new FormLayout();
        TextField loginTf = new TextField("Login:");
        PasswordField passwordPf = new PasswordField("Password:");
        Button loginBtn = new Button("Login", event -> {
            String login = loginTf.getValue();
            String password = passwordPf.getValue();
            log.info("Try to log '{}' with hashed password '{}'", login, Util.hashPassword(password));
            User user = userRepository.findByLoginAndPassword(login, Util.hashPassword(password));
            if (user != null) {
                this.user = user;
                setContent(
                        new VerticalLayout(
                                Util.userBar(),
                                tabSheet
                        )
                );
                employeesTab.refresh();
            } else {
                this.user = null;
                Notification.show("Access denied !", Notification.Type.ERROR_MESSAGE);
            }
        });
        loginLayout.addComponents(
                loginTf,
                passwordPf,
                loginBtn
        );

        setContent(loginLayout);
    }

    public User getUser() {
        return user;
    }

    @WebServlet(urlPatterns = "/*", name = "EmpleyeeGUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainGui.class, productionMode = false)
    public static class EmpleyeeGUIServlet extends VaadinServlet {
    }
}
