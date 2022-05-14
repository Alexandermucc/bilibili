package com.alex.service;

import com.alex.dao.DemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexandermucc
 * @date 2022/05/13 22:20
 **/
@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    public Integer query(Integer id) {
        return demoDao.query(id);
    }

    public String queryNameById(Integer id) {
        return demoDao.queryNameById(id);
    }
}

