package part1intro

object Lesson4TypeClassVariance {

  import cats.Eq
  import cats.instances.int._ // bring Eq[Int] TypeClass instance
  import cats.instances.option._ // construct a Eq[Option[Int]] TypeClass instance
  import cats.syntax.eq._

  val aComparison = Option(2) === Option(3)
  // val anInvalidadComparison = Some(2) === None // Eq[Some[Int]] not found

  // part 1 - introduce variance
  class Animal
  class Cat extends Animal

  // covariant type: subtyping is propagated to the generic type
  class Cage[+T]
  val cage: Cage[Animal] = new Cage[Cat] // Cat <: Animal, so Cage[Cat] <: Cage[Animal]

  // contravariant type: subtyping is propagated BACKWARDS to the generic type.
  // Usually, contravariant types are actions types or something that can do something to an `Animal` or `Cat`.

  class Vet[-T]
  val vet: Vet[Cat] = new Vet[Animal] // Cat <: Animal, then Vet[Animal] <: Vet[Cat]

  // rule of thumb:
  // "HAS a T" => covariant
  // "ACTS on T" => contravariant

  // variance affect how TypeClasses instances are being fetched. For example,

  // a. Contravariant example
  trait SoundMaker[-T]
  implicit object AnimalSoundMaker extends SoundMaker[Animal]

  def makeSound[T](implicit soundMaker: SoundMaker[T]): Unit = println(
    "wow"
  ) // implementation not important

  makeSound[Animal] // it's OK - TypeClass instance defined above => `implicit object AnimalSoundMaker extends SoundMaker[Animal]`
  makeSound[Cat] // it's OK - TypeClass instance for Animal is also applicable for Cats
  // rule 1: contravariant TypeClasses can use the superclass instance if nothing is available strictly for that type

  // has implications for subtypes
  implicit object OptionSoundMake extends SoundMaker[Option[Int]]
  makeSound[Option[Int]]
  makeSound[Some[Int]]

  // b. Covariant TypeClass
  trait AnimalShow[+T] {
    def show: String
  }

  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    override def show: String = "animal everywhere"
  }

  implicit object CatsShow extends AnimalShow[Cat] {
    override def show: String = "so many cats!"
  }

  def organizeShow[T](implicit event: AnimalShow[T]): String = event.show
  // rule 2: covariant TypeClasses will always use the more specific TC instance (Cat) for that type
  // << here the compiler inject the right TypeClass println(organizeShow[Cat]) >>
  // but may confuse the compiler if the general TypeClass (Animal) is also present
  // << here the compiler will confuse println(organizeShow[Animal]) >>

  // rule 3: you can't have both benefits
  // Cats uses INVARIANT TypeClasses
  Option(2) == Option.empty[Int]

  def main(args: Array[String]): Unit =
    println(organizeShow[Cat]) // ok - the compiler will inject CatsShow as implicit
  // println(organizeShow[Animal]) // will not compile - ambiguous values

  // Takeaway: Use the General Type & "smart" constructors e.g. Option.empty[Int]

}
