package com.example.roomdatabasetask

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_data")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,

    @ColumnInfo(name = "user_name")
    var userName: String,

    @ColumnInfo(name = "blood_group")
    var bloodGroup: String,

    @ColumnInfo(name = "isDeleted")
    var isDeleted: Boolean = false
)