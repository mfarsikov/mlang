import java.lang.RuntimeException
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure


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

        is Expression.Fun0 -> {
            val f = funcs.filterIsInstance<Function.Function0>().single { it.name == expression.name }

            LinkedFun0(
                expression = expression,
                function = f
            )
        }
        is Expression.Fun1 -> {
            val linkedParam = link(expression.param1, clazz, funcs)

            val f = funcs.filterIsInstance<Function.Function1>()
                .singleOrNull() { matches(it.inputTypes, listOf(linkedParam.returnType)) && it.name == expression.name }
                ?: throw RuntimeException("Cannot find function ${expression.name}(${linkedParam.returnType})")

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
                .singleOrNull {
                    it.name == expression.name &&
                            matches(
                                it.inputTypes,
                                listOf(linkedParam1.returnType, linkedParam2.returnType)
                            )
                }
                ?: throw RuntimeException("Cannot find function ${expression.name}(${linkedParam1.returnType}, ${linkedParam2.returnType})")

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
                .singleOrNull() {
                    it.name == expression.name &&
                            matches(
                                it.inputTypes, listOf(
                                    linkedParam1.returnType,
                                    linkedParam2.returnType,
                                    linkedParam3.returnType
                                )
                            )
                }
                ?: throw RuntimeException("Cannot find function ${expression.name}(${linkedParam1.returnType}, ${linkedParam2.returnType}, ${linkedParam3.returnType})")

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

private fun matches(methodArgs: List<KType>, params: List<KType>): Boolean {
    if (methodArgs.size != params.size) return false
    if (methodArgs == params) return true
    return methodArgs.zip(params).all { (methodArg, param) -> methodArg.jvmErasure.isSuperclassOf(param.jvmErasure) }
}

