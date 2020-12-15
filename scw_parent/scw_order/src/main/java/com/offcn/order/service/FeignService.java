package com.offcn.order.service;

import com.offcn.common.AppResponse;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCW-PROJECT",fallback = FeignFallBackClass.class)
public interface FeignService {

    @GetMapping("project/getReturnById/{projectId}")
    public AppResponse<List<TReturn>> getReturnById(@PathVariable Integer projectId);

    }
