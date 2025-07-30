package com.anant.vestralok;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Controller
public class CalculatorController {

    @GetMapping("/")
    public String calculatorForm(Model model) {
        model.addAttribute("showBillLink", true); // Initially false
        model.addAttribute("result", "");
        model.addAttribute("operations", new String[]{"halfMinus10Percent", "square", "double"});
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(
            @RequestParam("number") @Valid @NotNull @Min(0) Double number,
            @RequestParam(value = "operation", defaultValue = "halfMinus10Percent") String operation,
            Model model) {

        try {
            double result = performCalculation(number, operation);
            model.addAttribute("result", String.format("Result: %.2f", result));
            model.addAttribute("showBillLink", true); // Set to true after successful calculation
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("showBillLink", false); // Keep false if error occurs
        }

        model.addAttribute("operations", new String[]{"halfMinus10Percent", "square", "double"});
        return "index";
    }

    private double performCalculation(double number, String operation) {
        switch(operation) {
            case "halfMinus10Percent":
                return (number / 2) * 0.9;
            case "square":
                return number * number;
            case "double":
                return number * 2;
            default:
                throw new IllegalArgumentException("Invalid operation selected");
        }
    }
}