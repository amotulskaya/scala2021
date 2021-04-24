package scala2021.amotulskaya.task03

object Main {
  def main(args: Array[String]): Unit = {
    val input: List[Symbol] = List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
    //Кодирование списка по длине.
    //Реализуем так называемый метод сжатия данных с кодированием длин серий.
    //Пример на консоле:
    //     scala> encodeDirect(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
    //     res0: List[(Int, Symbol)] = List((4,'a), (1,'b), (2,'c), (2,'a), (1,'d), (4,'e))
    println(input)
    val output = process(input)
    println(s"Result:\n${output}")
  }

  def process(lst: List[Symbol]): List[(Int, Symbol)] = {
    //encodeDirect(lst)
    encodeDirect(lst)
  }

  /*
    def encodeDirect(lst: List[Symbol]): List[(Int, Symbol)] = {
      lst.groupBy(identity).mapValues(_.length).map(identity).map(_.swap).toList
    }
  */
  def encodeDirect(lst: List[Symbol]): List[(Int, Symbol)] = {
    def _encodeDirect(res: List[(Int, Symbol)], rem: List[Symbol]): List[(Int, Symbol)] = rem match {
      case Nil => res
      case ls => {
        val (s, r) = rem span {
          _ == rem.head
        }
        _encodeDirect(res ::: List((s.length, s.head)), r)
      }
    }

    _encodeDirect(List(), lst)
  }

}
