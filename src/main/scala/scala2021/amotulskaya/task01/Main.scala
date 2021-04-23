package scala2021.amotulskaya.task01

object Main {
  def main(args: Array[String]): Unit = {
    val input = Array(
      "900,google.com",
      "60,mail.yahoo.com",
      "10,mobile.sports.yahoo.com",
      "40,sports.yahoo.com",
      "10,stackoverflow.com",
      "2,en.wikipedia.org",
      "1,es.wikipedia.org",
      "1,mobile.sports"
    )
    //Array[String]()
    //Array(
    //      "900,google.com",
    //      "60,mail.yahoo.com",
    //      "10,mobile.sports.yahoo.com",
    //      "40,sports.yahoo.com",
    //      ",stackoverflow.com",
    //      "2,en.wikipedia.org",
    //      "1,es.wikipedia.org",
    //      "1,mobile.sports",
    //      ","
    //    )
    println(input.mkString("|"))
    val output = process(input)
    if (!output.isEmpty) {
      println(output.mkString("\n"))
    } else {
      println("Empty Input")
    }

  }


  def process(array: Array[String]): Array[(String, Int)] = {
    //Array[(String, Array(String))]
    val arraySpl = array.map { s =>
      val split = s.split(",")
      val fixedSplit = Array(if(split.length>0){split(0)}else{""},if(split.length>1){split(1)}else{""})
      (fixedSplit(0), splitAndAccumulateReverse(fixedSplit(1)).groupBy(identity).mapValues(_.length).map(identity).toMap)
      //(split(0), splitAndAccumulateReverse(split(1)).groupBy(identity).mapValues(_.length).map(identity).toMap)
      //(value, array of string)
    }
    println(arraySpl.mkString(" "))
    /*
        arraySpl.flatMap{
          case(k,theMap) => theMap.map(e => (k, e._1,e._2))
        }.foreach{ case(v, k1,k2) => println(s"value [$v] [$k1] - [$k2]'") }
    */
    val arrayCnt = arraySpl.flatMap {
      case (k, theMap) => theMap.map(e => (k, e._1, e._2))
    }.map { case (v, k1, k2) => (k1, if (!v.isEmpty) { v.toInt } else { 0 } * k2.toInt) } //.toMap - Q: why losing entries?
    println(arrayCnt.mkString(" "))

    val arrayGrpd = arrayCnt.groupBy(_._1).mapValues(_.map(_._2).sum).toList
    println(arrayGrpd.mkString("|"))

    val arraySrtd = scala.util.Sorting.stableSort(arrayGrpd, (e1: (String, Int), e2: (String, Int)) => e1._1.reverse < e2._1.reverse)//.toArray
    //println(arraySrtd.mkString("|"))
    arraySrtd
  }

  def splitAndAccumulateReverse(string: String) = {
    if (!string.isEmpty) {
      val s = string.split("\\.").reverse //.mkString(".").split("\\.")
      s.tail.scanLeft(s.head) { case (acc, elem) =>
        elem + "." + acc
      }
    } else {
      Array("None"):Array[String]
    }
  }

  def splitAndAccumulate(string: String) = {
    val s = string.split("\\.")
    s.tail.scanLeft(s.head) { case (acc, elem) =>
      acc + "." + elem
    }
  }

}
