package com.slupicki.solteq.demoapp.gui;

import com.slupicki.solteq.demoapp.charts.SalariesPerMonthChart;
import com.slupicki.solteq.demoapp.dao.SalaryRepository;
import com.slupicki.solteq.demoapp.model.Salary;
import com.vaadin.ui.*;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.slupicki.solteq.demoapp.common.Constants.DATE_FORMATTER;
import static com.slupicki.solteq.demoapp.common.Constants.DATE_PATTERN;

@Component
public class ChartsTab extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(ChartsTab.class);

    private SalariesPerMonthChart chart;
    private final SalaryRepository salaryRepository;

    @Autowired
    public ChartsTab(SalaryRepository salaryRepository) {
        super();
        this.salaryRepository = salaryRepository;
        LocalDate firstDate = salaryRepository.findFirstDate();
        LocalDate latestDate = salaryRepository.findLatestDate();

        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(new Label("From date:"));
        final DateField fromDateDf = new DateField();
        fromDateDf.setDateFormat(DATE_PATTERN);
        fromDateDf.setValue(firstDate);
        horizontalLayout.addComponent(fromDateDf);
        horizontalLayout.addComponent(new Label("To date:"));
        final DateField toDateDf = new DateField();
        toDateDf.setDateFormat(DATE_PATTERN);
        toDateDf.setValue(latestDate);
        horizontalLayout.addComponent(toDateDf);
        final Button drawButton = new Button("Draw", event -> {
            if (chart != null) {
                removeComponent(chart);
            }
            chart = getChart(fromDateDf.getValue(), toDateDf.getValue());
            addComponent(chart);
        });
        horizontalLayout.addComponent(drawButton);
        addComponent(horizontalLayout);
    }

    public void refresh() {
        log.info("Refresh {}", this.getClass().getSimpleName());
        if (chart != null)
            chart.drawChart();
    }

    private SalariesPerMonthChart getChart(LocalDate fromDate, LocalDate toDate) {
        String Title = "Average salary per month";
        String xTtitle = "Month";
        String yTitle = "$";
        String subTitle = String.format("from %s to %s", DATE_FORMATTER.format(fromDate), DATE_FORMATTER.format(toDate));

        Tuple2<List<String>, List<String>> data = collectData(fromDate, toDate);
        List<String> months = data._1;
        List<String> salaries = data._2;

        // Make custom-made google bar chart
        SalariesPerMonthChart chart = new SalariesPerMonthChart(Title, subTitle);

        // Add headers
        chart.addHeaders(List.of(xTtitle, "Average salary").asJava());

        // add data
        months.zip(salaries).forEach(tuple ->
            chart.addValues(List.of(tuple._1, tuple._2).asJava())
        );

        chart.drawChart();

        return chart;
    }

    private Tuple2<List<String>, List<String>> collectData(LocalDate fromDate, LocalDate toDate) {
        List<String> months = List.empty();
        List<String> salaries = List.empty();

        LocalDate startDate = LocalDate.of(fromDate.getYear(), fromDate.getMonth(), 1);
        while (startDate.isBefore(toDate)) {
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);
            String month = String.format("%d/%d", startDate.getMonthValue(), startDate.getYear());
            log.info("Check month '{}'", month);
            months = months.append(month);
            List<Salary> salariesOfMonth = List.ofAll(salaryRepository.findByFromDateIsLessThanEqualAndToDateIsGreaterThan(endDate, startDate));
            log.info("Find {} salaries", salariesOfMonth.size());
            BigDecimal sum = salariesOfMonth.map(Salary::getAmount).fold(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal average = salariesOfMonth.size() > 0 ? sum.divide(BigDecimal.valueOf(salariesOfMonth.size())) : BigDecimal.ZERO;
            log.info("sum: {}, average: {}", sum, average);
            salaries = salaries.append(average.toString());
            startDate = startDate.plusMonths(1);
        }

        return Tuple.of(months, salaries);
    }
}
