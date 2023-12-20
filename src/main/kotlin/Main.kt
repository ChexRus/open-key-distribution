import kotlin.random.Random

//lab7 открытое распределения ключей по схеме Диффи-Хеллмана и MTI
fun main() {
    var p = 0
    while (p == 0) {
        p = inputP()
    }
    var alpha = 0
    while (alpha == 0) {
        alpha = inputAlpha(p - 1)
    }
    val x = rand(p - 2)
    val y = rand(p - 2)
    val a = rand(p - 2)
    val b = rand(p - 2)
    val mesA = gorner(alpha, x, p)
    val mesB = gorner(alpha, y, p)
    val zA = gorner(alpha, a, p)
    val zB = gorner(alpha, b, p)
    println("Сторона А сгенерировала число а=$a и x=$x и передает стороне B число mesA=$mesA и zA=$zA")
    println("Сторона B сгенерировала число b=$b и y=$y и передает стороне B число mesB=$mesB и zB=$zB")
    val k1 = (gorner(mesB!!, a, p)!! * gorner(zB!!, x, p)!!).mod(p) //Сторона А вычисляет ключ
    val k2 = (gorner(mesA!!, b, p)!! * gorner(zA!!, y, p)!!).mod(p)  //Сторона B вычисляет ключ
    if (k1 == k2)
        println("Ключи k1=$k1 и k2=$k2 одинаковы")
    else println("Произошла подмена")
}

fun rand(p: Int): Int {
    var x = 0
    x = Random.nextInt(1, p)
    return x
}  //РАНДОМАЙЗЕР

fun inputAlpha(p: Int): Int {
    var alpha = 0
    println("Введите натуральное число 'alpha' из отрезка [1;$p]")
    return try {
        alpha = readln().toInt()
        if (alpha < 1 || alpha > p) {
            println("Некорректный ввод. Попробуйте ещё раз")
            0
        } else alpha
    } catch (e: NumberFormatException) {
        println("Некорректный ввод. Попробуйте ещё раз")
        0
    }
}   //ВВОДИМ НАТУРАЛЬНОЕ ЧИСЛО a

fun inputP(): Int {
    var p = 0
    println("Введите простое число P:")
    try {
        p = readln().toInt()
        var flag = true
        for (i in 2..(p / 2)) {
            if (p % i == 0) {
                flag = false
                break
            }
        }
        return if (p < 1 || !flag) {
            println("Некорректный ввод. Попробуйте ещё раз")
            0
        } else p
    } catch (e: NumberFormatException) {
        println("Некорректный ввод. Попробуйте ещё раз")
        return 0
    }
}  //ВВОДИМ ПРОСТОЕ ЧИСЛО p

fun gorner(a: Int, x: Int, m: Int): Int? {
    var r = 1
    var k = 0
    var y: Int? = null
    if (m == 0) return null
    if (a == 0) return 0
    if (a == 1 || x == 0) return 1
    while (r <= x && k < 32) {
        r = (1 shl k)
        k++
    }
    k--
    if (k == 0 || k > 32) return null
    r = a
    y = if (x % 2 == 1) a
    else 1
    for (i in 1..k - 1) {
        r = (r * r).mod(m)
        if (((x shr i) and 1) == 1) {
            if (y != null) {
                y = (y.toInt() * r).mod(m)
            }
        }
    }
    return y
} //a^x mod m