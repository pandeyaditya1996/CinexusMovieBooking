package com.cmpe.cosmos.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun convertTo12HourFormat(time24: String): String {
    val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val date = sdf24.parse(time24)
    return sdf12.format(date)
}

fun convertTo24HourFormat(time12: String): String {
    val sdf24 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val date = sdf12.parse(time12)
    return sdf24.format(date)
}

fun getFormattedDate(isTomorrow: Boolean): String {
    // Get the current date
    val currentDate = if (isTomorrow) {
        // If tomorrow's date is requested, add one day to the current date
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.time
    } else {
        Date()
    }

    // Define the desired date format
    val dateFormat = SimpleDateFormat("EEE, d MMM", Locale.ENGLISH)

    // Format the date using the specified format
    return dateFormat.format(currentDate)
}

fun getSeatName(seatNumber: Int): String {
    // Calculate row index and column index
    val row = (seatNumber - 1) / 10
    val column = (seatNumber - 1) % 10

    // Convert column index to letter (A-J)
    val rowName = ('A' + row).toString()

    // Seat name format: RowLetter + ColumnNumber
    return "$rowName${column + 1}"

}

fun convertSeatNamesToIndexArray(seatNames: String): IntArray? {
    val seatNameList = seatNames.split(",").map { it.trim() }

    if (seatNameList.any { it.isBlank() }) {
        return null // Invalid input, empty seat name
    }

    val indexArray = IntArray(100) { 1 } // Initialize with 1

    for (seatName in seatNameList) {
        val seatNumber = getSeatNumberFromName(seatName)
        if (seatNumber != null && seatNumber in 1..100) {
            // Set the corresponding element to 0
            indexArray[seatNumber - 1] = 0
        } else {
            return null // Invalid seat name in the input
        }
    }

    return indexArray
}

fun getSeatNumberFromName(seatName: String): Int? {
    val rowLetter = seatName[0]
    val columnNumber = seatName.substring(1).toIntOrNull()

    if (columnNumber != null && columnNumber in 1..10 && rowLetter in 'A'..'J') {
        val rowIndex = rowLetter - 'A'
        return rowIndex * 10 + columnNumber
    } else {
        return null // Invalid seat name format
    }
}


fun convertDateFormat(inputDate: String): String {
    val inputFormat = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    try {
        val date = inputFormat.parse(inputDate)
        val calendar = Calendar.getInstance()
        calendar.time = date ?: Date()

        // Set the year to 2023
        calendar.set(Calendar.YEAR, 2023)

        return outputFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle parsing errors or return a default value if needed
        return ""
    }
}

fun convertTimestampToDate(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val dateTime = LocalDateTime.parse(timestamp, formatter)

    // Format the date as "Friday, 28 July"

    return dateTime.format(DateTimeFormatter.ofPattern("EEE, d MMM", Locale.US))
}

fun convertTimestampToTime(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val dateTime = LocalDateTime.parse(timestamp, formatter)

    // Format the time as "4:00 PM"

    return dateTime.format(DateTimeFormatter.ofPattern("h:mm a", Locale.US))
}

fun isTimestampAfterCurrent(timestamp: String): Boolean {
    // Define the date format for the input timestamp
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    try {
        // Parse the input timestamp
        val inputDate = dateFormat.parse(timestamp)

        // Get the current timestamp
        val currentDate = Date()

        // Compare the two timestamps
        return inputDate != null && inputDate.after(currentDate)
    } catch (e: Exception) {
        // Handle parsing errors
        e.printStackTrace()
        return false
    }
}

fun getNextYearDate(): String {
    val currentDate = LocalDate.now()

    // Get the date one year after
    val oneYearAfter = currentDate.plusYears(1)

    // Format the dates (optional)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return oneYearAfter.format(formatter)
}

fun isDateAfterToday(inputDate: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    try {
        val currentDate = Calendar.getInstance().time
        val inputParsedDate = dateFormat.parse(inputDate)

        val todayCalendar = Calendar.getInstance()
        todayCalendar.time = currentDate

        return inputParsedDate.after(todayCalendar.time)

    } catch (e: Exception) {
        return false
    }
}

fun isDateAfterTomorrow(inputDate: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    try {
        val currentDate = Calendar.getInstance().time
        val inputParsedDate = dateFormat.parse(inputDate)

        val tomorrowCalendar = Calendar.getInstance()
        tomorrowCalendar.time = currentDate
        tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1)

        return inputParsedDate.after(tomorrowCalendar.time)

    } catch (e: Exception) {
        return false
    }
}
