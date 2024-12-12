package com.example.lbook.controller;

import com.example.lbook.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping()
    public void follow(@RequestParam Long followerId, @RequestParam Long followedId) {
        followService.followUser(followerId, followedId);
    }

    @PostMapping("/unfollow")
    public void unfollow(@RequestParam Long followerId, @RequestParam Long followedId) {
        followService.unfollowUser(followerId, followedId);
    }
}