package jjoommnn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Index
{
    @RequestMapping("/index.do")
    public String index()
    {
        return "index";
    }
}
