package scala2021.amotulskaya.task06

import cats.data.{EitherT, ValidatedNec}

import scala.concurrent.ExecutionContext.Implicits.global
import cats.implicits._

import scala.concurrent.Future

object Form {
  object Gender extends Enumeration {
    type Gender = Value
    val Male, Female = Value
  }

  sealed trait DVal {
    def err: String
  }

  case object NameIsInvalid extends DVal {
    override def err: String = "Name is invalid"
  }

  case object AgeIsInvalid extends DVal {
    override def err: String = "Age is invalid"
  }

  case object emailIsInvalid extends DVal {
    override def err: String = "Email is invalid"
  }

  case object heightIsInvalid extends DVal {
    override def err: String = "Height is invalid"
  }

  case object genderIsInvalid extends DVal {
    override def err: String = "Gender is invalid"
  }

  type ValidationResult[A] = ValidatedNec[DVal, A]

  def valName(name: String): ValidationResult[String] = if (name.matches("^[a-z]+$")) name.validNec else NameIsInvalid.invalidNec

  def valAge(age: Int): ValidationResult[Int] = if (age > 0) age.validNec else AgeIsInvalid.invalidNec

  def valEmail(email: String): ValidationResult[String] = if ( email.matches("^(.+)@(\\S+)$")) email.validNec else emailIsInvalid.invalidNec

  def valHeight(height: Double): ValidationResult[Double] = if (height > 100) height.validNec else heightIsInvalid.invalidNec

  import Gender._

  def valHeight(height: Double, sex: Gender): ValidationResult[Double] = if (height > 100 & sex == Male ) height.validNec else heightIsInvalid.invalidNec

  def valGender(sex: Gender): ValidationResult[Gender] = if (sex == Female  ||  sex == Male ) sex.validNec else genderIsInvalid.invalidNec

  //case class User(name: String, age: Int)
  case class User(name:String, age: Int, email: String, sex: Gender, height: Double)

  def validateForm(name: String, age: Int, email: String, sex: Gender, height: Double): ValidationResult[User] =
    (valName(name), valAge(age), valEmail(email), valGender(sex), valHeight(height,sex)).mapN(User)

  def validateFormPar(name: String, age: Int, email: String, sex: Gender, height: Double): Future[ValidationResult[User]] = {
    val nameF = Future(valName(name))
    val ageF = Future(valAge(age))
    val emailF = Future(valEmail(email))
    val sexF = Future(valGender(sex))
    val heightF = Future(valHeight(height))

    for {
      name <- nameF
      age <- ageF
      email <- emailF
      sex <- sexF
      height <- heightF
    } yield (name, age, email, sex, height).mapN(User)
  }
}
