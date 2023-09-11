package xyz.qumn.mybatis.plus.search.mybatis;

import com.baomidou.mybatisplus.core.MybatisConfiguration
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.h2.jdbcx.JdbcDataSource
import xyz.qumn.mybatis.plus.search.Entity.Person
import xyz.qumn.mybatis.plus.search.annotation.PersonSearchReq
import xyz.qumn.mybatis.plus.search.annotation.handle
import xyz.qumn.mybatis.plus.search.mapper.PersonMapper
import java.sql.Connection
import java.sql.Statement
import java.time.Duration
import java.time.Instant
import javax.sql.DataSource

fun getDataSource(): DataSource {
    val dataSource = JdbcDataSource()
    dataSource.setURL("jdbc:h2:mem:test")
    dataSource.user = "sa"

    dataSource.password = ""
    val connection: Connection = dataSource.getConnection()
    val statement: Statement = connection.createStatement()
    statement.execute(
        """create table person (
                id BIGINT NOT NULL,
                name VARCHAR(30) NULL,
                age INT NULL,
                PRIMARY KEY (id)
                )"""
    )
    return dataSource
}

fun getSessionFactory(): SqlSessionFactory {
    val dataSource: DataSource = getDataSource()
    val transactionFactory: TransactionFactory = JdbcTransactionFactory()
    val environment = Environment("Production", transactionFactory, dataSource)
    val configuration = MybatisConfiguration(environment)
    configuration.addMapper(PersonMapper::class.java)
    return MybatisSqlSessionFactoryBuilder().build(configuration)
}

class MybatisTest : StringSpec({
    "mybatis plus should work" {
        val sessionFactory = getSessionFactory()
        val session = sessionFactory.openSession(true)
        val personMapper = session.getMapper(PersonMapper::class.java)
        val person = Person(name = "zs", age = 1)
        personMapper.insert(person)
        val personSelected = personMapper.selectById(person.id)

        personSelected shouldNotBe null
        personSelected.name shouldBe person.name
        personSelected.age shouldBe person.age
    }
    "annotation handel should work" {
        val sessionFactory = getSessionFactory()
        val session = sessionFactory.openSession(true)
        val personMapper = session.getMapper(PersonMapper::class.java)
        val person = Person(name = "zs", age = 1)
        personMapper.insert(person)
        val wp = KtQueryWrapper(Person::class.java).eq(Person::name, "zs")
        println(wp.targetSql)
    }
    "handle should work" {
        getSessionFactory()
        val searchReq = PersonSearchReq(age = 3)
        val wp = LambdaQueryWrapper<Person>()
        handle(wp, Person::class.java, searchReq)
        wp.targetSql shouldBe "(age = ?)"
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
        handle(wp, Person::class.java, searchReq)
        wp.targetSql shouldContain "uname = ?"
        wp.targetSql shouldContain "create_at between ? and ?"
        wp.targetSql shouldContain "age = ?"
    }

})
