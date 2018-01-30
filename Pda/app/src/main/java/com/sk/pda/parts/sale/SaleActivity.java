package com.sk.pda.parts.sale;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.sk.pda.R;

import java.util.ArrayList;

public class SaleActivity extends Activity implements OnChartGestureListener, OnChartValueSelectedListener {
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        initChartView();
    }


    /**
     *初始化Line图表
     */
    private void initChartView(){
        mChart = (LineChart) findViewById(R.id.chart1);
        //设置监听
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        //设置网格背景
        mChart.setDrawGridBackground(false);

        //为图表设置描述说明,可以设置字体大小、颜色等等
        Description mDescript = new Description();
        mDescript.setText("这个图标的内容");
        mChart.setDescription(mDescript);

        //为图表设置为空的时候的文字和颜色
        mChart.setNoDataText("你的图表没有设置数据");
        mChart.setNoDataTextColor(Color.BLACK);

        //设置触碰手势可用
        mChart.setTouchEnabled(true);

        //设置拖放和缩放可用
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        //设置x轴和y轴缩放可用
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // 如果禁止，x轴和y轴可分开缩放
        mChart.setPinchZoom(true);

        // 设置背景颜色
        // mChart.setBackgroundColor(Color.GRAY);

        // 初始化标注的提示信息框
        MarkerView mv = new MarkerView(this, R.layout.custom_marker_view);
        //为图表设置标注
        mChart.setMarkerView(mv);

        setChartData(45, 100);
    }

    /**
     * 为图表设置数据
     * @param count  数据的数量
     * @param range  数值范围
     */
    private void setChartData(int count, float range) {

        //生成数值数组列表
        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        //数据集
        LineDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "数据1");

            // 设置数据虚线如： "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            //设置数据
            mChart.setData(data);
        }
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
