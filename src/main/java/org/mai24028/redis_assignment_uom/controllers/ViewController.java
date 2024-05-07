package org.mai24028.redis_assignment_uom.controllers;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.mai24028.redis_assignment_uom.models.RecordItem;
import org.mai24028.redis_assignment_uom.services.RecordsService;
import org.mai24028.redis_assignment_uom.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class ViewController {

    private final UserService userService;
    private final RecordsService recordsService;

    public ViewController(UserService userService, RecordsService recordsService) {
        this.userService = userService;
        this.recordsService = recordsService;
    }

    @GetMapping("/")
    public String start(Model model) {
        model.addAttribute("now", new Date().toInstant());
        return "index";
    }

    @HxRequest("sign-in-form")
    @PostMapping("/login")
    public String login(Model model, @RequestParam("username") String username) {
        model.addAttribute("now", new Date().toInstant());

        System.out.println("Username: " + username);

        try{
            if (userService.userExists(username)){
                model.addAttribute("welcome_message", "Welcome back " + username);

            } else {
                userService.storeUser(username);
                model.addAttribute("welcome_message", "Welcome " + username + ", you are now registered!");
            }

            model.addAttribute("username", username);
            return "dashboard_fragment";
        }
        catch (Exception e){
            model.addAttribute("error_message", e.getMessage());
            model.addAttribute("username", username);
            return "loginFailed";
        }

    }

    @HxRequest("new-record-form")
    @PostMapping("/records")
    public HtmxResponse addRecord(@RequestParam("record_name") String recordName, @RequestParam("username") String username) {

//        model.addAttribute("records", List.of(new RecordItem(recordName, 0)));
        try{
            recordsService.storeRecord(username, recordName);
        } catch (Exception e) {
            return HtmxResponse.builder().view("alreadyExistsAlert").trigger("record-already-exists").build();
        }

        return HtmxResponse.builder().trigger("user-entries-list-updated").build();
    }

    @HxRequest("user-entries-list")
    @GetMapping("/records/{username}")
    public String getRecords(Model model, @PathVariable("username") String username) {

        System.out.println("Username: " + username);

        List<RecordItem> records = recordsService.findRecordsByUsername(username);
        model.addAttribute("records", records);

        return "userRecordItem";
    }

    @HxRequest("artist-search-form")
    @PostMapping("/search")
    public HtmxResponse searchRecord(Model model, @RequestParam("record_name") String recordName) {

        String username = recordsService.searchRecord(recordName);

        if(username != null){
            model.addAttribute("username", username);
        }

        return HtmxResponse.builder().trigger("search-completed").view("searchResult").build();
    }

    @HxRequest("entries-per-user")
    @GetMapping("/entries-per-user")
    public HtmxResponse getEntriesPerUser(Model model) {
        model.addAttribute("entriesPerUser", recordsService.getEntriesPerUser());
        return HtmxResponse.builder().view("entriesPerUser").build();
    }

    @HxRequest("average-queries-section")
    @GetMapping("/average-queries")
    public HtmxResponse getAverageQueries(Model model) {
        model.addAttribute("queries", recordsService.getAverageQueries());
        return HtmxResponse.builder().view("averageQueries").build();
    }
}
