package scala2021.amotulskaya.task06

import cats.data.{EitherT, ValidatedNec}

import scala.concurrent.Future
import scala.util.Try
import cats.implicits._

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

  sealed trait DVal {
    def err: String
  }

  case object NameIsInvalid extends DVal {
    override def err: String = "Name is invalid"
  }

  case object AgeIsInvalid extends DVal {
    override def err: String = "Age is invalid"
  }

  type ValidationResult[A] = ValidatedNec[DVal, A]

  def valName(name: String): ValidationResult[String] = if (name.matches("^[a-z]+$")) name.validNec else NameIsInvalid.invalidNec

  def valAge(age: Int): ValidationResult[Int] = if (age > 0) age.validNec else AgeIsInvalid.invalidNec

  case class User(name: String, age: Int)

  def validateForm(name: String, age: Int): ValidationResult[User] =
    (valName(name), valAge(age)).mapN(User)

  def validateFormPar(name: String, age: Int): Future[ValidationResult[User]] = {
    val nameF = Future(valName(name))
    val ageF = Future(valAge(age))

    for {
      name <- nameF
      age <- ageF
    } yield (name, age).mapN(User)
  }

  println(validateForm("user", 9))
  println(validateForm("user", 0))

  object WeekDay extends Enumeration {
    type WeekDay = Value
    val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
  }

  import WeekDay._

  def isWorkingDay(d: WeekDay) = !(d == Sat || d == Sun)

  WeekDay.values filter isWorkingDay foreach println

}
