package scala2021.amotulskaya.task02

object Main {
  def main(args: Array[String]): Unit = {
    val input = "if((2+x)*(3-y)==3)".toList //List[Char]
    //if((2+x)*(3-y)==3)
    //Я сказал ему (это еще (не) сделано). (Но он не послушал)
    //:-) false
    //())(
    println(input)
    val output = process(input)
    if (!output) {
      println("False\n")
    } else {
      println("True\n")
    }
  }

  def process(lst: List[Char]): Boolean = {
    bracketsCounter(lst, '(', ')')
  }

  def bracketsCounter(lst: List[Char], brL: Char, brR: Char): Boolean = {
    def bracketsCounterInner(lst: List[Char], brL: Char, brR: Char, cnt: Int): Boolean = {
      if (lst.isEmpty) cnt == 0
      else {
        if (lst.head == brL) bracketsCounterInner(lst.tail, brL, brR, cnt + 1)
        else if (lst.head == brR) cnt > 0 && bracketsCounterInner(lst.tail, brL, brR, cnt - 1)
        else bracketsCounterInner(lst.tail, brL, brR, cnt)
      }
    }
    bracketsCounterInner(lst, brL, brR, 0)
  }

}
