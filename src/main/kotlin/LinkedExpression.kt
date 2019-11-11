import java.math.BigDecimal
import kotlin.reflect.KCallable
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

interface LinkedExpression {
    val returnType: KType
    fun evaluate(c: Any): Any
}

data class LinkedFun0(
    val expression: Expression.Fun0,
    val function: Function.Function0
) : LinkedExpression {
    override val returnType = function.returnType
    override fun evaluate(c: Any): Any {
        return function.f()
    }
}

data class LinkedFun1(
    val expression: Expression.Fun1,
    val linkedParam1: LinkedExpression,
    val function: Function.Function1
) : LinkedExpression {

    override val returnType = function.returnType

    override fun evaluate(c: Any): Any {
        val param1 = linkedParam1.evaluate(c)
        return function.f(param1)
    }
}

data class LinkedFun2(
    val expression: Expression.Fun2,
    val linkedParam1: LinkedExpression,
    val linkedParam2: LinkedExpression,
    val function: Function.Function2
) : LinkedExpression {
    override val returnType = function.returnType
    override fun evaluate(c: Any): Any {
        val param1 = linkedParam1.evaluate(c)
        val param2 = linkedParam2.evaluate(c)
        return function.f(param1, param2)
    }
}

data class LinkedFun3(
    val expression: Expression.Fun3,
    val linkedParam1: LinkedExpression,
    val linkedParam2: LinkedExpression,
    val linkedParam3: LinkedExpression,
    val function: Function.Function3
) : LinkedExpression {
    override val returnType = function.returnType

    override fun evaluate(c: Any): Any {
        val param1 = linkedParam1.evaluate(c)
        val param2 = linkedParam2.evaluate(c)
        val param3 = linkedParam3.evaluate(c)
        return function.f(param1, param2, param3)
    }
}

data class LinkedField(
    val field: Expression.Field,
    override val returnType: KType,
    val extractor: KCallable<*>
) : LinkedExpression {
    override fun evaluate(c: Any): Any {
        return extractor.call(c)!! //TODO !!
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
        override val returnType: KType = BigDecimal::class.starProjectedType
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