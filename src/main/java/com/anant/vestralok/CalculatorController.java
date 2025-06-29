package com.anant.vestralok;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalculatorController {
    @GetMapping("/")
    public String calculatorForm(Model model) {
        model.addAttribute("result", "");
        return "calculator";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam("number") double number, Model model) {
        double result = (number / 2) * 0.9; // (number/2 - 10%)
        model.addAttribute("result", "Result: " + result);
        return "calculator";
    }
}


