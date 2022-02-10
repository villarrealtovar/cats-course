package part0Playground

import cats.Eval

object Playground {
  val meaningOfLife = Eval.later {
    println("Learning Cats: computing abstractions and the meaning of the life...")
    40
  }

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 43
    case 8 => 56
  }



  def main(args: Array[String]): Unit = {
    aPartialFunction(1)
    println(meaningOfLife.value)
  }
}
