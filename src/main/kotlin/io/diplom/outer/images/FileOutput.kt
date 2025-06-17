package io.diplom.outer.images

data class FileOutput(
    val uri: String,
    val filename: String
) {
    companion object {
        fun empty() = FileOutput("", "")
    }
}
