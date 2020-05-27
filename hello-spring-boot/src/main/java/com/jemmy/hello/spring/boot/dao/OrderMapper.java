package com.jemmy.hello.spring.boot.dao;

import com.jemmy.hello.spring.boot.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jemmy_order
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jemmy_order
     *
     * @mbg.generated
     */
    int insert(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jemmy_order
     *
     * @mbg.generated
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jemmy_order
     *
     * @mbg.generated
     */
    List<Order> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jemmy_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Order record);

    /**
     * 批量插入
     */
    int insertBatch(@Param("orderList") List<Order> record);

    /**
     * 根据用户id查询订单
     */
    List<Order> selectByUserId(@Param("userId") Integer userId);
}