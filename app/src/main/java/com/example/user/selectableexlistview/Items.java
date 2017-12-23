package com.example.user.selectableexlistview;

/**
 * Created by User on 12/23/2017.
 */

public class Items {
    int parentId;
    int childId;
    int checkPos;

    public Items() {
    }


    public Items(int parentId, int childId) {
        this.parentId = parentId;
        this.childId = childId;
    }

    public Items(int parentId, int childId, int checkPos) {
        this.parentId = parentId;
        this.childId = childId;
        this.checkPos = checkPos;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getCheckPos() {
        return checkPos;
    }

    public void setCheckPos(int checkPos) {
        this.checkPos = checkPos;
    }
}
