@file:Suppress("unused")

import com.example.AppConfig
import com.example.DbConfig

fun main() {
    val appPort = AppConfig.default().port
    val dbPort = DbConfig.default().port
    println("Starting application on port $appPort.")
    println("Connecting to database on port $dbPort.")
    println("...just kidding, it's only an example.")
}
