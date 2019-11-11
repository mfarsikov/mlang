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
            ctx.boolOperator().AND() != null -> Expression.Fun(
                "and",
                listOf(
                    visit(ctx.expression(0)),
                    visit(ctx.expression(1))
                )
            )
            ctx.boolOperator().OR() != null -> Expression.Fun(
                "or",
                listOf(
                    visit(ctx.expression(0)),
                    visit(ctx.expression(1))
                )
            )
            else -> throw RuntimeException()
        }
    }

    override fun visitOperator_exp(ctx: LangParser.Operator_expContext): Expression {
        return when {
            ctx.operator().EQ() != null -> Expression.Fun(
                "eq",
                listOf(
                    visit(ctx.expression(0)),
                    visit(ctx.expression(1))
                )
            )
            ctx.operator().GE() != null -> Expression.Fun(
                "ge",
                listOf(
                    visit(ctx.expression(0)),
                    visit(ctx.expression(1))
                )
            )
            ctx.operator().GT() != null -> Expression.Fun(
                "gt",
                listOf(
                    visit(ctx.expression(0)),
                    visit(ctx.expression(1))
                )
            )
            ctx.operator().LE() != null -> Expression.Fun(
                "le",
                listOf(
                    visit(ctx.expression(0)),
                    visit(ctx.expression(1))
                )
            )
            ctx.operator().LT() != null -> Expression.Fun(
                "lt",
                listOf(
                    visit(ctx.expression(0)),
                    visit(ctx.expression(1))
                )
            )
            else -> throw RuntimeException()
        }
    }

    override fun visitField_exp(ctx: LangParser.Field_expContext): Expression.Field {
        return Expression.Field(ctx.field().NAME().text)
    }

    override fun visitFunction0_exp(ctx: LangParser.Function0_expContext): Expression {
        return Expression.Fun(ctx.function0().NAME().text, listOf())
    }

    override fun visitFunction1_exp(ctx: LangParser.Function1_expContext): Expression {
        return Expression.Fun(ctx.function1().NAME().text, listOf(visit(ctx.function1().expression())))
    }

    override fun visitFunction2_exp(ctx: LangParser.Function2_expContext): Expression {
        return Expression.Fun(
            ctx.function2().NAME().text,
            listOf(
                visit(ctx.function2().expression(0)),
                visit(ctx.function2().expression(1))
            )
        )
    }

    override fun visitFunction3_exp(ctx: LangParser.Function3_expContext): Expression {
        return Expression.Fun(
            ctx.function3().NAME().text,
            listOf(
                visit(ctx.function3().expression(0)),
                visit(ctx.function3().expression(1)),
                visit(ctx.function3().expression(2))
            )
        )
    }

    override fun visitParentheses_exp(ctx: LangParser.Parentheses_expContext): Expression {
        return visit(ctx.expression())
    }

    override fun visitNot_exp(ctx: LangParser.Not_expContext): Expression {
        return Expression.Fun("not", listOf(visit(ctx.expression())))
    }
}
