package xyz.qumn.mybatis.plus.search.h2;

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.sql.DriverManager
import javax.sql.DataSource

class H2ConnectionTest : StringSpec({
    "should connect to h2 database"{
        val jdbcURL = "jdbc:h2:mem:test"
        val conn = DriverManager.getConnection(jdbcURL)
        val statement = conn.createStatement()
        val rst = statement.executeQuery("select 1 as count")
        rst.next() shouldBe true
        rst.getInt("count") shouldBe 1
    }
})
