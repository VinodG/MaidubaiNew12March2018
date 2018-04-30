package com.winit.maidubai.dataobject;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public class GalleryDO implements AsymmetricItem {

  private int columnSpan;
  private int rowSpan;
  private int position;
  public String image;
  public String url;
  public String Type="";
  public String Path="";
  public int GalleryId;

  public GalleryDO() {
    this(1, 1, 0);
  }

  public GalleryDO(int columnSpan, int rowSpan, int position) {
    this.columnSpan = columnSpan;
    this.rowSpan = rowSpan;
    this.position = position;
  }

  public GalleryDO(Parcel in) {
    readFromParcel(in);
  }

  @Override public int getColumnSpan() {
    return columnSpan;
  }

  @Override public int getRowSpan() {
    return rowSpan;
  }

  public int getPosition() {
    return position;
  }

  public void setColumnSpan(int columnSpan) {
    this.columnSpan = columnSpan;
  }

  public void setRowSpan(int rowSpan) {
    this.rowSpan = rowSpan;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override public String toString() {
    return String.format("%s: %sx%s", position, rowSpan, columnSpan);
  }

  @Override public int describeContents() {
    return 0;
  }

  private void readFromParcel(Parcel in) {
    columnSpan = in.readInt();
    rowSpan = in.readInt();
    position = in.readInt();
  }

  @Override public void writeToParcel(@NonNull Parcel dest, int flags) {
    dest.writeInt(columnSpan);
    dest.writeInt(rowSpan);
    dest.writeInt(position);
  }

  /* Parcelable interface implementation */
  public static final Creator<GalleryDO> CREATOR = new Creator<GalleryDO>() {

    @Override public GalleryDO createFromParcel(@NonNull Parcel in) {
      return new GalleryDO(in);
    }

    @Override @NonNull
    public GalleryDO[] newArray(int size) {
      return new GalleryDO[size];
    }
  };
}
