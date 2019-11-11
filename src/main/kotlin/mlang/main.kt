package mlang

fun main() {
    val linkedGraph = Lang.parse(
        expression = """ !(value >= 3 & between(today(), date("2019-11-11"), date("2019-11-11"))) """,
        context = Invoice::class,
        functionsFile = "mlang.Functions"
    )
    val result = linkedGraph.evaluate(Invoice())
    println(result)
}

data class Invoice(
    val value: Int = 10
)
