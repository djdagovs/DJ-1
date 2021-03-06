package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import monitoring.Reporting

object MonitorController extends Controller {

    def monitorSource() = Action {
        Ok.feed( Reporting.monitoringEnumerator.through( Reporting.toEventSource ) ).as( "text/event-stream" )
    }
}