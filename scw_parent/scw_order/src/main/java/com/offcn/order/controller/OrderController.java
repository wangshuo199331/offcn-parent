package com.offcn.order.controller;

import com.offcn.common.AppResponse;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OrderService orderService;

    @PostMapping("saveOrder")
    public AppResponse submitOrder(@RequestBody OrderInfoSubmitVo submitVo){
        String memberId = stringRedisTemplate.opsForValue().get(submitVo.getAccessToken());
        if (memberId == null) {
            return AppResponse.fail("无此权限，请先登录");
        }
        try {
            TOrder order = orderService.saveOrder(submitVo);
            AppResponse<TOrder> orderAppResponse = AppResponse.ok(order);
            return orderAppResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }
    }
}
