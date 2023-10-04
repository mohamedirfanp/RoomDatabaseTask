package com.example.roomdatabasetask

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDataDao {

    @Insert
    suspend fun d(item:UserModel)

    @Query("SELECT * FROM user_data WHERE blood_group = :requestedBloodGroup")
    suspend fun getUsers(requestedBloodGroup : String): List<UserModel>

    @Query("DELETE FROM user_data WHERE user_name = :requestedUserName")
    suspend fun deleteUser(requestedUserName : String): Unit

}