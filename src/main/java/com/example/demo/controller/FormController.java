package com.example.demo.controller;

import com.example.demo.model.FormData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
public class FormController {

    // Static list to store form submissions (in-memory storage)
    private static final List<FormData> formDataList = new ArrayList<>();
    @GetMapping("/")
    public String showIndex() {
        System.out.println("in index");
        return "index";
    }

    @GetMapping("/form")
    public String showForm() {
        return "form";
    }

    @PostMapping("/submitForm")
    public String submitForm(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("age") int age,
            Model model) {
        System.out.println("in submitForm");

        try {
            System.out.println("in try submitForm");
            // Create a new FormData object and add it to the list
            FormData formData = new FormData(name, email, age);
            formDataList.add(formData);

            // Add the list to the model
            model.addAttribute("formDataList", formDataList);

            return "result";
        } catch (NumberFormatException e) {
            System.out.println("in catch1 submitForm");
            // Handle case where 'age' is not a valid integer
            model.addAttribute("error", "Invalid age format. Please enter a valid number.");
            return "form";
        } catch (Exception e) {
            System.out.println("in catch2 submitForm");
            // Handle other unexpected errors
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return "form";
        }
    }

    @GetMapping("/result")
    public String showResult(Model model) {
        model.addAttribute("formDataList", formDataList);
        return "result";
    }

    @GetMapping("/edit/{name}")
    public String showUpdateForm(@PathVariable("name") String name, Model model) {
        Optional<FormData> optionalFormData = formDataList.stream()
                .filter(r -> r.getName().equals(name))
                .findFirst();

        if (optionalFormData.isPresent()) {
            FormData formData = optionalFormData.get();
            model.addAttribute("formData", formData);
            return "UpdateForm";
        } else {
            model.addAttribute("error", "Record not found");
            return "result"; // or some error page
        }
    }

    @PostMapping("/update/{name}")
    public String updateRecord(@PathVariable("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("age") int age) {

        Iterator<FormData> iterator = formDataList.iterator();
        while (iterator.hasNext()) {
            FormData record = iterator.next();
            if (record.getName().equals(name)) {
                record.setEmail(email);
                record.setAge(age);
                return "redirect:/result";
            }
        }

        // Handle case where the record is not found
        return "redirect:/result"; // or some error page
    }

    @GetMapping("/delete/{name}")
    public String deleteRecord(@PathVariable("name") String name) {

        Iterator<FormData> iterator = formDataList.iterator();
        while (iterator.hasNext()) {
            FormData record = iterator.next();
            if (record.getName().equals(name)) {
                iterator.remove();
                return "redirect:/result";
            }
        }

        // Handle case where the record is not found
        return "redirect:/result"; // or some error page
    }

    @PostMapping("/searchResult")
    public String searchResult(@RequestParam("searchItem") String name , Model model) {
        System.out.println("in try searchResult");
        Optional<FormData> optionalFormData = formDataList.stream()
                .filter(r -> r.getName().equals(name))
                .findFirst();

       if (optionalFormData.isPresent()) {
            FormData formData = optionalFormData.get();
            model.addAttribute("formData", formData);
            return "searchResult";
        } else {
            model.addAttribute("error", "Record not found");
            return "error"; // or some error page
        }
    }
}
