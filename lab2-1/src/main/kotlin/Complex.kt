import kotlin.math.sqrt

data class Complex(val re: Double, val im: Double)

val Double.j: Complex
    get() = Complex(0.0, this)

operator fun Double.times(c: Complex): Complex {
    return Complex(this * c.re, this * c.im)
}

operator fun Double.minus(c: Complex): Complex {
    return Complex(this - c.re, - c.im)
}

operator fun Double.plus(c: Complex): Complex {
    return Complex(this + c.re, c.im)
}

operator fun Complex.plus(c: Complex): Complex {
    return Complex(re + c.re, im + c.im)
}

fun Complex.abs() = sqrt(re * re + im * im)
