package com.aiegroup.todo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDo")
data class ToDo(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String,
    @ColumnInfo(name = "taskName")
    var taskName: String,
    @ColumnInfo(name = "taskCreater")
    var taskCreater: String,
    @ColumnInfo(name = "taskDescription")
    var taskDescription: String)




