# Java Programming Beginner Course (Step by Step)

ဤ course သည် Java programming ကို အခြေခံမှစ၍ တစ်ဆင့်ချင်း ဖတ်ရှုလေ့လာနိုင်ရန် ရေးသားထားသော beginner course ဖြစ်သည်။ Programming မရေးဖူးသေးသူများအတွက်လည်း လိုက်လုပ်နိုင်အောင် concept၊ example code၊ လက်တွေ့လေ့ကျင့်ခန်းများကို အခန်းလိုက် စီစဉ်ထားသည်။

## Course Goal

ဤ course ပြီးဆုံးသည့်အခါ သင်သည်—

- Java program တစ်ခု၏ structure ကို နားလည်နိုင်မည်။
- Variables, data types, operators, control flow, loops များကို အသုံးပြုနိုင်မည်။
- Method, array, class, object စသည့် Java အခြေခံ concept များကို ရေးနိုင်မည်။
- Beginner level console application အသေးစားများ တည်ဆောက်နိုင်မည်။
- Object-Oriented Programming (OOP) ၏ အခြေခံအယူအဆများကို နားလည်နိုင်မည်။

## Prerequisites

- Computer အသုံးပြုနိုင်ခြင်း
- English keyboard ဖြင့် code ရိုက်နိုင်ခြင်း
- Programming အတွေ့အကြုံ မလိုအပ်ပါ

## Tools to Install

### 1. JDK (Java Development Kit)

Java code ကို compile/run လုပ်ရန် JDK လိုအပ်သည်။ Beginner အတွက် LTS version ဖြစ်သော Java 17 သို့မဟုတ် Java 21 ကို အသုံးပြုနိုင်သည်။

Install ပြီးနောက် terminal သို့မဟုတ် command prompt တွင် စစ်ဆေးပါ။

```bash
java -version
javac -version
```

### 2. Code Editor

Beginner အတွက် အောက်ပါ editor တစ်ခုခု သုံးနိုင်သည်။

- Visual Studio Code
- IntelliJ IDEA Community Edition
- Eclipse IDE

ဤ course တွင် terminal command နှင့် plain Java file များဖြင့် ရှင်းပြမည်။

---

# Module 1: Java ဆိုတာဘာလဲ

## 1.1 Java ၏ အဓိပ္ပါယ်

Java သည် general-purpose programming language တစ်ခုဖြစ်ပြီး web application, mobile application, desktop application, backend system, enterprise software များတွင် အသုံးများသည်။ Java ၏ အဓိကအားသာချက်မှာ “Write Once, Run Anywhere” ဖြစ်သည်။ ဆိုလိုသည်မှာ Java program ကို တစ်ကြိမ်ရေးပြီး JVM ရှိသော platform များတွင် run နိုင်သည်။

## 1.2 Java Program Run ဖြစ်ပုံ

Java source code သည် `.java` file ဖြစ်သည်။ ၎င်းကို `javac` compiler ဖြင့် bytecode အဖြစ် compile လုပ်ပြီး `.class` file ထွက်လာသည်။ ထို့နောက် JVM (Java Virtual Machine) က bytecode ကို run လုပ်သည်။

Flow:

```text
Hello.java  ->  javac  ->  Hello.class  ->  java  ->  Program Output
```

## 1.3 First Java Program

