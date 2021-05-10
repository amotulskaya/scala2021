package scala2021.amotulskaya.task06

import cats.data.Validated.{Invalid, Valid}
import scala2021.amotulskaya.task06.Form.Gender._
import scala2021.amotulskaya.task06.Form._

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  /*
  * С веб-формы вам приходит объект с полями name:String, age: Int, email: String, sex: Male or Female(изучите как реализовать энумы в scala), height: Double.
Вам необходимо имплементировать валидацию данных по следующим правилам:
Name - только латинские буквы, non-empty
Age - больше нуля и меньше 100
Email - стандартная валидация на regexp, может быть empty(тогда не выводить ошибку)
Если sex = Male то рост должен быть больше 100 (надуманный случай но хочется затронуть контекстно-зависимую валидацию)

Валидацию необходимо имплементировать в нескольких вариантах :
Выдавать только первую ошибку
Выводить все возможные ошибки списком
Выводить все возможные ошибки, и проводить валидацию каждого поля параллельно(in parallel)

* */

  println(validateForm("user", 9, "email@dot.com", Male,100))
  println(validateForm("user", 0, "email@ com", Female,0))

  val result = validateForm("user", 0, "email@ com", Female,0)
  val temp = result match {
    case Valid(_)        => List()
    case Invalid(errors) => errors.map(x => x.err)//.toNonEmptyList
  }
  println(temp)
  //println(validateForm("user", 0, "email@ com", Female,0).toEither.map())

  println(validateFormPar("user", 0, "email@ com", Female,0).onComplete(println))
}
