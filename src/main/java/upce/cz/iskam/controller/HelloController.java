package upce.cz.iskam.controller;


import org.springframework.web.bind.annotation.*;
import upce.cz.iskam.dto.SimpleBody;

@RestController
public class HelloController {
    @GetMapping("")
    public String helloWorld() {
        return "Hello world from Spring Boot application.";
    }

    @GetMapping("/path/{message}")
    public String helloPathParams(@PathVariable String message) {
        return "Hello with path params: " + message;
    }

    @GetMapping("/query")
    public String helloQueryParams(@RequestParam String message) {
        return "Hello with query params: " + message;
    }

    @GetMapping("/body")
    public String helloRequestBody(@RequestBody SimpleBody body) {
        return "Hello with request body " + body.toString();
    }
}
