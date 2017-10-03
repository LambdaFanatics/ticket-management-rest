package model

object Errors {
  sealed abstract class Error(code: Int, message: String) {
  }

  case object GenericError extends Error(0, "Application generic error")

  case object TicketValidateError extends Error(300, "Invalid ticket fields")

  case object StorageError extends Error(400, "Storage generic error")
  case object StorageRetrieveError extends Error(401, "Storage retrieve error")
  case object StorageCreateError extends Error(402, "Storage create error")
  case object StorageUpdateError extends Error(403, "Storage update error")



  case object TicketRetrieveError extends Error(120, "Unable to retrieve ticket")
  case object TicketOpenError extends Error(100, "Unable to open ticket")
  case object TicketStartError extends Error(101, "Unable to start ticket")
  case object TicketCloseError extends Error(102, "Unable to close ticket")
  case object TicketChangeError extends Error(103, "Unable to change ticket")

  case object EntityExistsError extends Error(200, "Entity already exists")
  case object EntityMissingError extends Error(201, "Entity is missing")
}
