package com.aiegroup.todo.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aiegroup.todo.utils.Converter
import kotlinx.android.parcel.Parcelize
import java.util.*
/**
 * .storage model class for task item
 */
@Parcelize
@Entity(tableName = "ToDo")
data class ToDo(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String,
    @ColumnInfo(name = "taskTime")
    var taskTime: String,
    @ColumnInfo(name = "taskName")
    var taskName: String,
    @ColumnInfo(name = "taskCreater")
    var taskCreater: String,
    @TypeConverters(Converter::class)
    @ColumnInfo(name = "taskDate")
    var taskDate: Date): Parcelable




