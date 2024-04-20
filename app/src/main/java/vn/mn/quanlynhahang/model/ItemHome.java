package vn.mn.quanlynhahang.model;

import android.content.Intent;

public class ItemHome {
    private int image;
    private String titleName;
    private Intent intent;
    public ItemHome() {
    }

    public ItemHome(int image, String titleName, Intent intent) {
        this.image = image;
        this.titleName = titleName;
        this.intent = intent;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
