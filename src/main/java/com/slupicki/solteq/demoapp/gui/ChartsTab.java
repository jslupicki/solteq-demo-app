package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.charts.SalariesPerMonthChart;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChartsTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ChartsTab.class);
    private final SalariesPerMonthChart chart;

    public ChartsTab() {
        super();
        addComponent(new Label("Charts - work in progress"));
        chart = getChart();
        addComponent(chart);
    }

    public void refresh() {
        log.info("Refresh {}", this.getClass().getSimpleName());
        chart.drawChart();
    }

    private SalariesPerMonthChart getChart() {
        String Title = "Salaries per months";
        String xTtitle = "Month";
        String yTitle = "$";
        String subTitle = "from 01/2018 to 05/2018";

        List<String> months = List.of(
                "01/2018",
                "02/2018",
                "03/2018",
                "04/2018",
                "05/2018"
        );
        List<String> salaries = List.of(
                "11",
                "12",
                "13",
                "14",
                "15"
        );

        // Make custom-made google bar chart
        SalariesPerMonthChart chart = new SalariesPerMonthChart(Title, subTitle);

        // Add headers
        chart.addHeaders(List.of(xTtitle, "Salaries").asJava());

        // add data
        months.zip(salaries).forEach(tuple ->
            chart.addValues(List.of(tuple._1, tuple._2).asJava())
        );

        chart.drawChart();

        return chart;
    }
}
