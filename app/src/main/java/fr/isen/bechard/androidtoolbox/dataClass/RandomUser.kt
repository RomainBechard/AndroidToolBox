package fr.isen.bechard.androidtoolbox.dataClass

import com.google.gson.Gson

class RandomUser {

    var gender: String? = null
    var name: Name? = null
    var location: Location? = null
    var email: String? = null
    var login: Login? = null
    var dob: String? = null
    var registered: String? = null
    var phone: String? = null
    var cell: String? = null
    var id: Id? = null
    var picture: Picture? = null
    var nat: String? = null


    override fun toString(): String {
        return "RandomUser{" +
                "gender='" + gender + '\'' +
                ", name=" + name +
                ", location=" + location +
                ", email='" + email + '\'' +
                ", login=" + login +
                ", dob='" + dob + '\'' +
                ", registered='" + registered + '\'' +
                ", phone='" + phone + '\'' +
                ", cell='" + cell + '\'' +
                ", id=" + id +
                ", picture=" + picture +
                ", nat='" + nat + '\'' +
                '}'
    }

    class Name {
        var first: String? = null
        var last: String? = null
        override fun toString(): String {
            return g.toJson(this)
        }
        fun fullName(): String {
            return "$first $last"
        }
    }

    class Location {
        var city: String? = null
        var state: String? = null
        var postcode: String? = null
        override fun toString(): String {
            return g.toJson(this)
        }
        fun address(): String{
            return "$postcode $city, $state"
        }
    }

    class Login {
        var username: String? = null
        var password: String? = null
        var salt: String? = null
        var md5: String? = null
        var sha1: String? = null
        var sha256: String? = null
        override fun toString(): String {
            return g.toJson(this)
        }
    }

    class Id {
        var name: String? = null
        var value: String? = null
        override fun toString(): String {
            return g.toJson(this)
        }
    }

    class Picture {
        var large: String? = null
        var medium: String? = null
        var thumbnail: String? = null
        override fun toString(): String {
            return g.toJson(this)
        }
    }

    companion object {
        var g = Gson()
    }
}