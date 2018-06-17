package com.projects.juan.journeys.modules;

public class SmsCode {
    private ChangeListener changeListener;

    public void setListener(ChangeListener changeListener){
        this.changeListener = changeListener;
    }

    public void setSmsCode(String sms_code) {
        if (changeListener != null) changeListener.onChange(sms_code);
    }

    public interface ChangeListener {
        void onChange(String sms_code);
    }
}
