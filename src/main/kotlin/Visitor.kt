
class Visitor : LangBaseVisitor<Expression>() {

    override fun visitLiteral_exp(ctx: LangParser.Literal_expContext): Expression.Literal {
        return when {
            ctx.literal().STRING() != null -> Expression.Literal.StringLiteral(ctx.literal().text)
            ctx.literal().NUM() != null -> Expression.Literal.NumLiteral(ctx.literal().text.toBigDecimal())
            ctx.literal().BOOL() != null -> Expression.Literal.BoolLiteral(ctx.literal().text!!.toBoolean())
            else -> throw RuntimeException()
        }
    }

    override fun visitBool_exp(ctx: LangParser.Bool_expContext): Expression {
        return when {
            ctx.boolOperator().AND() != null -> Expression.Fun2(
                "AND",
                visit(ctx.expression(0)),
                visit(ctx.expression(1))
            )
            ctx.boolOperator().OR() != null -> Expression.Fun2(
                "OR",
                visit(ctx.expression(0)),
                visit(ctx.expression(1))
            )
            else -> throw RuntimeException()
        }
    }

    override fun visitOperator_exp(ctx: LangParser.Operator_expContext): Expression {
        return when {
            ctx.operator().EQ() != null -> Expression.Fun2(
                "EQ",
                visit(ctx.expression(0)),
                visit(ctx.expression(1))
            )
            ctx.operator().GE() != null -> Expression.Fun2(
                "GE",
                visit(ctx.expression(0)),
                visit(ctx.expression(1))
            )
            ctx.operator().GT() != null -> Expression.Fun2(
                "GT",
                visit(ctx.expression(0)),
                visit(ctx.expression(1))
            )
            ctx.operator().LE() != null -> Expression.Fun2(
                "LE",
                visit(ctx.expression(0)),
                visit(ctx.expression(1))
            )
            ctx.operator().LT() != null -> Expression.Fun2(
                "LT",
                visit(ctx.expression(0)),
                visit(ctx.expression(1))
            )
            else -> throw RuntimeException()
        }
    }

    override fun visitField_exp(ctx: LangParser.Field_expContext): Expression.Field {
        return Expression.Field(ctx.field().NAME().text)
    }

    override fun visitFunction_exp(ctx: LangParser.Function_expContext): Expression {
        return Expression.Fun1(ctx.function().NAME().text, visit(ctx.function().expression()))
    }

    override fun visitParentheses_exp(ctx: LangParser.Parentheses_expContext): Expression {
        return visit(ctx.expression())
    }

    override fun visitNot_exp(ctx: LangParser.Not_expContext): Expression {
        return Expression.Fun1("NOT", visit(ctx.expression()))
    }
}
