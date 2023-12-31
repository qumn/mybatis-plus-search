package io.github.qumn.mybatis.plus.search.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Operation(val value: KClass<*> = EQ::class)

interface Operator {
    /**
     * the name of column is same as that set in tableField
     * @param value it is an array, if the field of searchDto be inherits from collection or array,
     *              the value should be destruction to the value. else the first of value array is the value of searchDto's field
     * @return Pair the first is the sql that should apply in wrapper, the second is the parameter of apply
     *         if the first is null, the Pair would be ignored
     */
    fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>>
}

class EQ : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String, Array<out Any>> {
        return Pair("$column = {0}", value)
    }

}

class NE: Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column <> {0}", value)
    }
}


class GT : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column > {0}", value)
    }
}

class GE: Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column >= {0}", value)
    }
}

class LT: Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column < {0}", value)
    }
}
class LE: Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column <= {0}", value)
    }
}


class LIKE : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column like {0}", arrayOf("%${value[0]}%"))
    }
}

class LEFTLIKE : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column like {0}", arrayOf("%${value[0]}"))
    }
}

class RIGHTLIKE : Operator {
    override fun doOperator(column: String, vararg value: Any): Pair<String?, Array<out Any>> {
        return Pair("$column like {0}", arrayOf("${value[0]}%"))
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