`Hello.java` file တစ်ခု ဖန်တီးပါ။

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");
    }
}
```

Run လုပ်ရန်—

```bash
javac Hello.java
java Hello
```

Output:

```text
Hello, Java!
```

## Code Explanation

- `public class Hello` သည် class တစ်ခုကို ကြေညာခြင်းဖြစ်သည်။
- File name သည် public class name နှင့် တူရမည်။ ဥပမာ `Hello.java`။
- `main` method သည် Java program စတင် run သည့် entry point ဖြစ်သည်။
- `System.out.println()` သည် text ကို console တွင် print ထုတ်ရန် အသုံးပြုသည်။

## Practice

1. `Hello.java` ကို run လုပ်ပါ။
2. Output ကို `Hello, Myanmar!` ဟု ပြောင်းပါ။
3. သင့်နာမည်ကို print ထုတ်ပါ။

---

# Module 2: Variables and Data Types

## 2.1 Variable ဆိုတာဘာလဲ

Variable သည် data သိမ်းဆည်းရန် memory ထဲတွင် နာမည်ပေးထားသော နေရာတစ်ခုဖြစ်သည်။

```java
int age = 20;
String name = "Aung Aung";
```

## 2.2 Common Data Types

| Data Type | Meaning | Example |
| --- | --- | --- |
| `int` | Integer number | `10`, `100` |
| `double` | Decimal number | `3.14`, `99.5` |
| `char` | Character တစ်လုံး | `'A'` |
| `boolean` | true/false | `true`, `false` |
| `String` | Text | `"Hello"` |

## 2.3 Example

```java
public class VariablesDemo {
    public static void main(String[] args) {
        String name = "Mya Mya";
        int age = 18;
        double height = 5.4;
        boolean isStudent = true;

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Height: " + height);
        System.out.println("Student: " + isStudent);
    }
}
```

## 2.4 Naming Rules

Variable name ရေးရာတွင်—

- Letter, digit, underscore `_`, dollar sign `$` ပါနိုင်သည်။
- Digit ဖြင့် စ၍ မရပါ။
- Java keyword မဖြစ်ရပါ။
- Meaningful name သုံးပါ။ ဥပမာ `studentAge`, `totalPrice`။

## Practice

1. သင့်နာမည်၊ အသက်၊ မြို့၊ student ဖြစ်/မဖြစ် ကို variables ဖြင့် သိမ်းပြီး print ထုတ်ပါ။
2. `int` နှစ်ခုကို သိမ်းပြီး ပေါင်းလဒ်ကို print ထုတ်ပါ။

---

# Module 3: Operators

## 3.1 Arithmetic Operators

| Operator | Meaning | Example |
| --- | --- | --- |
| `+` | Addition | `a + b` |
| `-` | Subtraction | `a - b` |
| `*` | Multiplication | `a * b` |
| `/` | Division | `a / b` |
| `%` | Remainder | `a % b` |

```java
public class OperatorDemo {
    public static void main(String[] args) {
        int a = 10;
        int b = 3;

        System.out.println(a + b);
        System.out.println(a - b);
        System.out.println(a * b);
        System.out.println(a / b);
        System.out.println(a % b);
    }
}
```

## 3.2 Comparison Operators

Comparison operator များသည် boolean result ပြန်ပေးသည်။

| Operator | Meaning |
| --- | --- |
| `==` | equal |
| `!=` | not equal |
| `>` | greater than |
| `<` | less than |
| `>=` | greater than or equal |
| `<=` | less than or equal |

## 3.3 Logical Operators

| Operator | Meaning |
| --- | --- |
| `&&` | AND |
| `||` | OR |
| `!` | NOT |

Example:

```java
int age = 20;
boolean hasTicket = true;

System.out.println(age >= 18 && hasTicket);
```

## Practice

1. `price` နှင့် `quantity` ကို သုံးပြီး total cost တွက်ပါ။
2. အသက် 18 နှင့်အထက် ဖြစ်/မဖြစ် boolean result print ထုတ်ပါ။

---

# Module 4: Control Flow with if/else

## 4.1 if Statement

Condition မှန်လျှင် code block ကို run လုပ်သည်။

```java
int age = 18;

if (age >= 18) {
    System.out.println("Adult");
}
```

## 4.2 if/else Statement

```java
int age = 16;

if (age >= 18) {
    System.out.println("Adult");
} else {
    System.out.println("Not adult");
}
```

## 4.3 else if

Condition များစွာ စစ်ရန် အသုံးပြုသည်။

```java
int mark = 75;

