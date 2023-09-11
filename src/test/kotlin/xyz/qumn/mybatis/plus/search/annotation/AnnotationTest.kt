package xyz.qumn.mybatis.plus.search.annotation

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import xyz.qumn.mybatis.plus.search.Entity.Person
import xyz.qumn.mybatis.plus.search.dto.PersonSearchReq

class AnnotationTest : StringSpec ({
    "kotest should world"{
        1 shouldBe 1
    }
    "should work" {
        val personSearchReq = PersonSearchReq(name = "zs")
        val wp = QueryWrapper<Person>()
        handle(wp, Person::class.java, personSearchReq)
    }
})