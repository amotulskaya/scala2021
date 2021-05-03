package scala2021.amotulskaya.task04

object Main {
  def main(args: Array[String]): Unit = {
    val input = 5
    val coins = List(2, 4, 6)

    println(input)
    val output1 = factorizeSimple(input)
    println(s"Result simple factors:\n${output1}")

    val output2 = factorize(input)
    println(s"Result factors:\n${output2}")

    //Tricky way without filters (of filter out all non-existent coins)
    //val lst = List.range(1, input)
    //println(s"Result list:\n${lst}")

    val lst = change(input, coins)
    println(s"${5} in coins ${coins.toString()}")
    println(s"${if(lst){"can"}else{"can't"}} be exchanged")
  }

  def change(k: Int, coins: List[Int]): Boolean = {
    def delit(k: Int, coins: List[Int]): Boolean = {
      if (!coins.isEmpty) {
        val a = coins.head
        (k % a) match {
          case (0) => true
          case (_) => delit(k - a * (k / a), coins.tail)
        }
      } else false
    }

    delit(k, coins)
  }

  def factorizeSimple(x: Int): List[Int] = {
    //Simple
    def delit(x: Int, a: Int): List[Int] = {
      (a * a < x, x % a) match {
        case (true, 0) => a :: delit(x / a, a)
        case (true, _) => delit(x, a + 1)
        case (false, _) => List(x)
      }
    }

    delit(x, 2)
  }

  def factorize(x: Int): List[Int] = {
    def delit(x: Int, a: Int): List[Int] = {
      ((a < x) & (a > 1), x % a) match {
        case (true, 0) => a :: delit(x / a, a)
        case (true, _) => delit(x, a - 1)
        case (false, _) => List(x)
      }
    }

    delit(x, x - 1)
  }

}
