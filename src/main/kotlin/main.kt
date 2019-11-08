import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.RuleNode

fun main() {


    """
        issueDate == date("2001-01-01") and
        expirationDate < today() + days(3) and
        status in ["a", "b", "c"] and
        (owner == "vasya" or owner == "petya")
    """.trimIndent()
    val parser = " x.a = true & (false | true)"
        .let { CharStreams.fromString(it) }
        .let { LogicLexer(it) }
        .let { CommonTokenStream(it) }
        .let { LogicParser(it) }

    println(V().visit(parser.expression()))
}

data class Expression(
    val type: String,
    val value: String? = null,
    val expression1: Expression? = null,
    val expression2: Expression? = null
)

class V : LogicBaseVisitor<Expression>() {

    override fun visitExpression_const(ctx: LogicParser.Expression_constContext): Expression {
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
    }
}
