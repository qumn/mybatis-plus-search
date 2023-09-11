package xyz.qumn.mybatis.plus.search.annotation

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache


val tableCache = TableCache()

fun <T, D> handle(wp: AbstractWrapper<T, *, *>, entity: Class<T>, searchDto: D) {
    val searchDtoGetMethods =
        searchDto!!::class.java.methods.filter { it.name.startsWith("get") && it.name != "getClass" }
    for (searchDtoGetMethod in searchDtoGetMethods) {
        val fieldName = getFieldNameByGetMethod(searchDtoGetMethod.name)
        val columnName = tableCache.getColumnName(entity, fieldName) ?: continue
        val value = searchDtoGetMethod.invoke(searchDto) ?: continue
        wp.apply("$columnName = {0}", value)
    }
}

class TableCache {
    // tableName -> <fieldName -> columnNames>
    private val tableColumns: MutableMap<Class<*>, Map<String, ColumnCache>> = mutableMapOf()

    fun getColumnName(clazz: Class<*>, columnName: String): String? {
        if (clazz !in tableColumns) {
            LambdaUtils.getColumnMap(clazz)?.let {
                tableColumns[clazz] = it
            }
        }
        return tableColumns[clazz]?.get(columnName.uppercase())?.column
    }
}

private fun getFieldNameByGetMethod(methodName: String): String {
    return methodName.substring(3).replaceFirstChar { it.lowercase() }
}

class OperationHandler {
}

data class Person(val id: Long?, val age: Int)
data class PersonSearchReq(
    val id: Long? = null,
    val age: Int? = null,
    val name: String? = null
)

fun main() {
    val columnMap = LambdaUtils.getColumnMap(Person::class.java)
    println(columnMap)
    val wp = LambdaQueryWrapper<Person>()
    handle(wp, Person::class.java, PersonSearchReq(age = 1))

    println(wp.targetSql)
}