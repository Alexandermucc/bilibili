package com.alex.api;

import com.alex.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexandermucc
 * @date 2022/05/13 22:19
 **/
@RestController
public class DemoApi {
    @Autowired
    private DemoService demoService;

    @GetMapping("/query")
    public Integer query(Integer id) {
        return demoService.query(id);
    }

    @GetMapping("/queryByName")
    public String queryByName(Integer id) {
        return demoService.queryNameById(id);
    }


}

