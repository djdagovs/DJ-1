package util

import models._
import play.api.libs.json._
import play.api.libs.concurrent._
import play.api.libs.ws._
import play.api.cache._
import play.api.Play.current
import controllers.Application

object LastFM {

    val emptyCover = "/assets/images/pict.png"

    def retrieveCoverArtFromLastFM(song: Song) = {

        WS.url("http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=b25b959554ed76058ac220b7b2e0a026&artist="
            + song.artist.replace(" ", "%20")
            + "&album=" + song.album.replace(" ", "%20")).get().onRedeem {
            value =>

                value.xml \\ "lfm" \\ "album" \\ "image" foreach {
                    image =>
                        (image \ "@size").text match {
                            case "large" => {
                                val url = image.text
                                url match {
                                    case "" => Cache.set(song.artist + song.album, emptyCover)
                                    case _ => {
                                        Cache.set(song.artist + song.album, url)
                                        Application.updateClientPlaying( )
                                    }
                                }
                            }
                            case _ => Unit
                        }
                }
        }
    }
}