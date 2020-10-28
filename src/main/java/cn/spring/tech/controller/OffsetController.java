package cn.spring.tech.controller;

import cn.spring.tech.service.OffsetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/o")
public class OffsetController {
    @Autowired
    private OffsetService offsetService;

    @GetMapping("/q")
    public String query(){
        int count = offsetService.count();
        System.out.println(count);
        return count+"";
    }
}
