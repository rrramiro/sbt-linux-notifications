package fr.ramiro.sbt.notifications

import sbt.{ SuiteResult, TestEvent, TestsListener, _ }
import sys.process.stringSeqToProcess
import scala.language.postfixOps

class NotificationsTestsListener(imageMap: Map[TestResult.Value, File]) extends TestsListener {

  private val counter = new NotificationsTestsCounter

  override def doInit(): Unit = counter.init

  override def doComplete(finalResult: TestResult.Value): Unit = {
    ("notify-send" +: (imageMap.get(finalResult).toSeq.flatMap { filePath =>
      Seq("-i", filePath.getAbsolutePath)
    } ++ Seq(
      finalResult.toString,
      counter.toString
    ))) !
  }

  override def testEvent(event: TestEvent): Unit = counter.increment(SuiteResult(event.detail))

  override def endGroup(name: String, t: Throwable): Unit = {}

  override def endGroup(name: String, result: TestResult.Value): Unit = {}

  override def startGroup(name: String): Unit = {}

}