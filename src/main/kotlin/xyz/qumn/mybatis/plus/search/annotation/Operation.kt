package xyz.qumn.mybatis.plus.search.annotation

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Operation(val operator: KClass<*> = EQ::class)

interface Operator<T, V> {
    fun doOperator(wp: LambdaQueryWrapper<Any>, column: SFunction<T, *>, value: V)
}

class EQ : Operator<Any, Any> {
    override fun doOperator(wp: LambdaQueryWrapper<Any>, column: SFunction<Any, *>, value: Any) {
        wp.eq(column, value)
    }
}

class GT : Operator<Any, Comparable<*>> {
    override fun doOperator(
        wp: AbstractWrapper<Any, SFunction<Any, *>, *>,
        column: SFunction<Any, *>,
        value: Comparable<*>,
    ) {
        wp.gt(column, value)
    }
}

class LIKE : Operator<Any, Comparable<*>> {
    override fun doOperator(
        wp: AbstractWrapper<Any, SFunction<Any, *>, *>,
        column: SFunction<Any, *>,
        value: Comparable<*>,
    ) {
        wp.like(column, value)
    }

}


