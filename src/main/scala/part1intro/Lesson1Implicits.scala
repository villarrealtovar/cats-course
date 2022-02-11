package part1intro

object Lesson1Implicits {

  // 1. implicit classes
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name!"
  }

  implicit class ImpersonableString(name: String) {
    def greet: String = Person(name).greet
  }

  // explicit way calling the `greet` method on `ImpersonableString` class
  val impersonableString = new ImpersonableString("Peter")
  impersonableString.greet

  // implicit way
  val greeting = "Peter".greet // => `extension method` pattern

  // 2. importing implicit conversions in scope
  import scala.concurrent.duration._
  val oneSecond = 1.second

  // 3. implicit arguments and values
  def increment(x: Int)(implicit amount: Int): Int = x + amount

  implicit val defaultAmount = 10

  val incremented2 = increment(
    2
  ) // the compiler completes and adding (10). Thus, it becomes increment(2)(10)
  // implicit argument 10 is passed by the compiler

  def multiply(x: Int)(implicit times: Int) = x * times
  val times2 = multiply(2)

  // 4. more complex examples
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(serializer.toJson(_)).mkString("[", ",", "]")

  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {

    override def toJson(person: Person): String =
      s"""
         |{
         |"name": "${person.name}"
         |}
         |""".stripMargin.trim
  }

  val personJson = listToJson(List(Person("Andres"), Person("Caro")))

  /**    NOTE: Implicit argument is used to PROVE THE EXISTENCE of a type
    */

  // 5. Implicit methods
  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {

    override def toJson(value: T): String =
      s"""
         |{
         |"${value.productElementName(0)}" : "${value.productElement(0)}"
         |}
         |""".stripMargin.trim
  }

  case class Cat(catName: String)

  val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))
  // in the background the compiler does:
  // listToJson(List(Cat("Tom"), Cat("Garfield")))(oneArgCaseClassSerializer[Cat])

  /**    NOTE: Implicit methods are used to PROVE THE EXISTENCE of a type
    */

  // 6. can be used for implicit conversions (DISCOURAGED)

  def main(args: Array[String]): Unit = {
    println(oneArgCaseClassSerializer[Cat].toJson(Cat("Garfield")))
    println(oneArgCaseClassSerializer[Person].toJson(Person("Thiago")))

    List(1, 2, 3).sorted
  }
}
