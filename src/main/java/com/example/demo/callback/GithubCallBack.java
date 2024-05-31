package com.example.demo.callback;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/callback/github")
public class GithubCallBack {
	
	@GetMapping
	@ResponseBody
	public String getToken(@RequestParam("code")String code) {
		// github 會回調 code
		return "code: "+code;
	}
}
