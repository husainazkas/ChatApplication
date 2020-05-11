package com.husainazkas.chatapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserData(
    var uid : String,
    var name: String,
    var email: String,
    var ava: String) : Parcelable {
    constructor():this("","", "", "")
}