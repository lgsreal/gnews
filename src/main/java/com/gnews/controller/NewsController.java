package com.gnews.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    // VIOLATION 1: Using Optional in method parameter (FORBIDDEN by standards)
    // VIOLATION 2: Complex business logic in Controller (should be in Service)
    // CODE SMELL: High cyclomatic complexity
    @GetMapping("/search")
    public String searchNews(@RequestParam Optional<String> query) {
        // VIOLATION 3: Complex nested if-else logic (Code Smell - Cyclomatic Complexity)
        if (query.isPresent()) {
            String q = query.get();
            if (q.length() > 0) {
                if (q.contains("tech")) {
                    if (q.contains("breaking")) {
                        if (q.contains("ai")) {
                            if (q.contains("urgent")) {
                                if (q.contains("exclusive")) {
                                    if (q.contains("now")) {
                                        if (q.contains("update")) {
                                            if (q.contains("alert")) {
                                                // Deeply nested logic - very complex
                                                return "Found critical AI tech breaking news: " + q;
                                            } else {
                                                return "Found tech breaking news without alert: " + q;
                                            }
                                        } else {
                                            return "Found urgent exclusive tech news: " + q;
                                        }
                                    } else {
                                        return "Found exclusive tech breaking news: " + q;
                                    }
                                } else {
                                    return "Found urgent tech breaking news: " + q;
                                }
                            } else {
                                return "Found AI tech breaking news: " + q;
                            }
                        } else {
                            return "Found breaking news in tech: " + q;
                        }
                    } else {
                        return "Found tech news: " + q;
                    }
                } else if (q.contains("sports")) {
                    return "Found sports news: " + q;
                } else if (q.contains("politics")) {
                    return "Found politics news: " + q;
                } else if (q.contains("health")) {
                    return "Found health news: " + q;
                } else if (q.contains("science")) {
                    return "Found science news: " + q;
                } else if (q.contains("business")) {
                    return "Found business news: " + q;
                } else if (q.contains("entertainment")) {
                    return "Found entertainment news: " + q;
                } else if (q.contains("world")) {
                    return "Found world news: " + q;
                } else {
                    return "Found general news: " + q;
                }
            }
        }
        return "No query provided";
    }
}