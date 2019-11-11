import java.time.LocalDate


fun now() = LocalDate.now()
fun eq(a: Any, b: Any) = a == b
fun date(date: String) = LocalDate.parse(date)
fun not(a: Boolean) = !a
fun <T : Comparable<T>> gt(a: T, b: T) = a > b
fun <T : Comparable<T>> ge(a: T, b: T) = a >= b
fun <T : Comparable<T>> lt(a: T, b: T) = a < b
fun <T : Comparable<T>> le(a: T, b: T) = a <= b
fun or(a: Boolean, b: Boolean) = a || b
fun and(a: Boolean, b: Boolean) = a && b
fun between(date: LocalDate, start: LocalDate, end: LocalDate) = !start.isAfter(date) && !date.isAfter(end)
