package uz.itschool.shopping.service

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.itschool.shopping.model.User

class SharedPrefHelper private constructor(context: Context){
    val shared = context.getSharedPreferences("data", 0)
    val edit = shared.edit()
    val gson  = Gson()


    companion object{
        private var instance :SharedPrefHelper? = null
        fun getInstance(context: Context):SharedPrefHelper{
            if (instance == null) instance = SharedPrefHelper(context)
            return instance!!
        }
    }

    fun setUser(user: User){
        val data  = gson.toJson(user)
        edit.putString("user", data)
    }

    fun logOut(){
        edit.putString("user", "")
    }
    fun getUser():User?{
        var data = shared.getString("user", "")
        if (data == "") return null
        val typeToken = object : TypeToken<User> {}.type
        return gson.fromJson(data, typeToken)
    }

}