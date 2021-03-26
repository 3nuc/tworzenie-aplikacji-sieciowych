package pl.systemyRozproszone.systemyRozproszone.restControllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArithmeticOperationController {



    @GetMapping("/discretization/get")
    public String returnString(){
        return "get test";
    }

    @PostMapping("/post")
    public String postString(){
        return "post test";
    }
}
