package com.rahul.graphqldemo.controller;

import com.rahul.graphqldemo.dto.DisplayPattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rahul.graphqldemo.controller.TestController.TEST;

@RequestMapping(TEST)
@RestController
public class TestController {
    static final String TEST = "test";

    @GetMapping("null-stream")
    public void checkNullStream(){
        List<DisplayPattern> displayPatternList = null;
        List<String> list = Optional.ofNullable(displayPatternList)
                .orElseGet(Collections::emptyList).stream()
                .map(DisplayPattern::getCode).collect(Collectors.toList());
        System.out.println(list);
    }
}
