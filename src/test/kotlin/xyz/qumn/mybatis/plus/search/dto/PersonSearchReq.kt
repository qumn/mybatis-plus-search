package xyz.qumn.mybatis.plus.search.dto;

import xyz.qumn.mybatis.plus.search.annotation.BETWEEN
import xyz.qumn.mybatis.plus.search.annotation.GT
import xyz.qumn.mybatis.plus.search.annotation.LIKE
import xyz.qumn.mybatis.plus.search.annotation.Operation
import java.time.Instant

data class PersonSearchReq(
    val id: Long? = null,
    @Operation(GT::class) val age: Int? = null,
    @Operation(LIKE::class) val name: String? = null,
    @Operation(BETWEEN::class) val createAt: Array<Instant>? = null,
)
