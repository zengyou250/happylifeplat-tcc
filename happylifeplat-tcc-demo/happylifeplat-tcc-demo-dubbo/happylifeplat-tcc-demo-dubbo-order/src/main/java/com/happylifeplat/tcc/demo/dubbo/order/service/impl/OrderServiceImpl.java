/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.happylifeplat.tcc.demo.dubbo.order.service.impl;

import com.happylifeplat.tcc.common.utils.IdWorkerUtils;
import com.happylifeplat.tcc.demo.dubbo.order.entity.Order;
import com.happylifeplat.tcc.demo.dubbo.order.enums.OrderStatusEnum;
import com.happylifeplat.tcc.demo.dubbo.order.mapper.OrderMapper;
import com.happylifeplat.tcc.demo.dubbo.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;


@Service("orderService")
public class OrderServiceImpl implements OrderService {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);


    private final OrderMapper orderMapper;


    private final PaymentServiceImpl paymentServiceImpl;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, PaymentServiceImpl paymentServiceImpl) {
        this.orderMapper = orderMapper;
        this.paymentServiceImpl = paymentServiceImpl;
    }


    @Override
    public String orderPay(Integer count, BigDecimal amount) {
        final Order order = buildOrder(count, amount);
        final int rows = orderMapper.save(order);

        if (rows > 0) {
            paymentServiceImpl.makePayment(order);
        }


        return "success";
    }

    /**
     * 模拟在订单支付操作中，库存在try阶段中的库存异常
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    public String mockInventoryWithTryException(Integer count, BigDecimal amount) {
        final Order order = buildOrder(count, amount);
        final int rows = orderMapper.save(order);

        if (rows > 0) {
            paymentServiceImpl.mockPaymentInventoryWithTryException(order);
        }


        return "success";
    }

    /**
     * 模拟在订单支付操作中，库存在try阶段中的timeout
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    public String mockInventoryWithTryTimeout(Integer count, BigDecimal amount) {
        final Order order = buildOrder(count, amount);
        final int rows = orderMapper.save(order);

        if (rows > 0) {
            paymentServiceImpl.mockPaymentInventoryWithTryTimeout(order);
        }


        return "success";
    }

    /**
     * 模拟在订单支付操作中，库存在Confirm阶段中的异常
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    public String mockInventoryWithConfirmException(Integer count, BigDecimal amount) {
        final Order order = buildOrder(count, amount);
        final int rows = orderMapper.save(order);

        if (rows > 0) {
            paymentServiceImpl.mockPaymentInventoryWithConfirmException(order);
        }


        return "success";
    }

    /**
     * 模拟在订单支付操作中，库存在Confirm阶段中的timeout
     *
     * @param count  购买数量
     * @param amount 支付金额
     * @return string
     */
    @Override
    public String mockInventoryWithConfirmTimeout(Integer count, BigDecimal amount) {
        final Order order = buildOrder(count, amount);
        final int rows = orderMapper.save(order);

        if (rows > 0) {
            paymentServiceImpl.mockPaymentInventoryWithConfirmTimeout(order);
        }


        return "success";
    }


    @Override
    public void updateOrderStatus(Order order) {
        orderMapper.update(order);
    }

    private Order buildOrder(Integer count, BigDecimal amount) {
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(IdWorkerUtils.getInstance().buildPartNumber());
        order.setProductId(1);//demo中的表里只有商品id为1的数据
        order.setStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setTotalAmount(amount);
        order.setCount(count);
        order.setUserId(10000);//demo中 表里面存的用户id为10000
        return order;
    }
}