if (mark >= 80) {
    System.out.println("Grade A");
} else if (mark >= 60) {
    System.out.println("Grade B");
} else if (mark >= 40) {
    System.out.println("Grade C");
} else {
    System.out.println("Fail");
}
```

## Practice

1. Number တစ်ခုသည် positive, negative, zero ဖြစ်/မဖြစ် စစ်ပါ။
2. Mark အလိုက် grade ထုတ်ပါ။
3. Age အလိုက် child, teenager, adult ဟု ခွဲပါ။

---

# Module 5: Loops

Loop သည် code တစ်ခုကို ထပ်ခါထပ်ခါ run လုပ်ရန် အသုံးပြုသည်။

## 5.1 for Loop

အကြိမ်အရေအတွက် သိပြီးသားဖြစ်လျှင် `for` loop သုံးသည်။

```java
for (int i = 1; i <= 5; i++) {
    System.out.println(i);
}
```

## 5.2 while Loop

Condition မှန်နေသမျှ run လုပ်သည်။

```java
int count = 1;

while (count <= 5) {
    System.out.println(count);
    count++;
}
```

## 5.3 do-while Loop

အနည်းဆုံး တစ်ကြိမ် run လုပ်ပြီးမှ condition စစ်သည်။

```java
int count = 1;

do {
    System.out.println(count);
    count++;
} while (count <= 5);
```

## Practice

1. 1 မှ 10 ထိ print ထုတ်ပါ။
2. 1 မှ 100 ထိ even number များ print ထုတ်ပါ။
3. 1 မှ 10 ထိ sum တွက်ပါ။
4. Multiplication table တစ်ခုရေးပါ။

---

# Module 6: Methods

## 6.1 Method ဆိုတာဘာလဲ

Method သည် reusable code block ဖြစ်သည်။ Code ကို စနစ်တကျ ခွဲခြားရေးရန် method များကို အသုံးပြုသည်။

```java
public class MethodDemo {
    static void sayHello() {
        System.out.println("Hello!");
    }

    public static void main(String[] args) {
        sayHello();
    }
}
```

## 6.2 Method with Parameters

```java
public class Greeting {
    static void greet(String name) {
        System.out.println("Hello, " + name);
    }

    public static void main(String[] args) {
        greet("Aung Aung");
        greet("Mya Mya");
    }
}
```

## 6.3 Method with Return Value

```java
public class Calculator {
    static int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        int result = add(5, 7);
        System.out.println(result);
    }
}
```

## Practice

1. `multiply(int a, int b)` method ရေးပါ။
2. `isEven(int number)` method ရေးပြီး boolean return ပြန်ပါ။
3. Name ကို parameter အဖြစ် လက်ခံပြီး greeting print ထုတ်သော method ရေးပါ။

---

# Module 7: Arrays

## 7.1 Array ဆိုတာဘာလဲ

Array သည် data type တူညီသော values များကို တစ်စုတစ်စည်းတည်း သိမ်းရန် အသုံးပြုသည်။

```java
int[] numbers = {10, 20, 30, 40, 50};
```

## 7.2 Access Array Elements

Array index သည် 0 မှ စသည်။

```java
System.out.println(numbers[0]); // 10
System.out.println(numbers[2]); // 30
```

## 7.3 Loop Through Array

```java
public class ArrayDemo {
    public static void main(String[] args) {
        int[] numbers = {10, 20, 30, 40, 50};

        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }
    }
}
```

## 7.4 Enhanced for Loop

```java
for (int number : numbers) {
    System.out.println(number);
}
```

## Practice

1. Integer array တစ်ခု ဖန်တီးပြီး sum တွက်ပါ။
2. Array ထဲမှ largest number ရှာပါ။
3. String array ထဲမှ name များ print ထုတ်ပါ။

---

# Module 8: User Input with Scanner

## 8.1 Scanner

User ထံမှ input ယူရန် `Scanner` class ကို အသုံးပြုသည်။

```java
import java.util.Scanner;

