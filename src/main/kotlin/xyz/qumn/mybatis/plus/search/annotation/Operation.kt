package xyz.qumn.mybatis.plus.search.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Operation(val value: KClass<*> = EQ::class)

interface Operator {
    fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>>
}

class EQ : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String, Array<out Any>> {
        return Pair("$column = {0}", value)
    }
}

class GT : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column > {0}", value)
    }
}

class LIKE : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column like '%{0}%'", value)
    }
}

class IN : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        if (value.isEmpty()) return Pair(null, value)
        val sql = buildString {
            append("$column in (")
            for (i in value.indices) {
                if (i == 0) {
                    append("{$i}")
                } else {
                    append(", {$i}")
                }
            }
            append(")")
        }
        return Pair(sql, value)
    }
}

class BETWEEN : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        if (value.isEmpty()) return Pair(null, value)
        return Pair("$column between {0} and {1}", value)
    }
}
