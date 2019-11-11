package mlang

import LangLexer
import LangParser
import mlang.linker.LinkedExpression
import mlang.linker.link
import mlang.parser.Visitor
import mu.KLogging
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinFunction

object Lang : KLogging() {
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

    private fun loadFunctionFromFile(file: String): List<KFunction<*>> {
        return Lang::loadFunctionFromFile
            .javaMethod!!
            .declaringClass
            .classLoader
            .loadClass("${file}Kt")
            .methods
            .filter { Modifier.isStatic(it.modifiers) }
            .map { it.kotlinFunction }
            .filterNotNull()
    }
}
