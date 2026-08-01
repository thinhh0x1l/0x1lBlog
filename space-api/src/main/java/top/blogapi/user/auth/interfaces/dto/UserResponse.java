package top.blogapi.user.auth.interfaces.dto;

public record UserResponse(
        Long id,
        String email,
        String displayName,
        String avatarUrl,
        String bio,
        String website,
        String location,
        String role,
        Boolean isCreator,
        String status,
        Integer level,
        Long exp,
        Integer checkinStreak,
        Long coins,
        Long gems,
        Long reputationWriting,
        Long reputationCommunity,
        Long reputationCreativity,
        Long reputationInfluence
) {}
