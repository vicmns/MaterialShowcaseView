package com.vicmns.library;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vicmns on 2/3/2015.
 */
public class ShowcaseViewModel implements Parcelable {
    private String mTitle, mDescription;
    private int backgroundColor;
    private boolean isButtonShown;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isButtonShown() {
        return isButtonShown;
    }

    public void setButtonShown(boolean isButtonShown) {
        this.isButtonShown = isButtonShown;
    }

    protected ShowcaseViewModel(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        backgroundColor = in.readInt();
        isButtonShown = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeInt(backgroundColor);
        dest.writeByte((byte) (isButtonShown ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ShowcaseViewModel> CREATOR = new Parcelable.Creator<ShowcaseViewModel>() {
        @Override
        public ShowcaseViewModel createFromParcel(Parcel in) {
            return new ShowcaseViewModel(in);
        }

        @Override
        public ShowcaseViewModel[] newArray(int size) {
            return new ShowcaseViewModel[size];
        }
    };
}
