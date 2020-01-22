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
import java.util.Date;
import java.util.List;

import bpc.dis.dateutilities.JalaliCalendar.JalaliCalendar;
import bpc.dis.dateutilities.PersianCalendarKernel;
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

    private AdapterProperty adapterProperty;
    private SpinnerAdapter adapterDay;
    private SpinnerAdapter adapterMonth;
    private SpinnerAdapter adapterYears;
    private PersianCalendarKernel persianCalendarKernel;
    private boolean hasSelectedRequest = false;
    private SolarCalendar selectedSolarCalendar;

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
                setMonthByYear(Integer.valueOf(years.get(spYear.getSelectedItemPosition())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                setDayByYearAndMonth(Integer.valueOf(years.get(spYear.getSelectedItemPosition())), months.get(position));
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void initDate() {
        persianCalendarKernel = new PersianCalendarKernel(getContext(), startDate, endDate);
        setYear();
    }

    public Date getSelectedDate() {
        String day = String.valueOf(spDay.getSelectedItemPosition() + 1);
        String month = String.valueOf(spMonth.getSelectedItemPosition() + 1);
        String year = years.get(spYear.getSelectedItemPosition());
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        return jalaliCalendar.getGregorianDate(year + "/" + month + "/" + day);
    }

    public void setSelectedDate(Date date) {
        hasSelectedRequest = true;
        selectedSolarCalendar = new SolarCalendar(date);
        setSelectedYear(selectedSolarCalendar.year);
    }


    private void setYear() {
        years.clear();
        years.addAll(persianCalendarKernel.getYears());
        adapterYears.notifyDataSetChanged();
    }

    private void setMonthByYear(int year) {
        months.clear();
        months.addAll(persianCalendarKernel.getMonthsByYear(year));
        adapterMonth.notifyDataSetChanged();
        if (hasSelectedRequest) {
            setSelectedMonth(selectedSolarCalendar.month);
        }
    }

    private void setDayByYearAndMonth(int year, String stringMonth) {
        days.clear();
        days.addAll(persianCalendarKernel.getDaysByMonth(year, stringMonth));
        adapterDay.notifyDataSetChanged();
        if (hasSelectedRequest) {
            setSelectedDay(selectedSolarCalendar.day);
        }
    }


    private void setSelectedYear(int year) {
        try {
            for (int i = 0; i < years.size(); i++) {
                if (Integer.valueOf(years.get(i)) == year) {
                    spYear.setSelection(i, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelectedMonth(int month) {
        try {
            if (months.size() >= month) {
                spMonth.setSelection(month - 1, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelectedDay(int day) {
        try {
            hasSelectedRequest = false;
            selectedSolarCalendar = null;
            if (days.size() >= day) {
                spDay.setSelection(day - 1, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}