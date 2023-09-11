package xyz.qumn.mybatis.plus.search.Entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId

enum class Gender {
    FEMALE,
    MALE
}

data class Person(
    @field:TableId(value = "uid")
    var id: Long? = null,
    @field:TableField(value = "uname")
    var name: String,
    val age: Int,
)
