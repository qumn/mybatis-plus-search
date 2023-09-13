package io.github.qumn.mybatis.plus.search.dto;

import io.github.qumn.mybatis.plus.search.annotation.BETWEEN
import io.github.qumn.mybatis.plus.search.annotation.GT
import io.github.qumn.mybatis.plus.search.annotation.Operation
import java.time.Instant

data class PersonSearchReq(
    val id: Long? = null,
    @Operation(GT::class) val age: Int? = null,
    val name: String? = null,
    @Operation(BETWEEN::class) val createAt: Array<Instant>? = null,
)
