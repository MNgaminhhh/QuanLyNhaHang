package vn.mn.quanlynhahang.model;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class ItemHome {
    private int image;
    private String titleName;
    private Class<? extends Fragment> fragmentClass;
    public ItemHome() {
    }

    public ItemHome(int image, String titleName, Class<? extends Fragment> fragmentClass) {
        this.image = image;
        this.titleName = titleName;
        this.fragmentClass = fragmentClass;
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

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }
}
