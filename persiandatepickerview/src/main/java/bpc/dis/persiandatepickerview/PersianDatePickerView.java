package bpc.dis.persiandatepickerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bpc.dis.dateutilities.JalaliCalendar.JalaliCalendar;
import bpc.dis.dateutilities.LeapYearHelper.LeapYearHelper;
import bpc.dis.dateutilities.SolarCalendar.SolarCalendar;

public class PersianDatePickerView extends FrameLayout {

    private Spinner spYear;
    private Spinner spMonth;
    private Spinner spDay;
    private ConstraintLayout clYear;
    private ConstraintLayout clMonth;
    private ConstraintLayout clDay;

    private Date startDate;
    private Date endDate;
    private List<String> years;
    private List<String> months;
    private List<String> days;

    private boolean isLeapYear;
    private AdapterProperty adapterProperty;
    private SpinnerAdapter adapterDay;
    private SpinnerAdapter adapterMonth;
    private SpinnerAdapter adapterYears;

    public PersianDatePickerView(@NonNull Context context) {
        super(context);
    }

    public PersianDatePickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public PersianDatePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PersianDatePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        View view = inflate(context, R.layout.dvp_persian_date_picker_view, this);
        spYear = view.findViewById(R.id.sp_year);
        spMonth = view.findViewById(R.id.sp_month);
        spDay = view.findViewById(R.id.sp_day);
        clYear = view.findViewById(R.id.cl_year);
        clMonth = view.findViewById(R.id.cl_month);
        clDay = view.findViewById(R.id.cl_day);
        adapterProperty = new AdapterProperty();
        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                isLeapYear = LeapYearHelper.checkLeapYear(Integer.parseInt(years.get(position)));
                setDay(spMonth.getSelectedItemPosition() + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                setDay(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        setupView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void setupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.PersianDatePickerView);
        setupLayout(context, styledAttributes);
        styledAttributes.recycle();
    }

    private void setupLayout(Context context, TypedArray styledAttributes) {

        int backgroundRes = styledAttributes.getResourceId(R.styleable.PersianDatePickerView_dpvBackground, -1);
        if (backgroundRes == -1) {
            backgroundRes = R.drawable.default_persian_date_picker_background;
        }
        setBackgroundResource(backgroundRes);

        int textColor = styledAttributes.getColor(R.styleable.PersianDatePickerView_dpvTextColor, context.getResources().getColor(R.color.persianDatePickerViewSpinnerTextColor));
        setTextColor(textColor);

        float textSize = styledAttributes.getDimension(R.styleable.PersianDatePickerView_dpvTextSize, context.getResources().getDimension(R.dimen.persianDatePickerViewTextSize));
        setTextSize(textSize);

        int textStyle = styledAttributes.getInteger(R.styleable.PersianDatePickerView_dpvTextStyle, 0);
        setTextStyle(textStyle);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "iran_sans_fa_number.ttf");
        setTypeface(typeface);

        setInitialAdapter();
    }

    private void setInitialAdapter() {
        days = new ArrayList<>();
        adapterDay = new SpinnerAdapter(getContext(), R.layout.dvp_persian_date_picker_adapter, R.id.txt_title, days, adapterProperty);
        spDay.setAdapter(adapterDay);

        months = new ArrayList<>();
        adapterMonth = new SpinnerAdapter(getContext(), R.layout.dvp_persian_date_picker_adapter, R.id.txt_title, months, adapterProperty);
        spMonth.setAdapter(adapterMonth);

        years = new ArrayList<>();
        adapterYears = new SpinnerAdapter(getContext(), R.layout.dvp_persian_date_picker_adapter, R.id.txt_title, years, adapterProperty);
        spYear.setAdapter(adapterYears);
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setBackgroundResource(int backgroundRes) {
        clYear.setBackgroundResource(backgroundRes);
        clMonth.setBackgroundResource(backgroundRes);
        clDay.setBackgroundResource(backgroundRes);
    }

    public void setTypeface(Typeface typeface) {
        adapterProperty.setTypeface(typeface);
    }

    public void setTextStyle(int textStyle) {
        adapterProperty.setTextStyle(textStyle);
    }

    public void setTextSize(float textSize) {
        textSize = textSize / getResources().getDisplayMetrics().density;
        adapterProperty.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        adapterProperty.setTextColor(textColor);
    }

    public void initDate() {
        setYear();
        setMonth();
    }

    public Date getSelectedDate() {
        String day = String.valueOf(spDay.getSelectedItemPosition() + 1);
        String month = String.valueOf(spMonth.getSelectedItemPosition() + 1);
        String year = years.get(spYear.getSelectedItemPosition());
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        return jalaliCalendar.getGregorianDate(year + "/" + month + "/" + day);
    }


    private void setYear() {
        SolarCalendar solarCalendar = new SolarCalendar(startDate);
        int startYear = solarCalendar.year;
        solarCalendar = new SolarCalendar(endDate);
        int endYear = solarCalendar.year;
        for (int i = startYear; i <= endYear; i++) {
            years.add(String.valueOf(i));
        }
        adapterYears.notifyDataSetChanged();
    }

    private void setMonth() {
        months.addAll(Arrays.asList(getResources().getStringArray(R.array.month)).subList(0, 12));
        adapterMonth.notifyDataSetChanged();
    }

    private void setDay(int month) {
        int end = 31;
        if (month > 6) {
            if (!isLeapYear) {
                if (month == 12)
                    end = 29;
                else
                    end = 30;
            } else {
                end = 30;
            }
        }
        days.clear();
        for (int i = 1; i <= end; i++) {
            days.add(String.valueOf(i));
        }
        adapterDay.notifyDataSetChanged();
    }

}