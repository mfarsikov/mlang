fun main() {
    val linkedGraph = parse(
        """
        value == 10
    """,
        Invoice::class,
        "Functions"
    )
    val result = linkedGraph.evaluate(Invoice())
    println(result)
}


data class Invoice(
    val value: Int = 10
)

