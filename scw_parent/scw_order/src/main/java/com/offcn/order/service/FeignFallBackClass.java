package com.offcn.order.service;

import com.offcn.common.AppResponse;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class FeignFallBackClass implements FeignService{


    @Override
    public AppResponse<List<TReturn>> getReturnById(Integer projectId) {
        return AppResponse.fail(null);
    }
}
