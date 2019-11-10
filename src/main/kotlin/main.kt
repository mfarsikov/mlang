import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.math.BigDecimal

fun main() {

    val parser = """
        true & "trs" & 1.03
    """.trimIndent()
        .let { CharStreams.fromString(it) }
        .let { LangLexer(it) }
        .let { CommonTokenStream(it) }
        .let { LangParser(it) }

    // println(parser.expression().toStringTree(parser))

    println(V().visit(parser.expression()))
}


sealed class Expression {
    data class Exp(val left: Expression, val  op: Operator, val right: Expression):Expression()
    sealed class Literal : Expression() {
        data class StringLiteral(val value: String) : Literal()
        data class NumLiteral(val value: BigDecimal) : Literal()
        data class BoolLiteral(val value: Boolean) : Literal()
    }

    sealed class Operator : Expression() {
        enum class BoolOpType { AND, OR }
        data class BoolOp(val type: BoolOpType) : Operator()
    }
}


class V : LangBaseVisitor<Expression>() {



    override fun visitLiteral(ctx: LangParser.LiteralContext?): Expression.Literal {
        return when {
            ctx?.STRING() != null -> Expression.Literal.StringLiteral(ctx.text)
            ctx?.NUM() != null -> Expression.Literal.NumLiteral(ctx.text.toBigDecimal())
            ctx?.BOOL() != null -> Expression.Literal.BoolLiteral(ctx.text!!.toBoolean())
            else -> throw RuntimeException()
        }
    }

    override fun visitExp_operator(ctx: LangParser.Exp_operatorContext): Expression.Exp {
        val left = visit(ctx.expression(0))
        val op = visit(ctx.operator()) as Expression.Operator
        val right = visit(ctx.expression(1))
        return Expression.Exp(left, op, right)
    }

    override fun visitOperator(ctx: LangParser.OperatorContext?): Expression.Operator {
        return when {
            ctx?.AND() != null -> Expression.Operator.BoolOp(Expression.Operator.BoolOpType.AND)
            else -> throw RuntimeException()
        }
    }

    /* override fun visitExpression_const(ctx: LogicParser.Expression_constContext): Expression {
         return Expression("const", value = ctx.text)
     }

     override fun visitExpression_and(ctx: LogicParser.Expression_andContext): Expression {
         return Expression(
             type = "and",
             expression1 = visit(ctx.expression(0)),
             expression2 = visit(ctx.expression(1))
         )
     }

     override fun visitExpression_or(ctx: LogicParser.Expression_orContext): Expression {
         return Expression(
             type = "or",
             expression1 = visit(ctx.expression(0)),
             expression2 = visit(ctx.expression(1))
         )
     }

     override fun visitExpression_group(ctx: LogicParser.Expression_groupContext): Expression {
         return Expression(
             type = "group",
             expression1 = visit(ctx.expression())
         )
     }*/
}

