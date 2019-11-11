package mlang.parser

sealed class Expression {
    data class Function(val name: String, val params: List<Expression>) : Expression()

    data class Field(val name: String) : Expression()

    sealed class Literal : Expression() {
        data class StringLiteral(val value: String) : Literal()
        data class NumLiteral(val value: Int) : Literal()
        data class BoolLiteral(val value: Boolean) : Literal()
    }
}
