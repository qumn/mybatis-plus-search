package io.github.qumn.mybatis.plus.search.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import io.github.qumn.mybatis.plus.search.Entity.Person
import io.github.qumn.mybatis.plus.search.annotation.setSearchCondition
import io.github.qumn.mybatis.plus.search.dto.PersonSearchReq
import io.github.qumn.mybatis.plus.search.mapper.PersonMapper
import io.github.qumn.mybatis.plus.search.util.getSessionFactory
import java.time.Duration
import java.time.Instant


class MybatisTest : StringSpec({
    lateinit var sessionFactory: SqlSessionFactory
    lateinit var session: SqlSession
    lateinit var personMapper: PersonMapper
    beforeAny {
        sessionFactory = getSessionFactory()
        session = sessionFactory.openSession(true)
        personMapper = session.getMapper(PersonMapper::class.java)
    }

    "mybatis plus should work" {
        val person = Person(name = "zs", age = 1)
        personMapper.insert(person)
        val personSelected = personMapper.selectById(person.id)

        personSelected shouldNotBe null
        personSelected.name shouldBe person.name
        personSelected.age shouldBe person.age
    }

    "handel should work" {
        val searchReq = PersonSearchReq(age = 3)
        val wp = LambdaQueryWrapper<Person>()
        setSearchCondition(wp, Person::class.java, searchReq)
        wp.targetSql shouldContain "age > ?"
    }

    "handle with multiple property should work" {
        getSessionFactory()
        val searchReq =
            PersonSearchReq(
                age = 3,
                name = "zs",
                createAt = arrayOf(Instant.now() - Duration.ofDays(1), Instant.now())
            )
        val wp = LambdaQueryWrapper<Person>()
        setSearchCondition(wp, Person::class.java, searchReq)
        wp.targetSql shouldContain "uname = ?"
        wp.targetSql shouldContain "create_at between ? and ?"
        wp.targetSql shouldContain "age > ?"
    }

    "should work" {
        val searchReq =
            PersonSearchReq(
                age = 3,
                name = "zs",
                createAt = arrayOf(Instant.now() - Duration.ofDays(1), Instant.now())
            )

        val wp = LambdaQueryWrapper<Person>()
        wp.setSearchCondition(searchReq)

        wp.targetSql shouldContain "uname = ?"
        wp.targetSql shouldContain "create_at between ? and ?"
        wp.targetSql shouldContain "age > ?"
        wp.targetSql shouldNotContain "uid = ?"
    }

    "sql should execute" {
        val p1 = Person(name = "zs", createAt = Instant.now(), age = 15)
        val p2 = Person(name = "zs1", createAt = Instant.now(), age = 17)
        val p3 = Person(name = "wu", createAt = Instant.now(), age = 18)
        personMapper.insert(p1)
        personMapper.insert(p2)
        personMapper.insert(p3)

        val searchReq = PersonSearchReq(name = "z")
        val wp = LambdaQueryWrapper<Person>()
        wp.setSearchCondition(searchReq)

        personMapper.selectList(wp).forEach {
            println(it)
        }
    }

})
