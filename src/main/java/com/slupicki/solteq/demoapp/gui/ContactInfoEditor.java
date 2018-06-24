package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.model.ContactInfo;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class ContactInfoEditor extends Window {

    private static final Logger log = LoggerFactory.getLogger(ContactInfoEditor.class);

    public ContactInfoEditor(
            ContactInfo contactInfo,
            BiConsumer<ContactInfo, ContactInfoEditor> saveListener,
            BiConsumer<ContactInfo, ContactInfoEditor> cancelListener
    ) {
        super();
        init(contactInfo, saveListener, cancelListener);
    }

    public ContactInfoEditor(
            String caption,
            ContactInfo contactInfo,
            BiConsumer<ContactInfo, ContactInfoEditor> saveListener,
            BiConsumer<ContactInfo, ContactInfoEditor> cancelListener
    ) {
        super(caption);
        init(contactInfo, saveListener, cancelListener);
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    private void init(
            ContactInfo contactInfo,
            BiConsumer<ContactInfo, ContactInfoEditor> saveListener,
            BiConsumer<ContactInfo, ContactInfoEditor> cancelListener
    ) {
        setWidth(20, Unit.EM);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        final TextField phoneTf = new TextField();
        phoneTf.setCaption("Phone");
        phoneTf.setValue(contactInfo.getPhone());
        final DateField fromDateDf = new DateField();
        fromDateDf.setDateFormat("yyyy-MM-dd");
        fromDateDf.setCaption("From date:");
        fromDateDf.setValue(contactInfo.getFromDate());
        Button saveBtn = new Button("Save", event -> {
            contactInfo.setPhone(phoneTf.getValue());
            contactInfo.setFromDate(fromDateDf.getValue());
            contactInfo.updateSearchString();
            saveListener.accept(contactInfo, this);
            close();
        });
        Button cancelBtn = new Button("Cancel", event -> {
            cancelListener.accept(contactInfo, this);
            close();
        });
        final HorizontalLayout buttonLayout = new HorizontalLayout(
                cancelBtn,
                saveBtn
        );
        content.addComponent(phoneTf);
        content.addComponent(fromDateDf);
        content.addComponent(buttonLayout);
        setContent(content);
        setModal(true);
        setClosable(false);
        setResizable(false);
    }
}
