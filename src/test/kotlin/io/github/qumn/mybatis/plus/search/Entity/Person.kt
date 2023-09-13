package io.github.qumn.mybatis.plus.search.Entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import java.time.Instant

enum class Gender {
    FEMALE,
    MALE
}

data class Person(
    @field:TableId(value = "uid")
    var id: Long? = null,
    @field:TableField(value = "uname")
    var name: String,
    var createAt: Instant = Instant.now(),
    val age: Int,
)
