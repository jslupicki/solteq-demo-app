package com.slupicki.solteq.demoapp;

import com.slupicki.solteq.demoapp.dao.SalaryRepository;
import com.slupicki.solteq.demoapp.model.Salary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class SalaryRepositoryTest {

    @Autowired
    private SalaryRepository salaryRepository;
    private Salary salary1_in;
    private Salary salary2_in;
    private Salary salary3_ex;
    private Salary salary4_in;
    private Salary salary5_in;
    private Salary salary6_in;
    private Salary salary7_ex;
    private Salary salary8_ex;

    @Before
    public void setUp() {
        salaryRepository.deleteAll();
        // Should be included because starts in such month and ends in next month
        salary1_in = new Salary(
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 2, 1),
                BigDecimal.valueOf(100)
        );
        // The same as salary1
        salary2_in = new Salary(
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 2, 1),
                BigDecimal.valueOf(200)
        );
        // Should be excluded because starts in next month
        salary3_ex = new Salary(
                LocalDate.of(2018, 2, 1),
                LocalDate.of(2018, 3, 1),
                BigDecimal.valueOf(300)
        );
        // Should be included because starts in previous year and ends in the future
        salary4_in = new Salary(
                LocalDate.of(2017, 2, 1),
                LocalDate.of(2018, 4, 1),
                BigDecimal.valueOf(300)
        );
        // Should be included because starts in previous year and ends in such month
        salary5_in = new Salary(
                LocalDate.of(2017, 2, 1),
                LocalDate.of(2018, 1, 15),
                BigDecimal.valueOf(300)
        );
        // Should be included because starts and ends in such month
        salary6_in = new Salary(
                LocalDate.of(2018, 1, 15),
                LocalDate.of(2018, 1, 18),
                BigDecimal.valueOf(300)
        );
        // Should be excluded as it is in the past
        salary7_ex = new Salary(
                LocalDate.of(2017, 1, 15),
                LocalDate.of(2017, 2, 15),
                BigDecimal.valueOf(300)
        );
        // Should be excluded as it is in the future
        salary8_ex = new Salary(
                LocalDate.of(2018, 3, 15),
                LocalDate.of(2018, 4, 15),
                BigDecimal.valueOf(300)
        );
        salaryRepository.save(salary1_in);
        salaryRepository.save(salary2_in);
        salaryRepository.save(salary3_ex);
        salaryRepository.save(salary4_in);
        salaryRepository.save(salary5_in);
        salaryRepository.save(salary6_in);
        salaryRepository.save(salary7_ex);
        salaryRepository.save(salary8_ex);
    }

    @Test
    public void find_salaries_for_month() {
        int year = 2018;
        int month = 1;
        LocalDate fromDate = LocalDate.of(year, month, 1);
        LocalDate toDate = fromDate.plusMonths(1).minusDays(1);

        List<Salary> salaries = salaryRepository.findByFromDateIsLessThanEqualAndToDateIsGreaterThan(
                toDate,
                fromDate
        );

        assertThat(salaries)
                .hasSize(5)
                .usingElementComparatorOnFields("amount", "fromDate", "toDate")
                .containsExactlyInAnyOrder(
                        salary1_in,
                        salary2_in,
                        salary4_in,
                        salary5_in,
                        salary6_in
                )
        ;
    }

    @Test
    public void find_first_and_last_date_of_salary() {
        assertThat(salaryRepository.findFirstDate())
                .isEqualTo("2017-01-15") // salary7_ex
        ;
        assertThat(salaryRepository.findLatestDate())
                .isEqualTo("2018-04-15") // salary8_ex
        ;
    }
}
