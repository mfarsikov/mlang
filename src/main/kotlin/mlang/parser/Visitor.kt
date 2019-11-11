package mlang.parser

import LangBaseVisitor
import LangParser

class Visitor : LangBaseVisitor<Expression>() {

    override fun visitLiteral_exp(ctx: LangParser.Literal_expContext): Expression.Literal {
        return when {
            ctx.literal().STRING() != null -> Expression.Literal.StringLiteral(ctx.literal().text.trim('"'))
            ctx.literal().NUM() != null -> Expression.Literal.NumLiteral(ctx.literal().text.toInt())
            ctx.literal().BOOL() != null -> Expression.Literal.BoolLiteral(ctx.literal().text!!.toBoolean())
            else -> throw RuntimeException()
        }
    }

    override fun visitBool_exp(ctx: LangParser.Bool_expContext): Expression {
        return when {
            ctx.boolOperator().AND() != null -> Expression.Fun("and", ctx.expression().map { visit(it) })
            ctx.boolOperator().OR() != null -> Expression.Fun("or", ctx.expression().map { visit(it) })
            else -> throw RuntimeException()
        }
    }

    override fun visitOperator_exp(ctx: LangParser.Operator_expContext): Expression {
        return when {
            ctx.operator().EQ() != null -> Expression.Fun("eq", ctx.expression().map { visit(it) })
            ctx.operator().GE() != null -> Expression.Fun("ge", ctx.expression().map { visit(it) })
            ctx.operator().GT() != null -> Expression.Fun("gt", ctx.expression().map { visit(it) })
            ctx.operator().LE() != null -> Expression.Fun("le", ctx.expression().map { visit(it) })
            ctx.operator().LT() != null -> Expression.Fun("lt", ctx.expression().map { visit(it) })
            ctx.operator().PLUS() != null -> Expression.Fun("plus", ctx.expression().map { visit(it) })
            ctx.operator().MINUS() != null -> Expression.Fun("minus", ctx.expression().map { visit(it) })
            else -> throw RuntimeException()
        }
    }

    override fun visitField_exp(ctx: LangParser.Field_expContext): Expression.Field {
        return Expression.Field(ctx.field().NAME().text)
    }

    override fun visitFunction_exp(ctx: LangParser.Function_expContext): Expression {
        return Expression.Fun(
            ctx.function().NAME().text,
            ctx.function().expression().map { visit(it) }
        )
    }

    override fun visitParentheses_exp(ctx: LangParser.Parentheses_expContext): Expression {
        return visit(ctx.expression())
    }

    override fun visitNot_exp(ctx: LangParser.Not_expContext): Expression {
        return Expression.Fun("not", listOf(visit(ctx.expression())))
    }
}
