package fr.ramiro.sbt.notifications

import sbt.SuiteResult

import scala.collection.mutable

class NotificationsTestsCounter {
  object TestState extends Enumeration {
    type TestState = Value
    val Tests = Value(0)
    val Passed = Value(1)
    val Failed = Value(2)
    val Error = Value(3)
    val Skipped = Value(4)
  }
  import TestState._

  val counterMap = new mutable.HashMap[TestState.Value, Int]

  def init = counterMap.clear()

  def all = counterMap.values.sum

  def increment(result: SuiteResult) = {
    Seq(
      Error -> result.errorCount,
      Passed -> result.passedCount,
      Failed -> result.failureCount,
      Skipped -> result.skippedCount
    ).foreach {
        case (state, incr) =>
          counterMap.put(state, counterMap.getOrElse(state, 0) + incr)
      }
  }

  override def toString: String = {
    counterMap += (Tests -> all)
    TestState.values.toSeq.sortBy(_.id).map { state => s"${counterMap.getOrElse(state, 0)} $state" }.mkString(" ")
  }
}
