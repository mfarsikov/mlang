import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.time.LocalDate
import kotlin.reflect.full.starProjectedType

fun main() {

    val parser = """
        true = getTrue("")
    """.trimIndent()
        .let { CharStreams.fromString(it) }
        .let { LangLexer(it) }
        .let { CommonTokenStream(it) }
        .let { LangParser(it) }

    // println(parser.expression().toStringTree(parser))

    val graph = Visitor().visit(parser.expression())

    println(graph)

    val todayFunction = Function.Function1(
        name = "getTrue",
        inputTypes = listOf(String::class.starProjectedType),
        returnType = Boolean::class.starProjectedType,
        f = { true }
    )

    val eq = Function.Function2(
        name = "EQ",
        inputTypes = listOf(Boolean::class.starProjectedType, Boolean::class.starProjectedType),
        returnType = Boolean::class.starProjectedType,
        f = { a, b -> a == b }
    )

    val linkedGraph = link(graph, String::class, listOf(todayFunction, eq))
    val result = linkedGraph.evaluate("")
    println(result)
}


