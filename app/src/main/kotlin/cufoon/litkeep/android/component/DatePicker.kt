package cufoon.litkeep.android.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

data class DateItem(
    val year: Int, val month: Int, val day: Int
) {
    override fun toString(): String =
        "$year-${"%02d".format(month + 1)}-${day.toString().padStart(2, '0')}"
}

@Composable
fun rememberDatePickerDialog(
    date: Calendar
): Pair<DateItem, () -> Unit> {
    val lc = LocalContext.current
    val year = date[Calendar.YEAR]
    val month = date[Calendar.MONTH]
    val day = date[Calendar.DAY_OF_MONTH]
    var currentDate by remember {
        mutableStateOf(DateItem(year, month, day))
    }

    val datePickerDialog by rememberUpdatedState(DatePickerDialog(lc, { _, y, m, d ->
        currentDate = DateItem(y, m, d)
    }, year, month, day))

    val open = {
        datePickerDialog.show()
    }

    return Pair(currentDate, open)
}

data class TimeItem(
    val hour: Int, val minute: Int
) {
    override fun toString(): String = "${"%02d".format(hour)}:${minute.toString().padStart(2, '0')}"
}

@Composable
fun rememberTimePickerDialog(
    date: Calendar
): Pair<TimeItem, () -> Unit> {
    val lc = LocalContext.current
    val hour = date[Calendar.HOUR_OF_DAY]
    val minute = date[Calendar.MINUTE]
    var currentTime by remember {
        mutableStateOf(TimeItem(hour, minute))
    }

    val datePickerDialog by rememberUpdatedState(TimePickerDialog(lc, { _, h, m ->
        currentTime = TimeItem(h, m)
    }, hour, minute, true))

    val open = {
        datePickerDialog.show()
    }

    return Pair(currentTime, open)
}
