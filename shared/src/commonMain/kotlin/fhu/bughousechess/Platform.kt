package fhu.bughousechess

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform