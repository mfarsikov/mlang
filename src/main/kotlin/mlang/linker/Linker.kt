package mlang.linker

import mlang.parser.Expression
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.isSuperclassOf
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
            extractor = classCtx.members.first { it.name == expression.name },
            returnType = classCtx.members.first { it.name == expression.name }.returnType
        )

        is Expression.Function -> {
            val linkedParams = expression.params.map { link(it, classCtx, functionCtx) }

            LinkedFun(
                expression = expression,
                linkedParams = linkedParams,
                function = findMatching(functionCtx, expression.name, linkedParams.map { it.returnType })
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
    if (methodArgs == params) return true
    return methodArgs.zip(params).all { (methodArg, param) -> methodArg.jvmErasure.isSuperclassOf(param.jvmErasure) }
}