public class InputDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your age: ");
        int age = scanner.nextInt();

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);

        scanner.close();
    }
}
```

## Common Scanner Methods

| Method | Usage |
| --- | --- |
| `nextLine()` | full line text ဖတ်ရန် |
| `next()` | word တစ်လုံး ဖတ်ရန် |
| `nextInt()` | integer ဖတ်ရန် |
| `nextDouble()` | decimal number ဖတ်ရန် |

## Practice

1. User ထံမှ number နှစ်ခုယူပြီး sum print ထုတ်ပါ။
2. User ထံမှ mark ယူပြီး grade ထုတ်ပါ။
3. Simple calculator တစ်ခုရေးပါ။

---

# Module 9: Classes and Objects

## 9.1 Class ဆိုတာဘာလဲ

Class သည် object တစ်ခု ဖန်တီးရန် blueprint ဖြစ်သည်။ Object သည် class မှ ဖန်တီးထားသော instance ဖြစ်သည်။

ဥပမာ `Student` class တွင် name, age, grade စသည့် data နှင့် study လုပ်သည့် behavior ပါနိုင်သည်။

## 9.2 Simple Class

```java
public class Student {
    String name;
    int age;

    void introduce() {
        System.out.println("My name is " + name + " and I am " + age + " years old.");
    }
}
```

## 9.3 Create Object

```java
public class Main {
    public static void main(String[] args) {
        Student student1 = new Student();
        student1.name = "Aung Aung";
        student1.age = 20;
        student1.introduce();
    }
}
```

## Practice

1. `Car` class တစ်ခု ဖန်တီးပါ။ Fields: brand, color, year။
2. `Book` class တစ်ခု ဖန်တီးပါ။ Fields: title, author, price။
3. Object နှစ်ခု ဖန်တီးပြီး data များ print ထုတ်ပါ။

---

# Module 10: Constructors

## 10.1 Constructor ဆိုတာဘာလဲ

Constructor သည် object ဖန်တီးသည့်အချိန်တွင် အလိုအလျောက် run သော special method ဖြစ်သည်။ Constructor name သည် class name နှင့်တူရမည်။ Return type မရှိပါ။

```java
public class Student {
    String name;
    int age;

    Student(String studentName, int studentAge) {
        name = studentName;
        age = studentAge;
    }

    void introduce() {
        System.out.println(name + " is " + age + " years old.");
    }
}
```

အသုံးပြုပုံ—

```java
public class Main {
    public static void main(String[] args) {
        Student student = new Student("Mya Mya", 19);
        student.introduce();
    }
}
```

## Practice

1. Constructor ပါသော `Car` class ရေးပါ။
2. Constructor ပါသော `Product` class ရေးပါ။
3. Object များ ဖန်တီးပြီး method ခေါ်ပါ။

---

# Module 11: OOP Basics

Object-Oriented Programming (OOP) တွင် အဓိက concept ၄ ခု ရှိသည်။

## 11.1 Encapsulation

Data ကို class ထဲတွင် သိမ်းပြီး direct access မလုပ်စေဘဲ method များမှတစ်ဆင့် ထိန်းချုပ်ခြင်းဖြစ်သည်။

```java
public class BankAccount {
    private double balance;

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}
```

## 11.2 Inheritance

Class တစ်ခုက အခြား class ၏ field/method များကို ဆက်ခံအသုံးပြုခြင်းဖြစ်သည်။

```java
class Animal {
    void eat() {
        System.out.println("Eating...");
    }
}

class Dog extends Animal {
    void bark() {
        System.out.println("Barking...");
    }
}
```

## 11.3 Polymorphism

Object များသည် တူညီသော method name ကို မတူညီသော behavior ဖြင့် လုပ်ဆောင်နိုင်ခြင်းဖြစ်သည်။

```java
class Animal {
    void sound() {
        System.out.println("Animal sound");
    }
}

