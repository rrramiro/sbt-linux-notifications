package fr.ramiro.sbt.notifications

import java.nio.file.Files

import sbt._
import sbt.Keys._

import scala.language.postfixOps

object NotificationsPlugin extends AutoPlugin {

  private val defaultImagePath = file(System.getProperty("user.home")) / ".sbt" / "notifications" / "icons"
  private val defaultErrorImageName = "error.png"
  private val defaultPassedImageName = "passed.png"
  private val defaultFailedImageName = "failed.png"

  private implicit class FileWrapper(file: File) {
    def toOption: Option[File] = if (file.exists()) { Some(file) } else { None }
  }

  private def installDefaultIcons() = {
    if (!defaultImagePath.exists()) {
      defaultImagePath.mkdirs()
    }
    for (
      imageFile <- Seq(defaultErrorImageName, defaultPassedImageName, defaultFailedImageName);
      targetFile = defaultImagePath / imageFile if !targetFile.exists()
    ) {
      Files.copy(this.getClass.getClassLoader.getResourceAsStream(imageFile), targetFile.toPath)
    }
  }

  object autoImport {
    val notificationsDefaultImagePath = SettingKey[File]("notifications-default-image-path", "Default path used to resolve test images.")
    val notificationsErrorImagePath = SettingKey[File]("notifications-error-image-path", "Default path used to resolve 'error' image.")
    val notificationsPassedImagePath = SettingKey[File]("notifications-passed-image-path", "Default path used to resolve 'passed' image.")
    val notificationsFailedImagePath = SettingKey[File]("notifications-failed-image-path", "Default path used to resolve 'failed' image.")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    testListeners <+= {
      (notificationsErrorImagePath, notificationsPassedImagePath, notificationsFailedImagePath).map {
        (errorImagePath, passedImagePath, failedImagePath) =>
          installDefaultIcons()
          new NotificationsTestsListener((errorImagePath.toOption.map {
            TestResult.Error -> _
          } ++ passedImagePath.toOption.map {
            TestResult.Passed -> _
          } ++ failedImagePath.toOption.map {
            TestResult.Failed -> _
          }).toMap)
      }
    },
    notificationsDefaultImagePath := defaultImagePath,
    notificationsErrorImagePath := notificationsDefaultImagePath.value / defaultErrorImageName,
    notificationsPassedImagePath := notificationsDefaultImagePath.value / defaultPassedImageName,
    notificationsFailedImagePath := notificationsDefaultImagePath.value / defaultFailedImageName
  )
}
