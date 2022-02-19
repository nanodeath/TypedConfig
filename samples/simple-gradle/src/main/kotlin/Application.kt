import com.example.GeneratedConfig

fun main() {
    val config = GeneratedConfig.default()
    val greeting = config.greeting
    println(greeting)
    println("Configuration was compiled and loaded successfully!")
    println("If you run this application with the GREETING environment variable set,")
    println("that will be printed instead.")
}
