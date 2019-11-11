import mu.KLogging
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.lang.RuntimeException
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinFunction

object Parser:KLogging() {
    fun parse(
        expression: String,
        context: KClass<*>,
        functionsFile: String
    ): LinkedExpression {
        val parser = expression
            .let { CharStreams.fromString(it) }
            .let { LangLexer(it) }
            .let { CommonTokenStream(it) }
            .let { LangParser(it) }

        val graph = Visitor().visit(parser.expression())
        logger.debug { graph.toString() }
        return link(graph, context, loadFunctionFromFile(functionsFile))
    }


    private fun toFunction(f: KFunction<*>): Function {
        return when (f.parameters.size) {
            0 -> Function.Function0(
                f.name,
                inputTypes = f.parameters.map { it.type },
                returnType = f.returnType,
                f = { f.call()!! }
            )
            1 -> Function.Function1(
                f.name,
                inputTypes = f.parameters.map { it.type },
                returnType = f.returnType,
                f = { f.call(it)!! }
            )
            2 -> Function.Function2(
                f.name,
                inputTypes = f.parameters.map { it.type },
                returnType = f.returnType,
                f = { a, b -> f.call(a, b)!! }
            )
            3 -> Function.Function3(
                f.name,
                inputTypes = f.parameters.map { it.type },
                returnType = f.returnType,
                f = { a, b, c -> f.call(a, b, c)!! }
            )
            else -> throw RuntimeException("Supported only 0-3 parameters, but was: ${f.parameters.size}, ${f.name}")
        }
    }

    private fun loadFunctionFromFile(file: String): List<Function> {
        return ::loadFunctionFromFile
            .javaMethod!!
            .declaringClass
            .classLoader
            .loadClass("${file}Kt")
            .methods
            .filter { Modifier.isStatic(it.modifiers) }
            .map { it.kotlinFunction }
            .filterNotNull()
            .map { toFunction(it) }
    }
}