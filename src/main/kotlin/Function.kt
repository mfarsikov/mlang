import kotlin.reflect.KType


sealed class Function {

    abstract val name: String
    abstract val inputTypes: List<KType>

    class Function0(
        override val name: String,
        override val inputTypes: List<KType>,
        val returnType: KType,
        val f: () -> Any
    ) : Function()

    class Function1(
        override val name: String,
        override val inputTypes: List<KType>,
        val returnType: KType,
        val f: (Any) -> Any
    ) : Function()

    class Function2(
        override val name: String,
        override val inputTypes: List<KType>,
        val returnType: KType,
        val f: (Any, Any) -> Any
    ) : Function()

    class Function3(
        override val name: String,
        override val inputTypes: List<KType>,
        val returnType: KType,
        val f: (Any, Any, Any) -> Any
    ) : Function()
}