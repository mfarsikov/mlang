package mlang.linker

import mlang.parser.Expression
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

interface LinkedExpression {
    val returnType: KType
    fun evaluate(c: Any): Any
}

data class LinkedFun(
    val expression: Expression.Function,
    val linkedParams: List<LinkedExpression>,
    val function: KFunction<*>
) : LinkedExpression {
    override val returnType = function.returnType
    override fun evaluate(c: Any): Any {
        val params = linkedParams.map { it.evaluate(c) }.toTypedArray()
        return function.call(*params)!!
    }
}

data class LinkedField(
    val field: Expression.Field,
    override val returnType: KType,
    val extractor: KCallable<*>
) : LinkedExpression {
    override fun evaluate(c: Any): Any {
        return extractor.call(c)!!
    }
}

sealed class LinkedLiteral : LinkedExpression {
    data class LinkedStringLiteral(
        val literal: Expression.Literal.StringLiteral,
        override val returnType: KType = String::class.starProjectedType
    ) : LinkedLiteral() {
        override fun evaluate(c: Any) = literal.value
    }

    data class LinkedNumLiteral(
        val literal: Expression.Literal.NumLiteral,
        override val returnType: KType = Int::class.starProjectedType
    ) : LinkedLiteral() {
        override fun evaluate(c: Any) = literal.value
    }

    data class LinkedBoolLiteral(
        val literal: Expression.Literal.BoolLiteral,
        override val returnType: KType = Boolean::class.starProjectedType
    ) : LinkedLiteral() {
        override fun evaluate(c: Any) = literal.value
    }
}
