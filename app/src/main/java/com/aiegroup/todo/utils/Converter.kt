package com.aiegroup.todo.utils

import androidx.databinding.BindingAdapter
import androidx.room.TypeConverter
import com.google.android.material.button.MaterialButton
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
/**
 * .date time converter class used as type converter for insert date field in room database.
 */
class Converter {

    var dfDDMMYYHHMM: DateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault())

    init {
        dfDDMMYYHHMM.timeZone = TimeZone.getTimeZone("GMT")
    }

    companion object {
        var dfDDMMYY: DateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        var dfHHMM: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        var dfDDMMYYHHMM: DateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault())


        @JvmStatic
        fun dateToDDMMMYYYY(value: Date): String {
            return if (value != null) {
                dfDDMMYY.format(value)
            } else ({
                null
            }).toString()
        }
        @JvmStatic
        fun dateToHHMM(value: Date): String {
            return if (value != null) {
                dfHHMM.format(value)
            } else ({
                null
            }).toString()
        }
        @JvmStatic
        fun ToDDMMMYYYYHHMM(value: Date): String {
            return if (value != null) {
                dfDDMMYYHHMM.format(value)
            } else ({
                null
            }).toString()
        }

    }

    @TypeConverter
    fun timeToDate(value: String?): Date? {
        return if (value != null) {
            try {
                return dfDDMMYYHHMM.parse(value)
            } catch (e: ParseException) {
//                Log.e(TAG, e.getMessage())
            }
            null
        } else {
            null
        }
    }

    @TypeConverter
    fun dateToTime(value: Date?): String? {
        return if (value != null) {
            dfDDMMYYHHMM.format(value)
        } else {
            null
        }
    }
}