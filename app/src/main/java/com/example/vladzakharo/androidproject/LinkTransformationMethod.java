package com.example.vladzakharo.androidproject;

import android.graphics.Rect;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.TransformationMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Vlad Zakharo on 02.01.2017.
 */

public class LinkTransformationMethod implements TransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        TextView textView = (TextView) view;
        if (textView.getText() == null || !(textView.getText() instanceof Spannable)) {
            return source;
        }

        Spannable text = (Spannable) textView.getText();

        URLSpan spans[] = textView.getUrls();
        for (URLSpan span: spans) {
            String url = span.getURL();
            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);
            text.removeSpan(span);
            text.setSpan(new NewCustomChromeTab(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return text;
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

    }
}
