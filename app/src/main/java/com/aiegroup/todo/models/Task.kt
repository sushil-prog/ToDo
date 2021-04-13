package com.aiegroup.todo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: String,
    @ColumnInfo(name = "taskName")
    val taskName: String,
    @ColumnInfo(name = "taskDescription")
    val taskDescription: String)
