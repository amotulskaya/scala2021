package scala2021.amotulskaya.task05

import cats.data.EitherT
import scala2021.amotulskaya.task05.model.{Department, Employee, Info, Manager}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Main {
  def main(args: Array[String]): Unit = {

    val employees = List(
      Employee(1, "Steve", 1),
      Employee(3, "Mark", 1),
      Employee(4, "Jane", 1),
      Employee(7, "Samuel", 2),
      Employee(10, "Igor", 2),
      Employee(11, "Naveen", 4),
      Employee(12, "Christy", 5),
      Employee(15, "Megan", 3)
    )
    val departments = List(
      Department(1, "Marketing"),
      Department(2, "Sales"),
      Department(3, "Research"),
      Department(4, "IT"),
    )
    val managers = List(
      Manager("Marketing", 1),
      Manager("Sales", 10),
      Manager("IT", 14),
    )

    // Найти имя менеджера департамента, в котором работает сотрудник по имени сотрудника
    def findManagerName(employee: String): Option[String] = {

      val managersExtended =
        for {manager <- managers
             user <- employees
             if user.name == employee
             if user.id == manager.employeeId}
          yield (user.id, user.name, manager.employeeId, user.departmentId, manager.department)

      val output =
        for {user <- employees
             manager <- managersExtended
             if user.id == manager._3}
          yield (user.name) // i.e. add this to a list

      Option(output.toString())
    }

    // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так
    def findManagerNameOrError(employee: String): Either[String, String] = {

      val empl =
        for {user <- employees if user.name == employee} yield (user)

      val dep =
        for {user <- empl
             dep <- departments
             if user.departmentId == dep.id}
          yield (dep)

      val empManager =
        for {manager <- managers
             edep <- dep
             user <- employees

             if manager.department == edep.name
             if user.id == manager.employeeId}
          yield (user.name) // i.e. add this to a list

      if (empManager.isEmpty) {
        if (empl.isEmpty) Left(s"No Employee with name ${employee}")
        else {
          if (dep.isEmpty) Left(s"No Departament for Employee with name ${employee}")

          else Left(s"No Manager for Employee with name ${employee}")
        }
      }
      else Right(empManager.toString())

    }

    def findUser(employee: String): Either[String, String] =
      Try((for {user <- employees if user.name == employee} yield (user)).toString()).map(Right(_)).getOrElse(Left(s"No Employee with name ${employee}"))

    def findDepart(employee: String): Either[String, String] =
      Try((for {user <- employees
                dep <- departments
                if user.departmentId == dep.id}
        yield (dep)).toString()).map(Right(_)).getOrElse(Left(s"No Departament for Employee with name ${employee}"))

    def findManName(employee: String): Either[String, String] =
      Try((for {manager <- managers
                edep <- departments
                user <- employees

                if manager.department == edep.name
                if user.id == manager.employeeId}
        yield (user.name)).toString()).map(Right(_)).getOrElse(Left(s"No Manager for Employee with name ${employee}"))

    // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так и сделать все это асинхронно
    def findManagerNameOrErrorAsync(employee: String): Future[Either[String, String]] = {
      Future.successful(findUser(employee))
      Future.successful(findDepart(employee))
      Future.successful(findManName(employee))
    }

    // Найти имя менеджера по имени сотрудника, в случае ошибки в данных - указать что именно не так и сделать каждую операцию асинхронной(операция = вызов репозитория)
    def findManagerNameOrErrorAsyncOperations(employee: String): Future[Either[String, String]] = {

      Future.successful(findUser(employee)) flatMap { eitherA =>
        Future.successful(findDepart(employee)) flatMap { eitherB =>
          Future.successful(findManName(employee)) flatMap { eitherC =>
            (eitherA, eitherB, eitherC) match {
              case (Right(a), Right(b), Right(c)) => Future(findManName(c))
              case (Left(err), _, _) => Future.successful(Left(err))
              case (_, Left(err), _) => Future.successful(Left(err))
              case (_, _, Left(err)) => Future.successful(Left(err))
            }
          }
        }
      }
    }

    // вывести список всех сотрудников, вместе с именем департамента и именем менеджера, если департамента или менеджера нет то использовать константу "Not Found"
    def findEmployeeManagers: List[Info] = {
      val nf = "Not Found"
      for {employee <- employees
           edep <- departments
           manager <- managers
           user <- employees
           if employee.departmentId == edep.id
           if edep.name == manager.department
           if manager.employeeId == user.id}
        yield Info(employee.name, edep.name, user.name)


      for {
        employee <- employees
        edep <- departments
        manager <- managers
        user <- employees
      }

        yield Info(employee.name, if (employee.departmentId == edep.id) {
          edep.name
        } else {
          nf
        }, if (edep.name == manager.department & manager.employeeId == user.id & manager.employeeId == user.id) {
          user.name
        } else {
          nf
        })
    }

    val empl = Seq("John", "Steve", "Mark", "Igor", "Christy", "Naveen", "Megan")
    for (e <- empl) {
      println(s"Result for ${e}: ${findManagerName(e)}")
      println(s"Result for ${e}: ${findManagerNameOrError(e)}")
      println(s"Result for ${e}: ${findManagerNameOrErrorAsync(e).onComplete(println)}")
      println(s"Result for ${e}: ${findManagerNameOrErrorAsyncOperations(e).onComplete(println)}")
    }
    println(s"${findEmployeeManagers}")


  }

}
