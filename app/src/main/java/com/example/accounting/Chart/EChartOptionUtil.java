package com.example.accounting.Chart;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;

import java.util.List;
import java.util.Map;

public class EChartOptionUtil {
    /**
     * 画折线图
     *
     * @param xAxis x轴的相关配置
     * @param yAxis y轴的相关配置
     * @return
     */
    public static GsonOption getLineChartOptions(Object[] xAxis, Object[] yAxis) {
        //通过option指定图表的配置项和数据
        GsonOption option = new GsonOption();
        option.title("折线图");//折线图的标题
        option.legend("销量");//添加图例
        option.tooltip().trigger(Trigger.axis);//提示框（详见tooltip），鼠标悬浮交互时的信息提示

        ValueAxis valueAxis = new ValueAxis();
        option.yAxis(valueAxis);//添加y轴

        CategoryAxis categorxAxis = new CategoryAxis();
        categorxAxis.axisLine().onZero(false);//坐标轴线，默认显示，属性show控制显示与否，属性lineStyle（详见lineStyle）控制线条样式
        categorxAxis.boundaryGap(true);
        categorxAxis.data(xAxis);//添加坐标轴的类目属性
        option.xAxis(categorxAxis);//x轴为类目轴

        Line line = new Line();

        //设置折线的相关属性
        line.smooth(true).name("销量").data(yAxis).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");

        //添加数据，将数据添加到option中
        option.series(line);
        return option;
    }

    /**
     * 饼状图的option配置
     * @param data  待传入的饼图的数据
     * @return
     */
    public static GsonOption getPieChartOptions(List<Map<String, Object>> data,String title) {
        GsonOption option = new GsonOption();
        option.title(title);
        option.legend("输出");
        option.tooltip().trigger(Trigger.axis);

        Pie pie = new Pie();
        pie.type(SeriesType.pie);
        pie.radius("55%");
        pie.itemStyle().emphasis().shadowBlur(10).shadowOffsetX(0).shadowColor("rgba(0, 0, 0, 0.5)");
        pie.setData(data);//data是传入的参数

        option.series(pie);
        return option;
    }

}

