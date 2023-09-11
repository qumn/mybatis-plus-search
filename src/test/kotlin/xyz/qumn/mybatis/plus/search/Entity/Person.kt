package xyz.qumn.mybatis.plus.search.Entity

enum class Gender {
    FEMALE,
    MALE
}

data class Person(
    var id: Long? = null,
    val name: String,
    val age: Int,
)
