package xyz.qumn.mybatis.plus.search.annotation

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.toolkit.support.SFunction

fun <T, R, D> handle(wp: AbstractWrapper<T, R, *>, entity: Class<T>, searchDto: D) {
    val entityFields = entity.fields.toList()
    val members = searchDto!!::class.java.fields.toList()
    for (entityField in entityFields) {
        val dtoGet = members.find { it.name == entityField.name }
        val value = dtoGet?.get(searchDto) ?: continue
        wp.eq(dtoGet, value)
    }
}

class OperationHandler {
}