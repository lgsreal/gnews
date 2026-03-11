package com.gnews.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    // VIOLATION 1: Using Optional in method parameter (should be removed)
    // VIOLATION 2: Business logic in Controller (should be in Service layer)
    // VIOLATION 3: High Cyclomatic Complexity code smell
    
    @GetMapping("/search")
    public List<String> searchNews(@RequestParam Optional<String> query,
                                   @RequestParam Optional<String> category,
                                   @RequestParam Optional<Integer> priority) {
        
        List<String> results = new java.util.ArrayList<>();
        
        // VIOLATION 3: Deep nested if-else causing high cognitive complexity
        if (query.isPresent()) {
            if (query.get().length() > 0) {
                if (query.get().contains("breaking")) {
                    if (category.isPresent()) {
                        if (category.get().equals("politics")) {
                            if (priority.isPresent()) {
                                if (priority.get() > 8) {
                                    if (query.get().length() > 50) {
                                        if (category.get().length() > 10) {
                                            if (priority.get() < 10) {
                                                results.add("Breaking news in politics with high priority: " + query.get());
                                            } else {
                                                results.add("Breaking news in politics: " + query.get());
                                            }
                                        } else {
                                            results.add("Short category breaking news: " + query.get());
                                        }
                                    } else {
                                        results.add("Short query breaking news: " + query.get());
                                    }
                                } else {
                                    results.add("Low priority breaking news: " + query.get());
                                }
                            } else {
                                results.add("Breaking news in politics: " + query.get());
                            }
                        } else if (category.get().equals("sports")) {
                            results.add("Breaking news in sports: " + query.get());
                        } else if (category.get().equals("tech")) {
                            results.add("Breaking news in tech: " + query.get());
                        }
                    } else {
                        results.add("Breaking news: " + query.get());
                    }
                } else if (query.get().contains("update")) {
                    results.add("News update: " + query.get());
                } else {
                    results.add("Regular news: " + query.get());
                }
            } else {
                results.add("Empty query provided");
            }
        } else {
            results.add("No query provided");
        }
        
        return results;
    }
}