class Cat extends Animal {
    void sound() {
        System.out.println("Meow");
    }
}
```

## 11.4 Abstraction

အသေးစိတ် implementation ကို ဖုံးကွယ်ပြီး လိုအပ်သော behavior ကိုသာ ပြခြင်းဖြစ်သည်။ Beginner အဆင့်တွင် abstract class နှင့် interface များကို နောက်ပိုင်းတွင် အသေးစိတ်လေ့လာနိုင်သည်။

## Practice

1. `BankAccount` class ကို private balance နှင့် deposit/withdraw methods များဖြင့် ရေးပါ။
2. `Animal` parent class နှင့် `Dog`, `Cat` child classes ရေးပါ။
3. Child class များတွင် sound method ကို မတူအောင်ရေးပါ။

---

# Module 12: Mini Projects

## Project 1: Simple Calculator

Requirements:

- User ထံမှ number နှစ်ခု input ယူပါ။
- Operator `+`, `-`, `*`, `/` ကို input ယူပါ။
- Result ကို print ထုတ်ပါ။
- Division by zero ကို စစ်ပါ။

Hints:

- `Scanner` သုံးပါ။
- `if/else` သို့မဟုတ် `switch` သုံးပါ။

## Project 2: Student Grade System

Requirements:

- Student name နှင့် mark input ယူပါ။
- Mark အလိုက် grade ထုတ်ပါ။
- 80 နှင့်အထက်: A
- 60 နှင့်အထက်: B
- 40 နှင့်အထက်: C
- 40 အောက်: Fail

## Project 3: Number Guessing Game

Requirements:

- Program ထဲတွင် secret number တစ်ခု သတ်မှတ်ပါ။
- User ကို number ခန့်မှန်းခိုင်းပါ။
- Guess မှားလျှင် higher/lower hint ပေးပါ။
- မှန်လျှင် congratulations message ပြပါ။

## Project 4: Library Book List

Requirements:

- `Book` class ရေးပါ။
- title, author, price fields ထည့်ပါ။
- Book objects ၃ ခု ဖန်တီးပါ။
- Array ထဲတွင် သိမ်းပြီး loop ဖြင့် print ထုတ်ပါ။

---

# Suggested 4-Week Study Plan

## Week 1

- Module 1: Java overview and first program
- Module 2: Variables and data types
- Module 3: Operators
- Daily practice: small print and calculation programs

## Week 2

- Module 4: if/else
- Module 5: loops
- Module 8: user input
- Daily practice: grade checker, even/odd checker, multiplication table

## Week 3

- Module 6: methods
- Module 7: arrays
- Daily practice: sum, average, largest number, reusable methods

## Week 4

- Module 9: classes and objects
- Module 10: constructors
- Module 11: OOP basics
- Module 12: mini projects

---

# Beginner Tips

- Code ကို ဖတ်ရုံမဟုတ်ဘဲ ကိုယ်တိုင် ရိုက်ပြီး run လုပ်ပါ။
- Error message ကို ကြောက်စရာမလိုပါ။ Error message သည် ပြဿနာကို ရှာဖွေရန် clue ဖြစ်သည်။
- တစ်နေ့ 30 မိနစ်မှ 1 နာရီအထိ ပုံမှန်လေ့ကျင့်ပါ။
- Example code ကို copy/paste မလုပ်ဘဲ ကိုယ်တိုင် ရိုက်ပါ။
- Project အသေးစားများကို မကြာခဏ လုပ်ပါ။

# Final Checklist

ဤ checklist ကို သင်တတ်မြောက်မှု စစ်ဆေးရန် အသုံးပြုပါ။

- [ ] Java file တစ်ခုရေးပြီး compile/run လုပ်နိုင်သည်။
- [ ] Variables နှင့် data types များ အသုံးပြုနိုင်သည်။
- [ ] if/else ဖြင့် condition စစ်နိုင်သည်။
- [ ] for/while loop များရေးနိုင်သည်။
- [ ] Method များရေးပြီး ခေါ်နိုင်သည်။
- [ ] Array ထဲမှ data များကို loop ဖြင့် သုံးနိုင်သည်။
- [ ] Scanner ဖြင့် user input ယူနိုင်သည်။
- [ ] Class နှင့် object အခြေခံကို နားလည်သည်။
- [ ] Constructor အသုံးပြုနိုင်သည်။
- [ ] Beginner mini project တစ်ခုကို ကိုယ်တိုင် တည်ဆောက်နိုင်သည်။
