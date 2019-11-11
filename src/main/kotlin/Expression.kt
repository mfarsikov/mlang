import java.math.BigDecimal

sealed class Expression {
    data class Fun0(val name: String) : Expression() //TODO
    data class Fun1(val name: String, val param1: Expression) : Expression()
    data class Fun2(val name: String, val param1: Expression, val param2: Expression) : Expression()
    data class Fun3(val name: String, val param1: Expression, val param2: Expression, val param3: Expression) : Expression() //TODO

    data class Field(val name: String) : Expression()

    sealed class Literal : Expression() {
        data class StringLiteral(val value: String) : Literal()
        data class NumLiteral(val value: BigDecimal) : Literal()
        data class BoolLiteral(val value: Boolean) : Literal()
    }
}

