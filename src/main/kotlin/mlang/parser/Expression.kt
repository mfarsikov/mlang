package mlang.parser

sealed class Expression {
    data class Fun0(val name: String) : Expression()
    data class Fun1(val name: String, val param1: Expression) : Expression()
    data class Fun2(val name: String, val param1: Expression, val param2: Expression) : Expression()
    data class Fun3(val name: String, val param1: Expression, val param2: Expression, val param3: Expression) : Expression()

    data class Field(val name: String) : Expression()

    sealed class Literal : Expression() {
        data class StringLiteral(val value: String) : Literal()
        data class NumLiteral(val value: Int) : Literal()
        data class BoolLiteral(val value: Boolean) : Literal()
    }
}

