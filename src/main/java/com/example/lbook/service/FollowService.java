package com.example.lbook.service;

import com.example.lbook.entity.Follow;
import com.example.lbook.entity.User;
import com.example.lbook.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    public void followUser(Long followerId, Long followedId) {
        // Kiểm tra xem đã follow chưa
        if (!followRepository.findByFollowerIdAndFollowedId(followerId, followedId).isPresent()) {
            Follow follow = new Follow(new User(followerId), new User(followedId));
            followRepository.save(follow);
        }
    }

    public void unfollowUser(Long followerId, Long followedId) {
        // Xóa mối quan hệ follow
        followRepository.deleteByFollowerIdAndFollowedId(followerId, followedId);
    }
}