package com.offcn.order.service;

import com.offcn.common.AppResponse;
import com.offcn.common.enums.OrderStatusEnume;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import com.offcn.util.AppDateUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private FeignService feignService;

    @Override
    public TOrder saveOrder(OrderInfoSubmitVo submitVo) {

        TOrder order = new TOrder();
        BeanUtils.copyProperties(submitVo,order);
        order.setCreatedate(AppDateUtils.getFormatTime());
        order.setMemberid(Integer.parseInt(redisTemplate.opsForValue().get(submitVo.getAccessToken())));
        order.setStatus(OrderStatusEnume.UNPAY.getCode()+"");
        order.setInvoice(submitVo.getInvoice().toString());
        order.setOrdernum(UUID.randomUUID().toString().replace("-",""));
        AppResponse appResponse = feignService.getReturnById(submitVo.getProjectid());
        if (appResponse.getCode()==0){
            List<TReturn> returnList = (List<TReturn>) appResponse.getData();
            Integer totalMoney = returnList.get(0).getSignalpurchase()*order.getRtncount()+returnList.get(0).getFreight();
            order.setMoney(totalMoney);
        }
        orderMapper.insert(order);
        return order;
    }
}
