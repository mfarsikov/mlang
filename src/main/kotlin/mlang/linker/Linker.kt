package mlang.linker

import mlang.parser.Expression
import kotlin.RuntimeException
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure


fun link(expression: Expression, classCtx: KClass<*>, functionCtx: List<KFunction<*>>): LinkedExpression {

    return when (expression) {
        is Expression.Literal -> when (expression) {
            is Expression.Literal.BoolLiteral -> LinkedLiteral.LinkedBoolLiteral(literal = expression)
            is Expression.Literal.NumLiteral -> LinkedLiteral.LinkedNumLiteral(literal = expression)
            is Expression.Literal.StringLiteral -> LinkedLiteral.LinkedStringLiteral(literal = expression)
        }

        is Expression.Field -> LinkedField(
            field = expression,
            returnType = classCtx.members.single { it.name == expression.name }.returnType,
            extractor = classCtx.members.single { it.name == expression.name }
        )

        is Expression.Fun0 -> LinkedFun0(
            expression = expression,
            function = findMatching(functionCtx, expression.name, emptyList())
        )

        is Expression.Fun1 -> {
            val linkedParam = link(expression.param1, classCtx, functionCtx)

            LinkedFun1(
                expression = expression,
                linkedParam1 = linkedParam,
                function = findMatching(functionCtx, expression.name, listOf(linkedParam.returnType))
            )
        }

        is Expression.Fun2 -> {
            val linkedParam1 = link(expression.param1, classCtx, functionCtx)
            val linkedParam2 = link(expression.param2, classCtx, functionCtx)

            LinkedFun2(
                expression = expression,
                linkedParam1 = linkedParam1,
                linkedParam2 = linkedParam2,
                function = findMatching(
                    functionCtx,
                    expression.name,
                    listOf(linkedParam1.returnType, linkedParam2.returnType)
                )
            )
        }

        is Expression.Fun3 -> {
            val linkedParam1 = link(expression.param1, classCtx, functionCtx)
            val linkedParam2 = link(expression.param2, classCtx, functionCtx)
            val linkedParam3 = link(expression.param3, classCtx, functionCtx)

            LinkedFun3(
                expression = expression,
                linkedParam1 = linkedParam1,
                linkedParam2 = linkedParam2,
                linkedParam3 = linkedParam3,
                function = findMatching(
                    functionCtx,
                    expression.name,
                    listOf(linkedParam1.returnType, linkedParam2.returnType, linkedParam3.returnType)
                )
            )
        }
    }
}

private fun findMatching(functionCtx: List<KFunction<*>>, name: String, params: List<KType>): KFunction<*> {
    return functionCtx
        .singleOrNull {
            it.name == name
                    && it.parameters.size == params.size
                    && matches(it.parameters.map { it.type }, params)
        }
        ?: throw RuntimeException("Cannot find function ${name}(${params.joinToString()})")

}

private fun matches(methodArgs: List<KType>, params: List<KType>): Boolean {
    if (methodArgs.size != params.size) return false
    if (methodArgs == params) return true
    return methodArgs.zip(params).all { (methodArg, param) -> methodArg.jvmErasure.isSuperclassOf(param.jvmErasure) }
}

