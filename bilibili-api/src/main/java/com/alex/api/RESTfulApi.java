package com.alex.api;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandermucc
 * @date 2022/05/14 12:19
 **/

// RESTful风格的demo
@RestController
public class RESTfulApi {

    private final Map<Integer,Map<String, Object>> dataMap;

    public RESTfulApi() {
        dataMap = new HashMap<>();
        for (int i = 1; i < 3 ; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",i);
            map.put("name","name : " + i);
            dataMap.put(i,map);
        }
    }

    @GetMapping("/restful/{id}")
    public Map<String, Object> getData(@PathVariable("id") Integer id) {
        return dataMap.get(id);
    }

    @DeleteMapping("/restful/{id}")
    public String deleteData(@PathVariable("id") Integer id) {
        dataMap.remove(id);
        return "delete success";
    }

    @PostMapping("/restful")
    public String postData(@RequestBody Map<String, Object> map) {
        Integer[] idArray = dataMap.keySet().toArray(new Integer[0]);
        Arrays.sort(idArray);
        int nextId = idArray[idArray.length - 1] + 1;
        dataMap.put(nextId,map);
        return "post success";
    }


    @PutMapping("/restful")
    public String putData(@RequestBody Map<String,Object> map) {
        // 获取当前的id
        Integer id = Integer.valueOf(String.valueOf(map.get("id")));
        Map<String, Object> containedData = dataMap.get(id);

        if (containedData == null) {
            Integer[] idArray = dataMap.keySet().toArray(new Integer[0]);
            Arrays.sort(idArray);
            int nextId = idArray[idArray.length - 1] + 1;
            dataMap.put(nextId,map);
        } else {
            dataMap.put(id, map);
        }

        return "put success";
    }

}

