package com.example.user.selectableexlistview;

/**
 * Created by User on 12/21/2017.
 */

public class ChildItem {
    private boolean isSelect;
    private String text;

    public ChildItem(boolean isSelect, String text) {
        this.isSelect = isSelect;
        this.text = text;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
