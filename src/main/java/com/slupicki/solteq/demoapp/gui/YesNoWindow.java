package com.slupicki.solteq.demoapp.gui;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class YesNoWindow extends Window {

    public YesNoWindow(Button.ClickListener yesListener, Button.ClickListener noListener) {
        init(yesListener, noListener);
    }

    public YesNoWindow(String caption, Button.ClickListener yesListener, Button.ClickListener noListener) {
        super(caption);
        init(yesListener, noListener);
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    private void init(Button.ClickListener yesListener, Button.ClickListener noListener) {
        final HorizontalLayout content = new HorizontalLayout();
        content.setMargin(true);
        Button yesBtn = new Button("Yes", event -> {
            yesListener.buttonClick(event);
            close();
        });
        Button noBtn = new Button("No", event -> {
            noListener.buttonClick(event);
            close();
        });
        content.addComponent(noBtn);
        content.addComponent(yesBtn);
        setContent(content);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }
}
