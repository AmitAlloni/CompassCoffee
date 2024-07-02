package com.example.coffeecompass.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coffee_shops")
data class LocalCoffeeShop(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val rate: Float,
    val imageUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeFloat(rate)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocalCoffeeShop> {
        override fun createFromParcel(parcel: Parcel): LocalCoffeeShop {
            return LocalCoffeeShop(parcel)
        }

        override fun newArray(size: Int): Array<LocalCoffeeShop?> {
            return arrayOfNulls(size)
        }
    }
}
