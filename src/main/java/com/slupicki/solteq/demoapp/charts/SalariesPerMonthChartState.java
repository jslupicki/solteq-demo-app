package com.slupicki.solteq.demoapp.charts;

import com.vaadin.shared.ui.JavaScriptComponentState;

import java.util.ArrayList;
import java.util.List;

public class SalariesPerMonthChartState extends JavaScriptComponentState {
    public String title;
    public String subTitle;
    public List<String> headers = new ArrayList<>();
    public List<List<String>> values = new ArrayList<>();
    public String myId;
}
