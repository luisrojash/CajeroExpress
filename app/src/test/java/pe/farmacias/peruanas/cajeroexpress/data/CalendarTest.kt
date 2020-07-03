package pe.farmacias.peruanas.cajeroexpress.data

import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import java.util.*

class CalendarTest {

    @Test
    fun testDefaultValues() {
        val cal = Calendar.getInstance()
        assertYMD(cal, Calendar.getInstance())
    }

    // Only Year/Month/Day precision is needed for comparing GardenPlanting Calendar entries
    private fun assertYMD(expectedCal: Calendar, actualCal: Calendar) {
        Assert.assertThat(
            actualCal.get(Calendar.YEAR),
            CoreMatchers.equalTo(expectedCal.get(Calendar.YEAR))
        )
        Assert.assertThat(
            actualCal.get(Calendar.MONTH),
            CoreMatchers.equalTo(expectedCal.get(Calendar.MONTH))
        )
        Assert.assertThat(
            actualCal.get(Calendar.DAY_OF_MONTH),
            CoreMatchers.equalTo(expectedCal.get(Calendar.DAY_OF_MONTH))
        )
    }
}