package com.example.myweather;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class WateringTimeView extends androidx.appcompat.widget.AppCompatTextView {
    private static final String TEMPLATE = "Duration: <strong>%s</strong>";
    private String template;

    public WateringTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TemplateWateringTimeView);
        template = attributes.getString(R.styleable.TemplateWateringTimeView_template);

        if (template == null || !template.contains("%s")) {
            template = "%s";
        }

        attributes.recycle();
    }

    public void setDuration(float duration) {
        int durationInMinutes = Math.round(duration / 60);
        int hours = durationInMinutes / 60;
        int minutes = durationInMinutes % 60;

        String hourText = "";
        String minuteText = "";

        if (hours > 0) {
            hourText = hours + (hours == 1 ? " hour " : " hours ");
        }
        if (minutes > 0) {
            minuteText = minutes + (minutes == 1 ? " minute" : " minutes");
        }
        if (hours == 0 && minutes == 0) {
            minuteText = "less than 1 minute";
        }

        String durationText = String.format(template, hourText + minuteText);
        setText(Html.fromHtml(durationText), BufferType.SPANNABLE);
    }
    public void setTemplate(String template) {
        this.template = template;
    }
}
