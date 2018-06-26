package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.exports.EmployeeCsvExport;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExportsTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ReportsTab.class);

    @Autowired
    private EmployeeCsvExport employeeCsvExport;

    public ExportsTab() {
        super();
        refresh();
    }

    public void refresh() {
        removeAllComponents();

        Button employeeCsvDownload = new Button("Download");
        StreamResource myResource = createResource();
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(employeeCsvDownload);
        addComponent(new HorizontalLayout(
                new Label("Employees in CSV"),
                employeeCsvDownload
        ));
    }

    private StreamResource createResource() {
        return new StreamResource(
                (StreamResource.StreamSource) () -> Try.of(() -> employeeCsvExport.generate()).getOrNull(),
                "employees.csv"
        );
    }
}
