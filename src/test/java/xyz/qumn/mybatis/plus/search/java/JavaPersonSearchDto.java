package xyz.qumn.mybatis.plus.search.java;

import xyz.qumn.mybatis.plus.search.annotation.BETWEEN;
import xyz.qumn.mybatis.plus.search.annotation.GT;
import xyz.qumn.mybatis.plus.search.annotation.LIKE;
import xyz.qumn.mybatis.plus.search.annotation.Operation;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

public class JavaPersonSearchDto {
    // default eq
    private Long id;
    @Operation(GT.class)
    private Integer age;
    @Operation(LIKE.class)
    private String name;
    @Operation(BETWEEN.class)
    private Instant[] instant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant[] getInstant() {
        return instant;
    }

    public void setInstant(Instant[] instant) {
        this.instant = instant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaPersonSearchDto that = (JavaPersonSearchDto) o;
        return Objects.equals(id, that.id) && Objects.equals(age, that.age) && Objects.equals(name, that.name) && Arrays.equals(instant, that.instant);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, age, name);
        result = 31 * result + Arrays.hashCode(instant);
        return result;
    }
}
