# Gpt_pp

# Kotlin Beginner Course (မြန်မာဘာသာ)

ဒီ course က Kotlin ကို အခြေခံကနေ step by step လေ့လာချင်သူတွေအတွက် ရေးထားတဲ့ beginner-friendly သင်ခန်းစာဖြစ်ပါတယ်။ Programming အတွေ့အကြုံ မရှိသေးသူများလည်း လိုက်လုပ်နိုင်အောင် concept တစ်ခုချင်းစီကို ရှင်းပြထားပြီး code example နဲ့ exercise တွေပါ ထည့်ထားပါတယ်။

## Course ကို ဘယ်လိုအသုံးပြုမလဲ

1. သင်ခန်းစာတွေကို အစဉ်လိုက်ဖတ်ပါ။
2. Example code တွေကို ကိုယ်တိုင်ရိုက်ပြီး run ကြည့်ပါ။
3. Lesson တစ်ခုပြီးတိုင်း exercise ကို လုပ်ပါ။
4. မသိတဲ့ error တွေ့ရင် error message ကို ဖတ်ပြီး အကြောင်းရင်းကို စဉ်းစားပါ။
5. အခြေခံတွေ နားလည်လာမှ Android development သို့ ဆက်သွားပါ။

## လိုအပ်တဲ့ Tools

Kotlin ကို စတင်လေ့လာဖို့ အောက်ပါနည်းလမ်းတစ်ခုခုကို သုံးနိုင်ပါတယ်။

### Option 1: Kotlin Playground

Beginner အတွက် အလွယ်ဆုံးနည်းလမ်းပါ။ Browser ထဲမှာပဲ Kotlin code ရေးပြီး run နိုင်ပါတယ်။

- Website: <https://play.kotlinlang.org/>

### Option 2: IntelliJ IDEA

Project တွေကို local computer မှာ တည်ဆောက်ချင်ရင် သုံးနိုင်ပါတယ်။

- IntelliJ IDEA Community Edition ကို install လုပ်ပါ။
- Kotlin project အသစ်တစ်ခု create လုပ်ပါ။
- `main` function ထဲမှာ code စရေးပါ။

### Option 3: Android Studio

Android app development အတွက် Kotlin သင်ချင်သူတွေက Android Studio ကို သုံးနိုင်ပါတယ်။ သို့သော် အစပိုင်းမှာ Kotlin syntax ကို အရင်နားလည်အောင် Kotlin Playground နဲ့ စလေ့လာတာ ပိုသင့်တော်ပါတယ်။

---

## Lesson 1: Kotlin ဆိုတာဘာလဲ

Kotlin သည် JetBrains က ဖန်တီးထားသော modern programming language တစ်ခုဖြစ်ပါတယ်။ Android app development မှာ အလွန်အသုံးများပြီး backend, desktop, multiplatform development တွေမှာလည်း အသုံးပြုနိုင်ပါတယ်။

### Kotlin ရဲ့ အားသာချက်များ

- Java ထက် syntax ပိုတိုပြီး ဖတ်ရလွယ်ပါတယ်။
- Null safety ပါဝင်လို့ null error တွေကို လျှော့ချနိုင်ပါတယ်။
- Object-oriented programming နဲ့ functional programming style နှစ်မျိုးလုံးကို ထောက်ပံ့ပါတယ်။
- Android development အတွက် official language တစ်ခုဖြစ်ပါတယ်။

### ပထမဆုံး Kotlin Program

```kotlin
fun main() {
    println("Hello, Kotlin!")
}
```

### ရှင်းလင်းချက်

- `fun` သည် function တစ်ခုကို ကြေညာရန် သုံးပါတယ်။
- `main` သည် program စတင် run သော function ဖြစ်ပါတယ်။
- `println()` သည် စာသားကို output ထုတ်ပေးပါတယ်။

### Exercise

အောက်ပါစာသားကို output ထုတ်ပါ။

```text
I am learning Kotlin.
```

---

## Lesson 2: Variables and Data Types

Variable ဆိုတာ data ကို သိမ်းထားတဲ့ နေရာတစ်ခုဖြစ်ပါတယ်။ Kotlin မှာ variable ကြေညာရန် `val` နဲ့ `var` ကို သုံးပါတယ်။

### `val` and `var`

```kotlin
fun main() {
    val name = "Aung Aung"
    var age = 20

    println(name)
    println(age)
}
```

### ကွာခြားချက်

- `val` သည် value ပြောင်းလို့မရပါ။ read-only variable ဖြစ်ပါတယ်။
- `var` သည် value ပြောင်းလို့ရပါတယ်။ mutable variable ဖြစ်ပါတယ်။

```kotlin
fun main() {
    val country = "Myanmar"
    var score = 80

    score = 95

    println(country)
    println(score)
}
```

