package com.example.pasquale.storytellingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Statistica implements Parcelable{

    private int numCorrette;
    private int numSbagliate;
    private int idAlbum;
    private int idPaziente;

    public Statistica() {
    }

    public Statistica(int numCorrette, int numSbagliate, int idAlbum, int idPaziente) {
        this.numCorrette = numCorrette;
        this.numSbagliate = numSbagliate;
        this.idAlbum = idAlbum;
        this.idPaziente = idPaziente;
    }

    protected Statistica(Parcel in) {
        numCorrette = in.readInt();
        numSbagliate = in.readInt();
        idAlbum = in.readInt();
        idPaziente = in.readInt();
    }

    public static final Creator<Statistica> CREATOR = new Creator<Statistica>() {
        @Override
        public Statistica createFromParcel(Parcel in) {
            return new Statistica(in);
        }

        @Override
        public Statistica[] newArray(int size) {
            return new Statistica[size];
        }
    };

    public int getNumCorrette() {
        return numCorrette;
    }

    public void setNumCorrette(int numCorrette) {
        this.numCorrette = numCorrette;
    }

    public int getNumSbagliate() {
        return numSbagliate;
    }

    public void setNumSbagliate(int numSbagliate) {
        this.numSbagliate = numSbagliate;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public int getIdPaziente() {
        return idPaziente;
    }

    public void setIdPaziente(int idPaziente) {
        this.idPaziente = idPaziente;
    }

    @Override
    public String toString() {
        return "Statistica{" +
                "numCorrette=" + numCorrette +
                ", numSbagliate=" + numSbagliate +
                ", idAlbum=" + idAlbum +
                ", idPaziente=" + idPaziente +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(numCorrette);
        parcel.writeInt(numSbagliate);
        parcel.writeInt(idAlbum);
        parcel.writeInt(idPaziente);
    }
}
