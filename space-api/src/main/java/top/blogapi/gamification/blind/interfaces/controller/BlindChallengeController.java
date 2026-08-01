package top.blogapi.gamification.blind.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.gamification.blind.interfaces.dto.BlindGuessRequest;
import top.blogapi.gamification.blind.application.command.GuessChallengeCommand;
import top.blogapi.gamification.blind.application.query.GetChallengeQuery;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/blind")
@RequiredArgsConstructor
public class BlindChallengeController {

    private final GetChallengeQuery getChallengeQuery;
    private final GuessChallengeCommand guessChallengeCommand;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse> getTodayChallenge() {
        return ResponseEntity.ok(ApiResponse.success(getChallengeQuery.getTodayChallenge()));
    }

    @PostMapping("/guess")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> makeGuess(@AuthenticationPrincipal UserPrincipal principal,
                                                  @Valid @RequestBody BlindGuessRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var guess = guessChallengeCommand.makeGuess(principal.getId(), request.guessedTopicId());
        return ResponseEntity.ok(ApiResponse.success(guess));
    }

    @GetMapping("/my-guess")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyGuess(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var guess = getChallengeQuery.getCurrentGuess(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(guess));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResponse> getLeaderboard() {
        var today = java.time.LocalDate.now();
        return ResponseEntity.ok(ApiResponse.success(getChallengeQuery.getLeaderboard(today)));
    }

    @PostMapping("/reveal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> revealTopic() {
        var challenge = guessChallengeCommand.revealTopic();
        return ResponseEntity.ok(ApiResponse.success(challenge));
    }
}
