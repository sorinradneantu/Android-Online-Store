package com.example.android_online_store.project.models

class User(val firstName:String = "", val lastName:String = "", val email:String = "", val phoneNumber:String = "", val address:String = "",val id:String = "") {

    override fun toString(): String {
        return "User(firstName='$firstName', lastName='$lastName', email='$email', phoneNumber='$phoneNumber', address='$address', id='$id')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (phoneNumber != other.phoneNumber) return false
        if (address != other.address) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }


}