`country` ကို ပြန်ပြောင်းလို့မရပေမယ့် `score` ကို ပြောင်းလို့ရပါတယ်။

### Common Data Types

| Type | Meaning | Example |
| --- | --- | --- |
| `Int` | ကိန်းပြည့် | `25` |
| `Double` | ဒဿမကိန်း | `19.99` |
| `String` | စာသား | `"Kotlin"` |
| `Boolean` | true/false | `true` |
| `Char` | စာလုံးတစ်လုံး | `'A'` |

### Type ကို တိတိကျကျရေးခြင်း

```kotlin
fun main() {
    val city: String = "Yangon"
    val population: Int = 5000000
    val isCapital: Boolean = false

    println(city)
    println(population)
    println(isCapital)
}
```

### Exercise

ကိုယ့်နာမည်၊ အသက်၊ programming လေ့လာနေ/မနေ ကို variable သုံးခုနဲ့ သိမ်းပြီး print ထုတ်ပါ။

---

## Lesson 3: String Template

String template သည် variable value ကို string ထဲမှာ ထည့်သုံးနိုင်တဲ့ feature ဖြစ်ပါတယ်။

```kotlin
fun main() {
    val name = "Su Su"
    val age = 18

    println("My name is $name.")
    println("I am $age years old.")
}
```

Expression တွေကို `${}` ထဲမှာ ထည့်သုံးနိုင်ပါတယ်။

```kotlin
fun main() {
    val price = 1000
    val quantity = 3

    println("Total price is ${price * quantity} kyats.")
}
```

### Exercise

`itemName`, `price`, `quantity` variable တွေသုံးပြီး အောက်ပါပုံစံ output ထုတ်ပါ။

```text
I bought 2 books. Total cost is 10000 kyats.
```

---

## Lesson 4: Operators

Operator ဆိုတာ calculation နဲ့ comparison လုပ်ရန် သုံးတဲ့ symbol ဖြစ်ပါတယ်။

### Arithmetic Operators

```kotlin
fun main() {
    val a = 10
    val b = 3

    println(a + b)
    println(a - b)
    println(a * b)
    println(a / b)
    println(a % b)
}
```

### Comparison Operators

```kotlin
fun main() {
    val x = 10
    val y = 20

    println(x == y)
    println(x != y)
    println(x > y)
    println(x < y)
    println(x >= y)
    println(x <= y)
}
```

### Logical Operators

```kotlin
fun main() {
    val hasEmail = true
    val hasPassword = true

    println(hasEmail && hasPassword)
    println(hasEmail || hasPassword)
    println(!hasEmail)
}
```

### Exercise

Student တစ်ယောက်ရဲ့ mark ကို variable ထဲမှာ သိမ်းပြီး `mark >= 40` ဖြစ်ရင် pass ဖြစ်ကြောင်း Boolean result ထုတ်ပါ။

---

## Lesson 5: Conditional Statements

Condition အပေါ်မူတည်ပြီး code တစ်ပိုင်းကို run ချင်ရင် `if`, `else if`, `else` ကို သုံးပါတယ်။

```kotlin
fun main() {
    val mark = 75

    if (mark >= 80) {
        println("Excellent")
    } else if (mark >= 40) {
        println("Pass")
    } else {
        println("Fail")
    }
}
```

### `if` as Expression

Kotlin မှာ `if` ကို value ပြန်ပေးတဲ့ expression အနေနဲ့လည်း သုံးနိုင်ပါတယ်။

```kotlin
fun main() {
    val age = 17
    val message = if (age >= 18) "Adult" else "Under 18"

    println(message)
}
```

### Exercise

Age variable တစ်ခုတည်ဆောက်ပြီး အသက် 18 နှစ်နှင့်အထက်ဖြစ်ရင် `You can register.`၊ မဟုတ်ရင် `You are too young.` လို့ output ထုတ်ပါ။

---

## Lesson 6: `when` Expression

`when` သည် condition အများကြီးကို စနစ်တကျစစ်ရန် သုံးပါတယ်။ Java ရဲ့ `switch` နဲ့ ဆင်တူပေမယ့် ပိုအားကောင်းပါတယ်။

```kotlin
fun main() {
    val day = 3

    val dayName = when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> "Invalid day"
    }

    println(dayName)
}
```

### Range နဲ့သုံးခြင်း

```kotlin
fun main() {
    val mark = 85

    val grade = when (mark) {
        in 80..100 -> "A"
        in 60..79 -> "B"
        in 40..59 -> "C"
        in 0..39 -> "Fail"
        else -> "Invalid mark"
    }

    println(grade)
}
```

### Exercise

Month number `1` မှ `12` အထိကို month name ပြောင်းပေးတဲ့ `when` expression ရေးပါ။

