package xyz.qumn.mybatis.plus.search.annotation

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField


val tableCache = TableCache()

// Convenient for use in kotlin
inline fun <reified T, D> AbstractWrapper<T, *, *>.setSearchCondition(searchDto: D) {
    setSearchCondition(this, T::class.java, searchDto)
}

/**
 * the fields in searchDto need to match the names in entity
 * if the field have no Operation annotation, the default operate is EQ
 * if the field have Operation, use the operator of operation
 */
fun <T, D> setSearchCondition(wp: AbstractWrapper<T, *, *>, entity: Class<T>, searchDto: D) {
    if (searchDto == null) return
    val searchDtoGetMethods =
        searchDto!!::class.java.methods.filter { it.name.startsWith("get") && it.name != "getClass" }
    for (searchDtoGetMethod in searchDtoGetMethods) {
        val fieldName = getFieldNameByGetMethod(searchDtoGetMethod.name)
        val columnName = tableCache.getColumnName(entity, fieldName) ?: continue
        val value = searchDtoGetMethod.invoke(searchDto) ?: continue
        val operator = getOperator(searchDto!!::class, fieldName)
        val (filterSql, parameter) = when (value) {
            is Array<*> -> operator.doOperator(columnName, *(value as Array<out Any>))
            is Collection<*> -> operator.doOperator(columnName, *(value.toTypedArray() as Array<out Any>))
            else -> operator.doOperator(columnName, value)
        }
        if (filterSql != null) wp.apply(filterSql, *parameter)
    }
}

private fun getOperator(kClass: KClass<*>, fieldName: String): Operator {
    val kProperty = kClass.memberProperties.find { it.name == fieldName }
        ?: throw RuntimeException("can't find $fieldName in $kClass")
    val operator = kProperty.findAnnotation<Operation>()?.value
        ?: kProperty.javaField?.getAnnotation(Operation::class.java)?.value
        ?: EQ::class
    return operator.primaryConstructor!!.call() as Operator
}

private fun getFieldNameByGetMethod(methodName: String): String {
    return methodName.substring(3).replaceFirstChar { it.lowercase() }
}

class TableCache {
    // tableName -> <fieldName -> columnNames>
    private val tableColumns: MutableMap<Class<*>, Map<String, ColumnCache>> = mutableMapOf()

    fun getColumnName(clazz: Class<*>, columnName: String): String? {
        if (clazz !in tableColumns) {
            val columnMap = LambdaUtils.getColumnMap(clazz)
                ?: throw java.lang.RuntimeException("place first add the $clazz mapper to mybatis plus")
            tableColumns[clazz] = columnMap
        }
        return tableColumns[clazz]?.get(columnName.uppercase())?.column
    }
}


