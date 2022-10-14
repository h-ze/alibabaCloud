package com.hz.server.editor;

import java.beans.PropertyEditorSupport;

public class DateEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        setValue(DateHelper.parseDate(text));
    }

}
