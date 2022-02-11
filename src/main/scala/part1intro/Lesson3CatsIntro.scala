package part1intro

object Lesson3CatsIntro {

  // Typeclass Eq
  // val aComparison = 2 == "a string" // => different types and it's always false. This is wrong, will trigger
  //                                         a compiler warning, will always return false

  // part 1 - type class import
  import cats.Eq

  // part 2 - import type class instances for the types you need
  import cats.instances.int._

  // part 3 - use the type class API
  val intEquality = Eq[Int]

  val aTypeSafeComparison = intEquality.eqv(2, 3) // false
  // val anUnsafeComparison = intEquality.eqv(2, "a string") // -- doesn't compile!

  // part 4 - use extension methods (if applicable)
  import cats.syntax.eq._

  val anotherTypeSafeComparison = 2 === 3 // returns false
  val notEqComparison = 2 =!= 3 // returns true
  // val invalidComparison = 2 === "a string" // this doesn't compile
  // Note: extension methods are only visible in the presence of the right TypeClass instance

  // part 5 - extending the TypeClass operations to composite types, e.g. lists
  import cats.instances.list._
  val aListComparison = List(2) === List(3, 4)

  // part 6 - create a TypeClass instance for a custom type
  case class ToyCar(model: String, price: Double)

  implicit val toyCarEq: Eq[ToyCar] = Eq.instance[ToyCar] { (car1, car2) =>
    car1.price == car2.price
  }

  val compareTwoToyCars = ToyCar("Ferrari", 29.99) === ToyCar("Lamborghini", 29.99)

}
