package org.projects.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viliusvig on 24/05/2017.
 */

public class Product implements Parcelable {
    private String name;
    private int quantity;

    // CTOR
    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // CTOR Parcable
    public Product(Parcel in) {
        this.name = in.readString();
        this.quantity = in.readInt();
    }

    // CTOR Firebase
    public Product() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Make a nice toString to incorporate Product name and quantity
    @Override
    public String toString() {
        return quantity + " " + name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
    }

    public final static Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