---

## Lesson 7: Loops

Loop သည် code တစ်ခုကို အကြိမ်ကြိမ် run ချင်တဲ့အခါ သုံးပါတယ်။

### `for` Loop

```kotlin
fun main() {
    for (number in 1..5) {
        println(number)
    }
}
```

### `while` Loop

```kotlin
fun main() {
    var count = 1

    while (count <= 5) {
        println(count)
        count++
    }
}
```

### `repeat`

```kotlin
fun main() {
    repeat(3) {
        println("Kotlin is fun!")
    }
}
```

### Exercise

`1` ကနေ `10` အထိရှိတဲ့ ကိန်းတွေထဲက even number တွေကိုပဲ print ထုတ်ပါ။

---

## Lesson 8: Functions

Function သည် task တစ်ခုကို ပြုလုပ်ပေးသော reusable code block ဖြစ်ပါတယ်။

```kotlin
fun greet() {
    println("Hello!")
}

fun main() {
    greet()
}
```

### Parameter ပါသော Function

```kotlin
fun greetUser(name: String) {
    println("Hello, $name!")
}

fun main() {
    greetUser("Mg Mg")
}
```

### Return Value ပါသော Function

```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}

fun main() {
    val result = add(5, 7)
    println(result)
}
```

### Single-expression Function

```kotlin
fun multiply(a: Int, b: Int): Int = a * b
```

### Exercise

`calculateArea(width: Int, height: Int): Int` function တစ်ခုရေးပြီး rectangle area ကို return ပြန်ပါ။

---

## Lesson 9: Lists

List သည် value အများကြီးကို collection တစ်ခုထဲမှာ သိမ်းရန် သုံးပါတယ်။

### Read-only List

```kotlin
fun main() {
    val fruits = listOf("Apple", "Banana", "Orange")

    println(fruits[0])
    println(fruits.size)
}
```

### Mutable List

```kotlin
fun main() {
    val names = mutableListOf("Aung", "Su")

    names.add("Hla")
    names.remove("Su")

    println(names)
}
```

### Loop နဲ့ List ဖတ်ခြင်း

```kotlin
fun main() {
    val languages = listOf("Kotlin", "Java", "Python")

    for (language in languages) {
        println(language)
    }
}
```

### Exercise

ကိုယ်ကြိုက်တဲ့ food 5 မျိုးကို list ထဲမှာ သိမ်းပြီး တစ်ကြောင်းစီ print ထုတ်ပါ။

---

## Lesson 10: Maps

Map သည် key နဲ့ value အတွဲလိုက် data သိမ်းရန် သုံးပါတယ်။

```kotlin
fun main() {
    val user = mapOf(
        "name" to "Aye Aye",
        "city" to "Mandalay",
        "job" to "Developer"
    )

    println(user["name"])
    println(user["city"])
}
```

### Mutable Map

```kotlin
fun main() {
    val scores = mutableMapOf(
        "Math" to 80,
        "English" to 75
    )

    scores["Science"] = 90
    println(scores)
}
```

### Exercise

Student တစ်ယောက်အတွက် name, age, city ကို map ထဲမှာ သိမ်းပြီး print ထုတ်ပါ။

---

## Lesson 11: Null Safety

Kotlin ရဲ့ အရေးကြီးဆုံး feature တစ်ခုက null safety ဖြစ်ပါတယ်။ ပုံမှန် variable တွေမှာ `null` သိမ်းလို့မရပါ။

```kotlin
fun main() {
    var name: String = "Kotlin"
    // name = null // Error ဖြစ်မယ်
}
```

Null သိမ်းချင်ရင် type နောက်မှာ `?` ထည့်ရပါတယ်။

```kotlin
fun main() {
    var name: String? = "Kotlin"
    name = null

    println(name)
}
```

### Safe Call Operator

```kotlin
fun main() {
    val name: String? = null

    println(name?.length)
}
```

### Elvis Operator

```kotlin
fun main() {
    val name: String? = null
    val length = name?.length ?: 0

    println(length)
}
```

### Exercise

Nullable string variable တစ်ခုရေးပြီး value မရှိရင် `Unknown` ကို output ထုတ်ပါ။

---

## Lesson 12: Classes and Objects

Class သည် object တည်ဆောက်ရန် blueprint ဖြစ်ပါတယ်။ Object သည် class မှ ဖန်တီးထားသော တကယ့် instance ဖြစ်ပါတယ်။

```kotlin
class Person(val name: String, var age: Int)

fun main() {
    val person = Person("Ko Ko", 21)

    println(person.name)
    println(person.age)
}
```

### Method ပါသော Class

```kotlin
class Dog(val name: String) {
    fun bark() {
        println("$name says woof!")
    }
}

fun main() {
    val dog = Dog("Lucky")
    dog.bark()
}
```

