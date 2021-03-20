import kotlin.math.sqrt

data class Complex(val re: Double, val im: Double)

operator fun Complex.times(d: Double): Complex {
    return Complex(this.re * d, this.im * d)
}

operator fun Complex.times(c: Complex): Complex {
    return Complex(this.re * c.re - this.im * c.im, this.re * c.im + this.im * c.re)
}

operator fun Complex.plus(c: Complex): Complex {
    return Complex(re + c.re, im + c.im)
}

operator fun Complex.minus(d: Double): Complex {
    return Complex(this.re - d, this.im)
}

operator fun Complex.minus(c: Complex): Complex {
    return Complex(this.re - c.re, this.im - c.im)
}

fun Complex.abs() = sqrt(re * re + im * im)

operator fun Double.minus(c: Complex): Complex {
    return Complex(this - c.re, - c.im)
}

operator fun Double.plus(c: Complex): Complex {
    return Complex(this + c.re, c.im)
}

operator fun Double.times(c: Complex): Complex {
    return Complex(this * c.re, this * c.im)
}

val Double.j: Complex
    get() = Complex(0.0, this)

val complex: Complex
    get() = Complex(0.0, 0.0)
