package mlang

import java.time.LocalDate

fun main() {
    val compiledGraph = Lang.compile(
        expression = """  amount < 100 & expirationDate > today() + days(5)  """,
        context = Invoice::class,
        functionsFile = "mlang.Functions"
    )

    val result = compiledGraph.evaluate(Invoice(amount = 10, expirationDate = LocalDate.parse("2019-12-31")))
    println(result)
}

data class Invoice(
    val amount: Int,
    val expirationDate: LocalDate
)
