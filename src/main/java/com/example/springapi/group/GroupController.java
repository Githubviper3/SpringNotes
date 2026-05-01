package com.example.springapi.group;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
class GroupController {
    private List<Group> groups = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public GroupController() {
        groups.add(new Group(
                idCounter.getAndIncrement(),
                "Cleveland Java User Group",
                "A community of Java developers in the Cleveland area sharing knowledge and best practices",
                "Cleveland",
                "Dan Vega",
                LocalDate.of(2010, 3, 15)
        ));

        groups.add(new Group(
                idCounter.getAndIncrement(),
                "Cleveland React Meetup",
                "Monthly meetup for React and JavaScript developers in Northeast Ohio",
                "Cleveland",
                "Sarah Johnson",
                LocalDate.of(2017, 6, 1)
        ));

        groups.add(new Group(
                idCounter.getAndIncrement(),
                "Cleveland Python User Group",
                "Python enthusiasts meeting to discuss Python programming, data science, and automation",
                "Cleveland",
                "Mike Chen",
                LocalDate.of(2012, 9, 20)
        ));

        groups.add(new Group(
                idCounter.getAndIncrement(),
                "Cleveland Tech Slack",
                "General technology community connecting developers, designers, and tech professionals in Cleveland",
                "Cleveland",
                "Community Led",
                LocalDate.of(2015, 1, 10)
        ));
    }

    @GetMapping("/")
     List<Group> getAllGroups() {
        return groups;
    }

    @GetMapping("/{id}")
    Optional<Group> getGroupById(@PathVariable long id) {
        return groups.stream()
                .filter( g -> g.id().equals(id))
                .findFirst();

    }


}
