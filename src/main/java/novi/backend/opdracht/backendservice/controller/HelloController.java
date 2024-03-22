package novi.backend.opdracht.backendservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// dit word een klasse waar request van de bezoeker binnen komen
// we maken hier een endpoint, die een request ontvangt en verdere actie onderneemt
// definieer 2 dingen
// --- hoe komt de clientrequest binnen
// --- wat voor soort request is het? (get, post, put)
//nu werkt deze als de url is: http://localhost:8080/hello?name=Pietje%20Puk

@RestController
public class HelloController {

    private String myName;

    @GetMapping("/hello")
    public String sayHello(@RequestParam(required = false) String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {

            // wie is de eigenaar van een entiteit?

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            return "hello " + userDetails.getUsername();
        } else if (name != null) {
            return "Hello " + name;
        } else {
            return "Hello Stranger";
        }
    }

    @PostMapping("/save")
    public void saveName(@RequestParam String name){
        myName = name;
    }

    @GetMapping("/retrieve")
    public String retrieveName() {
        return this.myName;
    }
}
