import java.lang.RuntimeException
import java.math.BigDecimal
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType



fun link(expression: Expression, clazz: KClass<*>, funcs: List<Function>): LinkedExpression {

    return when (expression) {
        is Expression.Literal -> when (expression) {
            is Expression.Literal.BoolLiteral -> LinkedLiteral.LinkedBoolLiteral(literal = expression)
            is Expression.Literal.NumLiteral -> LinkedLiteral.LinkedNumLiteral(literal = expression)
            is Expression.Literal.StringLiteral -> LinkedLiteral.LinkedStringLiteral(literal = expression)
        }

        is Expression.Field -> {
            LinkedField(
                field = expression,
                returnType = clazz.members.single { it.name == expression.name }.returnType,
                extractor = clazz.members.single { it.name == expression.name }
            )
        }

        is Expression.Fun0 ->{
            val f = funcs.filterIsInstance<Function.Function0>().single { it.name == expression.name }

            LinkedFun0(
                expression = expression,
                function = f
            )
        }
        is Expression.Fun1 -> {
            val linkedParam = link(expression.param1, clazz, funcs)

            val f = funcs.filterIsInstance<Function.Function1>()
                    .single { it.inputTypes == listOf(linkedParam.returnType) && it.name == expression.name }

            LinkedFun1(
                expression = expression,
                linkedParam1 = linkedParam,
                function = f
            )
        }

        is Expression.Fun2 -> {
            val linkedParam1 = link(expression.param1, clazz, funcs)
            val linkedParam2 = link(expression.param2, clazz, funcs)

            val f = funcs.filterIsInstance<Function.Function2>()
                .singleOrNull { it.inputTypes == listOf(linkedParam1.returnType, linkedParam2.returnType) && it.name == expression.name }
                ?: throw RuntimeException("Cannot find function ${expression.name}(${linkedParam1.returnType}, ${linkedParam2.returnType} ")

            LinkedFun2(
                expression = expression,
                linkedParam1 = linkedParam1,
                linkedParam2 = linkedParam2,
                function = f
            )
        }

        is Expression.Fun3 -> {
            val linkedParam1 = link(expression.param1, clazz, funcs)
            val linkedParam2 = link(expression.param2, clazz, funcs)
            val linkedParam3 = link(expression.param3, clazz, funcs)

            val f = funcs.filterIsInstance<Function.Function3>()
                .single { it.inputTypes == listOf(linkedParam1.returnType, linkedParam2.returnType, linkedParam3.returnType) && it.name == expression.name }

            LinkedFun3(
                expression = expression,
                linkedParam1 = linkedParam1,
                linkedParam2 = linkedParam2,
                linkedParam3 = linkedParam3,
                function = f
            )
        }
    }
}