### Data Class

Data class သည် data သိမ်းရန် အထူးသင့်တော်ပါတယ်။

```kotlin
data class Product(
    val name: String,
    val price: Int
)

fun main() {
    val product = Product("Book", 5000)
    println(product)
}
```

### Exercise

`Student` class တစ်ခုရေးပါ။ `name`, `age`, `grade` property တွေပါစေ။ Object တစ်ခုတည်ဆောက်ပြီး property တွေကို print ထုတ်ပါ။

---

## Lesson 13: Basic Error Handling

Program run နေစဉ် error ဖြစ်နိုင်တဲ့ code တွေကို handle လုပ်ဖို့ `try`, `catch` ကို သုံးနိုင်ပါတယ်။ မှတ်ချက် - import တွေကိုတော့ `try/catch` ထဲမထည့်သင့်ပါ။

```kotlin
fun main() {
    val numberText = "123"

    try {
        val number = numberText.toInt()
        println(number)
    } catch (error: NumberFormatException) {
        println("Invalid number")
    }
}
```

### Exercise

`"abc"` ကို integer ပြောင်းကြည့်ပြီး error ဖြစ်ရင် `Cannot convert to number` လို့ output ထုတ်ပါ။

---

## Lesson 14: Mini Project - Simple Calculator

အခုအထိ သင်ထားတဲ့ variables, functions, `when` expression တွေကို သုံးပြီး simple calculator တစ်ခုရေးပါမယ်။

```kotlin
fun calculate(a: Double, b: Double, operator: String): Double? {
    return when (operator) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> if (b != 0.0) a / b else null
        else -> null
    }
}

fun main() {
    val result = calculate(10.0, 5.0, "+")

    if (result != null) {
        println("Result is $result")
    } else {
        println("Invalid calculation")
    }
}
```

### Project Challenge

Calculator ကို အောက်ပါ feature တွေ ထပ်ထည့်ပါ။

- `%` operator ထည့်ပါ။
- Division by zero ဖြစ်ရင် error message သီးသန့်ပြပါ။
- Calculation result ကို string template နဲ့ ပိုလှအောင် output ထုတ်ပါ။

---

## Lesson 15: နောက်ထပ်ဘာလေ့လာမလဲ

Kotlin အခြေခံ syntax ကို နားလည်ပြီးသွားရင် အောက်ပါ topic တွေကို ဆက်လေ့လာနိုင်ပါတယ်။

1. Object-oriented programming အသေးစိတ်
2. Inheritance and interfaces
3. Lambdas and higher-order functions
4. Collections operations: `map`, `filter`, `reduce`
5. Coroutines
6. File input/output
7. Unit testing
8. Android development with Jetpack Compose
9. Ktor backend development
10. Kotlin Multiplatform

## Daily Study Plan

Beginner အနေနဲ့ ၂ ပတ်အတွင်း အခြေခံပြီးချင်ရင် အောက်ပါ plan ကို လိုက်နိုင်ပါတယ်။

| Day | Topic |
| --- | --- |
| Day 1 | Kotlin setup, Hello World |
| Day 2 | Variables and data types |
| Day 3 | String templates and operators |
| Day 4 | Conditions and `when` |
| Day 5 | Loops |
| Day 6 | Functions |
| Day 7 | Review and small exercises |
| Day 8 | Lists |
| Day 9 | Maps |
| Day 10 | Null safety |
| Day 11 | Classes and objects |
| Day 12 | Data classes and methods |
| Day 13 | Error handling |
| Day 14 | Simple calculator mini project |

## Beginner Tips

- Code ကို ဖတ်ရုံနဲ့ မလုံလောက်ပါ။ ကိုယ်တိုင်ရိုက်ပြီး run ပါ။
- Error ဖြစ်တာကို ကြောက်စရာမလိုပါ။ Error message က သင်ယူစရာဖြစ်ပါတယ်။
- `val` ကို default အနေနဲ့ သုံးပါ။ value ပြောင်းဖို့လိုမှ `var` သုံးပါ။
- Function သေးသေးလေးတွေ ခွဲရေးပါ။
- Null safety ကို အစောကြီးကတည်းက သေချာနားလည်အောင်လုပ်ပါ။

## Final Practice Projects

အောက်ပါ project တွေကို ကိုယ်တိုင်ရေးကြည့်ပါ။

1. Grade calculator
2. Todo list using mutable list
3. Shopping cart total calculator
4. Student information manager
5. Number guessing game

ဒီ course ကို ပြီးမြောက်အောင် လိုက်လုပ်ပြီးရင် Kotlin syntax အခြေခံတွေကို နားလည်ပြီး simple program တွေကို ကိုယ်တိုင်ရေးနိုင်ပါလိမ့်မယ်။
