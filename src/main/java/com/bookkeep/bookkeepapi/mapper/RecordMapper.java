package com.bookkeep.bookkeepapi.mapper;

import com.bookkeep.bookkeepapi.entity.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecordMapper {

    @Insert("INSERT INTO record(user_id, type, category, amount, date, remark) " +
            "VALUES(#{userId}, #{type}, #{category}, #{amount}, #{date}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Record record);

    @Select("SELECT * FROM record WHERE user_id = #{userId} ORDER BY date DESC")
    List<Record> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM record WHERE id = #{id}")
    Record findById(@Param("id") Long id);

    @Update("UPDATE record SET category=#{category}, amount=#{amount}, date=#{date}, remark=#{remark} WHERE id=#{id}")
    int update(Record record);

    @Delete("DELETE FROM record WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    // 按月份查询收支汇总
    @Select("SELECT COALESCE(SUM(CASE WHEN type='income' THEN amount ELSE 0 END), 0) AS totalIncome, " +
            "COALESCE(SUM(CASE WHEN type='expense' THEN amount ELSE 0 END), 0) AS totalExpense " +
            "FROM record WHERE user_id = #{userId} AND DATE_FORMAT(date, '%Y-%m') = #{month}")
    Map<String, Object> sumByMonth(@Param("userId") Long userId, @Param("month") String month);

    // 统计用户记账总数
    @Select("SELECT COUNT(*) FROM record WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Long userId);

    // 按分类统计
    @Select("SELECT category, type, SUM(amount) AS totalAmount, COUNT(*) AS count " +
            "FROM record WHERE user_id = #{userId} AND type = #{type} GROUP BY category ORDER BY totalAmount DESC")
    List<Map<String, Object>> sumByCategory(@Param("userId") Long userId, @Param("type") String type);
}
