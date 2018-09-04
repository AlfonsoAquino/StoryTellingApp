package com.example.pasquale.storytellingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Vignetta implements Parcelable{

    private int idAlbum;
    private String path;
    private int ordine;
    private int ordineProvvisorio;
    private int audio;

    public Vignetta() {
    }

    public Vignetta(int idAlbum, String path, int ordine) {
        this.idAlbum = idAlbum;
        this.path = path;
        this.ordine = ordine;
    }

    protected Vignetta(Parcel in) {
        idAlbum = in.readInt();
        path = in.readString();
        ordine = in.readInt();
    }

    public static final Creator<Vignetta> CREATOR = new Creator<Vignetta>() {
        @Override
        public Vignetta createFromParcel(Parcel in) {
            return new Vignetta(in);
        }

        @Override
        public Vignetta[] newArray(int size) {
            return new Vignetta[size];
        }
    };

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    public int getOrdineProvvisorio() {
        return ordineProvvisorio;
    }

    public void setOrdineProvvisorio(int ordineProvvisorio) {
        this.ordineProvvisorio = ordineProvvisorio;
    }

    @Override
    public String toString() {
        return "Vignetta{" +
                "idAlbum=" + idAlbum +
                ", path='" + path + '\'' +
                ", ordine=" + ordine +
                ", ordineProvvisorio=" + ordineProvvisorio +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idAlbum);
        parcel.writeString(path);
        parcel.writeInt(ordine);
    }
}
