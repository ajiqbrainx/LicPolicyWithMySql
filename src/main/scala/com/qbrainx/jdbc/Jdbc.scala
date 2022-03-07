package com.qbrainx.jdbc

import com.qbrainx.AppConfig
import com.qbrainx.model.LicPolicy


import java.sql.{DriverManager, ResultSet}
import scala.collection.mutable.ArrayBuffer

object Jdbc extends App {

  val dbConf = AppConfig.jdbc

  val driverClass = dbConf.getString("driverClass")
  val url = dbConf.getString("url")
  val username = dbConf.getString("username")
  val password = dbConf.getString("password")


  val insert: List[LicPolicy] = List(LicPolicy(3, "AKil", 31314321, Some("Ajil")))

  Class.forName(driverClass)

  //     createTable()
//  insertIntoTable(insert)
  selectFromTable().foreach(println)
//  deleteFromId(3)
//    updateIdFromName(4,"Aji")
//  updateIdFromMobileno(2,123213123)
  updateIdFromNominee(2,"Akil")

  def createTable(): Unit = {
    val connection = getConnection
    val statement = connection.createStatement()
    try {
      statement.executeUpdate {
        """
          |CREATE TABLE licpolicy(
          |PolicyId INT PRIMARY KEY,
          |Name VARCHAR(30) NOT NULL,
          |MobileNo INT,
          |Nominee VARCHAR(10))
          |""".stripMargin
      }
    } catch {
      case ex: Throwable => ex.printStackTrace()
    } finally {
      statement.close()
      connection.close()
    }
  }

  def insertIntoTable(policy: List[LicPolicy]): Unit = {
    val connection = getConnection
    val stmnt = connection.createStatement()
    try {
      policy.map {
        case LicPolicy(id, name, mobile, Some(nominee)) =>
          s"INSERT INTO licpolicy(PolicyId, Name, MobileNO,Nominee) VALUES ($id, '$name',$mobile, '$nominee')"
        case LicPolicy(id, name, mobile, None) =>
          s"INSERT INTO licpolicy(PolicyId, Name, MobileNO) VALUES ($id, '$name',$mobile)"
      }.foreach { query =>
        val result = stmnt.executeUpdate(query)
        println("Insert Result " + result)
      }
    } catch {
      case ex: Throwable => ex.printStackTrace()
    } finally {
      stmnt.close()
      connection.close()
    }
  }

  def selectFromTable(): List[LicPolicy] = {
    val connection = getConnection
    val stmnt = connection.createStatement()
    try {
      val rs: ResultSet = stmnt.executeQuery("SELECT * FROM licpolicy")
      val buffer= ArrayBuffer[LicPolicy]()
      while (rs.next()) {
        val student = LicPolicy(rs.getInt("PolicyId"), rs.getString("Name"), rs.getInt("MobileNo"), Option(rs.getString("Nominee")))
        buffer += student
      }
      buffer.toList
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
        List.empty
    } finally {
      stmnt.close()
      connection.close()
    }
  }

  def deleteFromId(id:Int) = {
    val connection = getConnection
    val stmnt = connection.createStatement()
    try {
      val delete = stmnt.executeUpdate {
        s"""
           |DELETE FROM licpolicy WHERE PolicyId=$id
           |""".stripMargin
      }
      println(s"Your Are Deleting this ID== $delete")
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
        List.empty
    } finally {
      stmnt.close()
      connection.close()
    }
  }
  def updateIdFromName(id:Int,name:String) = {
    val connection = getConnection
    val stmnt = connection.createStatement()
    try {
      val update = stmnt.executeUpdate {
        s"""
           |UPDATE licpolicy SET Name="$name"  WHERE PolicyId=$id
           |""".stripMargin
      }
      println(s"your Are Updating This == $update")
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
        List.empty
    } finally {
      stmnt.close()
      connection.close()
    }
  }

  def updateIdFromMobileno(id:Int,mobileNo:Int)={
    val connection=getConnection
    val statement=connection.createStatement()
    try{
      val update =statement.executeUpdate{
        s"""
          |UPDATE licpolicy SET MobileNo=$mobileNo WHERE PolicyID=$id
          |""".stripMargin
      }
    }
  }

  def updateIdFromNominee(id:Int,nominee:String)={
    val connection = getConnection
    val stmnt = connection.createStatement()
    try{
      val update=stmnt.executeUpdate{
        s"""
          |UPDATE licpolicy SET Nominee="$nominee" WHERE PolicyID=$id
          |""".stripMargin
      }
    }
  }

  def getConnection = DriverManager.getConnection(url, username, password)
}