# space-api — Cây thư mục

## `src/main/java/top/blogapi/`

```
top/blogapi/
├── BlogApiApplication.java
│
├── admin/
│   ├── application/
│   │   ├── command/
│   │   │   ├── AdminBanUserCommand.java
│   │   │   ├── AdminDeleteBlogCommand.java
│   │   │   ├── AdminModerateCommentCommand.java
│   │   │   ├── AdminToggleBlogRecommendCommand.java
│   │   │   ├── AdminToggleBlogTopCommand.java
│   │   │   ├── AdminUpdateUserRoleCommand.java
│   │   │   ├── BanUserCommand.java
│   │   │   └── UpdateSettingsCommand.java
│   │   └── query/
│   │       ├── GetAdminBlogsQuery.java
│   │       ├── GetAdminDashboardQuery.java
│   │       ├── GetAdminUsersQuery.java
│   │       ├── GetSettingsQuery.java
│   │       └── GetUserQuery.java
│   ├── domain/
│   │   ├── entity/
│   │   │   ├── enums/
│   │   │   │   └── SettingType.java
│   │   │   └── SiteSetting.java
│   │   ├── repository/
│   │   │   ├── AdminBlogRepository.java
│   │   │   ├── AdminCommentRepository.java
│   │   │   ├── AdminStatsRepository.java
│   │   │   ├── AdminUserRepository.java
│   │   │   └── SiteSettingRepository.java
│   │   └── service/
│   │       └── SiteSettingService.java
│   ├── infrastructure/
│   │   └── repository/
│   │       ├── AdminBlogRepositoryImpl.java
│   │       ├── AdminCommentRepositoryImpl.java
│   │       ├── AdminStatsRepositoryImpl.java
│   │       ├── AdminUserRepositoryImpl.java
│   │       ├── SiteSettingJpaRepository.java
│   │       └── SiteSettingRepositoryImpl.java
│   └── interfaces/
│       └── controller/
│           └── SiteSettingAdminController.java
│
├── content/
│   ├── blog/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   ├── CreateBlogCommand.java
│   │   │   │   ├── DeleteAdminBlogCommand.java
│   │   │   │   ├── DeleteBlogCommand.java
│   │   │   │   ├── PublishBlogCommand.java
│   │   │   │   ├── ToggleRecommendCommand.java
│   │   │   │   ├── ToggleTopCommand.java
│   │   │   │   └── UpdateBlogCommand.java
│   │   │   └── query/
│   │   │       ├── GetAdminBlogQuery.java
│   │   │       └── GetBlogQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Blog.java
│   │   │   │   └── BlogHashtag.java
│   │   │   ├── event/
│   │   │   │   └── BlogPublishedEvent.java
│   │   │   ├── repository/
│   │   │   │   ├── BlogHashtagRepository.java
│   │   │   │   └── BlogRepository.java
│   │   │   └── service/
│   │   │       ├── BlogService.java
│   │   │       └── ViewTrackingService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── BlogHashtagJpaRepository.java
│   │   │       ├── BlogHashtagRepositoryImpl.java
│   │   │       ├── BlogJpaRepository.java
│   │   │       ├── BlogMybatisMapper.java
│   │   │       └── BlogRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   ├── BlogAdminController.java
│   │       │   └── BlogController.java
│   │       └── dto/
│   │           ├── BlogMapper.java
│   │           ├── BlogResponse.java
│   │           ├── CreateBlogRequest.java
│   │           └── UpdateBlogRequest.java
│   │
│   ├── category/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   ├── CreateCategoryCommand.java
│   │   │   │   ├── DeleteCategoryCommand.java
│   │   │   │   └── UpdateCategoryCommand.java
│   │   │   └── query/
│   │   │       └── GetCategoryQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── Category.java
│   │   │   ├── repository/
│   │   │   │   └── CategoryRepository.java
│   │   │   └── service/
│   │   │       └── CategoryService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── CategoryJpaRepository.java
│   │   │       ├── CategoryMybatisMapper.java
│   │   │       └── CategoryRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   ├── CategoryAdminController.java
│   │       │   └── CategoryController.java
│   │       └── dto/
│   │           ├── CategoryMapper.java
│   │           └── CategoryResponse.java
│   │
│   ├── hashtag/
│   │   ├── application/
│   │   │   └── query/
│   │   │       └── GetTopHashtagQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── Hashtag.java
│   │   │   ├── repository/
│   │   │   │   └── HashtagRepository.java
│   │   │   └── service/
│   │   │       └── HashtagService.java
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── HashtagJpaRepository.java
│   │   │       └── HashtagRepositoryImpl.java
│   │   └── interfaces/
│   │       └── controller/
│   │           └── HashtagController.java
│   │
│   └── series/
│       ├── application/
│       │   ├── command/
│       │   │   ├── AddBlogToSeriesCommand.java
│       │   │   ├── CreateSeriesCommand.java
│       │   │   ├── DeleteSeriesCommand.java
│       │   │   ├── RemoveBlogFromSeriesCommand.java
│       │   │   └── UpdateSeriesCommand.java
│       │   └── query/
│       │       └── GetSeriesQuery.java
│       ├── domain/
│       │   ├── entity/
│       │   │   ├── BlogSeries.java
│       │   │   ├── SeriesBlog.java
│       │   │   └── SeriesSubscriber.java
│       │   ├── repository/
│       │   │   ├── BlogSeriesRepository.java
│       │   │   ├── SeriesBlogRepository.java
│       │   │   └── SeriesSubscriberRepository.java
│       │   └── service/
│       │       └── BlogSeriesService.java
│       ├── dto/
│       ├── infrastructure/
│       │   └── repository/
│       │       ├── BlogSeriesJpaRepository.java
│       │       ├── BlogSeriesMybatisMapper.java
│       │       ├── BlogSeriesRepositoryImpl.java
│       │       ├── SeriesBlogJpaRepository.java
│       │       ├── SeriesBlogRepositoryImpl.java
│       │       ├── SeriesSubscriberJpaRepository.java
│       │       └── SeriesSubscriberRepositoryImpl.java
│       └── interfaces/
│           └── controller/
│               └── SeriesController.java
│
├── dashboard/
│   ├── application/
│   │   └── query/
│   │       └── GetDashboardQuery.java
│   ├── domain/
│   │   └── service/
│   │       └── DashboardService.java
│   └── interfaces/
│       └── controller/
│
├── discovery/
│   ├── application/
│   │   └── query/
│   │       └── GetDiscoveryQuery.java
│   ├── domain/
│   │   └── service/
│   │       └── DiscoveryService.java
│   └── interfaces/
│       └── controller/
│           └── DiscoveryController.java
│
├── engagement/
│   ├── bookmark/
│   │   ├── application/
│   │   │   └── command/
│   │   │       └── ToggleBookmarkCommand.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── Bookmark.java
│   │   │   ├── repository/
│   │   │   │   └── BookmarkRepository.java
│   │   │   └── service/
│   │   │       └── BookmarkService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── BookmarkJpaRepository.java
│   │   │       └── BookmarkRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── BookmarkController.java
│   │       └── dto/
│   │           └── BookmarkRequest.java
│   │
│   ├── comment/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   ├── AdminCommentCommand.java
│   │   │   │   ├── CreateCommentCommand.java
│   │   │   │   ├── DeleteCommentCommand.java
│   │   │   │   └── UpdateCommentCommand.java
│   │   │   └── query/
│   │   │       └── GetCommentQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Comment.java
│   │   │   │   ├── CommentReaction.java
│   │   │   │   └── Mention.java
│   │   │   ├── event/
│   │   │   │   └── CommentCreatedEvent.java
│   │   │   ├── repository/
│   │   │   │   ├── CommentReactionRepository.java
│   │   │   │   ├── CommentRepository.java
│   │   │   │   └── MentionRepository.java
│   │   │   └── service/
│   │   │       └── CommentService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── CommentJpaRepository.java
│   │   │       ├── CommentMybatisMapper.java
│   │   │       ├── CommentReactionJpaRepository.java
│   │   │       ├── CommentReactionRepositoryImpl.java
│   │   │       ├── CommentRepositoryImpl.java
│   │   │       ├── MentionJpaRepository.java
│   │   │       └── MentionRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── CommentController.java
│   │       └── dto/
│   │           ├── CommentMapper.java
│   │           ├── CommentRequest.java
│   │           └── CommentResponse.java
│   │
│   ├── follow/
│   │   ├── application/
│   │   │   └── command/
│   │   │       └── ToggleFollowCommand.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── Follow.java
│   │   │   ├── event/
│   │   │   │   └── FollowEvent.java
│   │   │   ├── repository/
│   │   │   │   └── FollowRepository.java
│   │   │   └── service/
│   │   │       └── FollowService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── FollowJpaRepository.java
│   │   │       └── FollowRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── FollowController.java
│   │       └── dto/
│   │           └── FollowRequest.java
│   │
│   ├── notification/
│   │   ├── application/
│   │   │   └── query/
│   │   │       └── GetNotificationQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── Notification.java
│   │   │   ├── repository/
│   │   │   │   └── NotificationRepository.java
│   │   │   └── service/
│   │   │       └── NotificationService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── NotificationJpaRepository.java
│   │   │       └── NotificationRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── NotificationController.java
│   │       └── dto/
│   │           ├── NotificationMapper.java
│   │           └── NotificationResponse.java
│   │
│   ├── reaction/
│   │   ├── application/
│   │   │   └── command/
│   │   │       └── ReactCommand.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── BlogReaction.java
│   │   │   │   ├── enums/
│   │   │   │   │   └── ReactionType.java
│   │   │   │   └── Reaction.java
│   │   │   ├── repository/
│   │   │   │   ├── BlogReactionRepository.java
│   │   │   │   └── ReactionRepository.java
│   │   │   └── service/
│   │   │       └── ReactionService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── BlogReactionJpaRepository.java
│   │   │       ├── BlogReactionRepositoryImpl.java
│   │   │       ├── ReactionJpaRepository.java
│   │   │       └── ReactionRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── ReactionController.java
│   │       └── dto/
│   │           └── ReactionRequest.java
│   │
│   └── share/
│       ├── domain/
│       │   ├── entity/
│       │   │   └── Share.java
│       │   └── repository/
│       │       └── ShareRepository.java
│       └── infrastructure/
│           └── repository/
│               ├── ShareJpaRepository.java
│               └── ShareRepositoryImpl.java
│
├── gamification/
│   ├── badge/
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Badge.java
│   │   │   │   └── UserBadge.java
│   │   │   ├── repository/
│   │   │   │   ├── BadgeRepository.java
│   │   │   │   └── UserBadgeRepository.java
│   │   │   └── service/
│   │   │       └── BadgeService.java
│   │   ├── dto/
│   │   └── infrastructure/
│   │       └── repository/
│   │           ├── BadgeJpaRepository.java
│   │           ├── BadgeRepositoryImpl.java
│   │           ├── UserBadgeJpaRepository.java
│   │           └── UserBadgeRepositoryImpl.java
│   │
│   ├── blind/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   └── GuessChallengeCommand.java
│   │   │   └── query/
│   │   │       └── GetChallengeQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── BlindChallenge.java
│   │   │   │   └── BlindChallengeGuess.java
│   │   │   ├── repository/
│   │   │   │   ├── BlindChallengeGuessRepository.java
│   │   │   │   └── BlindChallengeRepository.java
│   │   │   └── service/
│   │   │       └── BlindChallengeService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── BlindChallengeGuessJpaRepository.java
│   │   │       ├── BlindChallengeGuessMybatisMapper.java
│   │   │       ├── BlindChallengeGuessRepositoryImpl.java
│   │   │       ├── BlindChallengeJpaRepository.java
│   │   │       └── BlindChallengeRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── BlindChallengeController.java
│   │       └── dto/
│   │           ├── BlindChallengeDTO.java
│   │           ├── BlindGuessDTO.java
│   │           ├── BlindGuessRequest.java
│   │           └── BlindLeaderboardDTO.java
│   │
│   ├── quest/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   ├── AssignQuestCommand.java
│   │   │   │   ├── ClaimQuestRewardCommand.java
│   │   │   │   └── CompleteQuestCommand.java
│   │   │   └── query/
│   │   │       └── GetQuestQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Quest.java
│   │   │   │   └── UserQuest.java
│   │   │   ├── repository/
│   │   │   │   ├── QuestRepository.java
│   │   │   │   └── UserQuestRepository.java
│   │   │   └── service/
│   │   │       └── QuestService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── QuestJpaRepository.java
│   │   │       ├── QuestRepositoryImpl.java
│   │   │       ├── UserQuestJpaRepository.java
│   │   │       ├── UserQuestMybatisMapper.java
│   │   │       └── UserQuestRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── QuestController.java
│   │       └── dto/
│   │           ├── ClaimRewardRequest.java
│   │           ├── QuestResponse.java
│   │           └── UserQuestResponse.java
│   │
│   ├── reputation/
│   │   ├── application/
│   │   │   └── query/
│   │   │       └── GetReputationQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── UserExpLog.java
│   │   │   ├── repository/
│   │   │   │   └── UserExpLogRepository.java
│   │   │   └── service/
│   │   │       └── ReputationService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── UserExpLogJpaRepository.java
│   │   │       └── UserExpLogRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── ReputationController.java
│   │       └── dto/
│   │
│   ├── shop/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   └── PurchaseItemCommand.java
│   │   │   └── query/
│   │   │       └── GetShopQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── ItemCatalog.java
│   │   │   │   └── UserInventory.java
│   │   │   ├── repository/
│   │   │   │   ├── ItemCatalogRepository.java
│   │   │   │   └── UserInventoryRepository.java
│   │   │   └── service/
│   │   │       └── ShopService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── ItemCatalogJpaRepository.java
│   │   │       ├── ItemCatalogRepositoryImpl.java
│   │   │       ├── UserInventoryJpaRepository.java
│   │   │       └── UserInventoryRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── ShopController.java
│   │       └── dto/
│   │           ├── InventoryDTO.java
│   │           ├── ItemDTO.java
│   │           └── PurchaseRequest.java
│   │
│   ├── skill/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   └── UnlockSkillCommand.java
│   │   │   └── query/
│   │   │       └── GetSkillTreeQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── SkillTree.java
│   │   │   │   ├── UserSkillProgress.java
│   │   │   │   └── UserSkillUnlock.java
│   │   │   ├── repository/
│   │   │   │   ├── SkillTreeRepository.java
│   │   │   │   ├── UserSkillProgressRepository.java
│   │   │   │   └── UserSkillUnlockRepository.java
│   │   │   └── service/
│   │   │       └── SkillTreeService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── SkillTreeJpaRepository.java
│   │   │       ├── SkillTreeRepositoryImpl.java
│   │   │       ├── UserSkillProgressJpaRepository.java
│   │   │       ├── UserSkillProgressMybatisMapper.java
│   │   │       ├── UserSkillProgressRepositoryImpl.java
│   │   │       ├── UserSkillUnlockJpaRepository.java
│   │   │       └── UserSkillUnlockRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── SkillTreeController.java
│   │       └── dto/
│   │           ├── SkillProgressDTO.java
│   │           ├── SkillTreeDTO.java
│   │           ├── UnlockRequest.java
│   │           └── UserSkillDTO.java
│   │
│   └── streak/
│       ├── application/
│       │   └── command/
│       │       └── CheckinCommand.java
│       ├── domain/
│       │   ├── entity/
│       │   │   └── DailyCheckin.java
│       │   ├── repository/
│       │   │   └── DailyCheckinRepository.java
│       │   └── service/
│       │       └── StreakService.java
│       ├── dto/
│       ├── infrastructure/
│       │   └── repository/
│       │       ├── DailyCheckinJpaRepository.java
│       │       └── DailyCheckinRepositoryImpl.java
│       └── interfaces/
│           ├── controller/
│           │   └── StreakController.java
│           └── dto/
│
├── infra/
│   ├── cache/
│   │   ├── CacheKey.java
│   │   ├── CacheMetrics.java
│   │   ├── CachePolicies.java
│   │   ├── CachePolicy.java
│   │   ├── CacheRegion.java
│   │   ├── CacheService.java
│   │   └── CacheServiceImpl.java
│   ├── config/
│   │   ├── AsyncConfig.java
│   │   ├── CacheConfig.java
│   │   ├── JpaAuditConfig.java
│   │   ├── RedisConfig.java
│   │   ├── SecurityConfig.java
│   │   └── WebConfig.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   ├── security/
│   │   ├── CurrentUser.java
│   │   ├── jwt/
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── JwtService.java
│   │   │   └── JwtServiceImpl.java
│   │   ├── MyAccessDeniedHandler.java
│   │   ├── MyAuthenticationEntryPoint.java
│   │   ├── oauth2/
│   │   │   ├── CustomOAuth2UserService.java
│   │   │   └── OAuth2LoginSuccessHandler.java
│   │   └── UserPrincipal.java
│   └── storage/
│       └── UploadService.java
│
├── listener/
│   ├── BlogEventListener.java
│   ├── CommentEventListener.java
│   ├── FollowEventListener.java
│   └── UserEventListener.java
│
├── shared/
│   ├── BaseEntity.java
│   ├── constant/
│   ├── event/
│   │   └── DomainEvent.java
│   ├── exception/
│   │   ├── AppException.java
│   │   ├── BusinessException.java
│   │   └── ErrorCode.java
│   └── response/
│       ├── ApiResponse.java
│       └── PageResponse.java
│
├── social/
│   ├── canvas/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   ├── AddStrokeCommand.java
│   │   │   │   └── CreateCanvasCommand.java
│   │   │   └── query/
│   │   │       └── GetCanvasQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Canvas.java
│   │   │   │   └── CanvasStroke.java
│   │   │   ├── repository/
│   │   │   │   ├── CanvasRepository.java
│   │   │   │   └── CanvasStrokeRepository.java
│   │   │   └── service/
│   │   │       └── CanvasService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── CanvasJpaRepository.java
│   │   │       ├── CanvasMybatisMapper.java
│   │   │       ├── CanvasRepositoryImpl.java
│   │   │       ├── CanvasStrokeJpaRepository.java
│   │   │       ├── CanvasStrokeMybatisMapper.java
│   │   │       └── CanvasStrokeRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── CanvasController.java
│   │       └── dto/
│   │           ├── CanvasMapper.java
│   │           ├── CanvasRequest.java
│   │           ├── CanvasResponse.java
│   │           └── CanvasStrokeResponse.java
│   │
│   ├── mischief/
│   │   ├── application/
│   │   │   └── query/
│   │   │       └── GetMischiefQuery.java
│   │   ├── domain/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   │       └── MischiefService.java
│   │   └── interfaces/
│   │       └── controller/
│   │           └── MischiefController.java
│   │
│   ├── playlist/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   ├── AddSongCommand.java
│   │   │   │   ├── RemoveSongCommand.java
│   │   │   │   ├── ReorderSongsCommand.java
│   │   │   │   └── VoteSongCommand.java
│   │   │   └── query/
│   │   │       └── GetPlaylistQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Playlist.java
│   │   │   │   ├── PlaylistSong.java
│   │   │   │   └── PlaylistVote.java
│   │   │   ├── repository/
│   │   │   │   ├── PlaylistRepository.java
│   │   │   │   ├── PlaylistSongRepository.java
│   │   │   │   └── PlaylistVoteRepository.java
│   │   │   └── service/
│   │   │       └── PlaylistService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── PlaylistJpaRepository.java
│   │   │       ├── PlaylistRepositoryImpl.java
│   │   │       ├── PlaylistSongJpaRepository.java
│   │   │       ├── PlaylistSongMybatisMapper.java
│   │   │       ├── PlaylistSongRepositoryImpl.java
│   │   │       ├── PlaylistVoteJpaRepository.java
│   │   │       ├── PlaylistVoteMybatisMapper.java
│   │   │       └── PlaylistVoteRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── PlaylistController.java
│   │       └── dto/
│   │           ├── AddSongRequest.java
│   │           ├── PlaylistMapper.java
│   │           ├── PlaylistResponse.java
│   │           ├── PlaylistSongResponse.java
│   │           └── ReorderRequest.java
│   │
│   ├── status/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   │   ├── CreateStatusCommand.java
│   │   │   │   └── VoteStatusCommand.java
│   │   │   └── query/
│   │   │       └── GetStatusQuery.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Status.java
│   │   │   │   ├── StatusPoll.java
│   │   │   │   └── StatusPollVote.java
│   │   │   ├── event/
│   │   │   │   └── StatusCreatedEvent.java
│   │   │   ├── repository/
│   │   │   │   ├── StatusPollRepository.java
│   │   │   │   ├── StatusPollVoteRepository.java
│   │   │   │   └── StatusRepository.java
│   │   │   └── service/
│   │   │       └── StatusService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── StatusJpaRepository.java
│   │   │       ├── StatusMybatisMapper.java
│   │   │       ├── StatusPollJpaRepository.java
│   │   │       ├── StatusPollRepositoryImpl.java
│   │   │       ├── StatusPollVoteJpaRepository.java
│   │   │       ├── StatusPollVoteRepositoryImpl.java
│   │   │       └── StatusRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── StatusController.java
│   │       └── dto/
│   │           ├── StatusMapper.java
│   │           ├── StatusRequest.java
│   │           └── StatusResponse.java
│   │
│   └── story/
│       ├── application/
│       │   ├── command/
│       │   │   └── CreateStoryCommand.java
│       │   └── query/
│       │       └── GetStoryQuery.java
│       ├── domain/
│       │   ├── entity/
│       │   │   ├── enums/
│       │   │   │   ├── StoryMediaType.java
│       │   │   │   └── StoryVisibility.java
│       │   │   ├── Story.java
│       │   │   └── StoryArchive.java
│       │   ├── repository/
│       │   │   ├── StoryArchiveRepository.java
│       │   │   └── StoryRepository.java
│       │   └── service/
│       │       └── StoryService.java
│       ├── dto/
│       ├── infrastructure/
│       │   └── repository/
│       │       ├── StoryArchiveJpaRepository.java
│       │       ├── StoryArchiveRepositoryImpl.java
│       │       ├── StoryJpaRepository.java
│       │       ├── StoryMybatisMapper.java
│       │       └── StoryRepositoryImpl.java
│       └── interfaces/
│           ├── controller/
│           │   └── StoryController.java
│           └── dto/
│               ├── StoryMapper.java
│               ├── StoryRequest.java
│               └── StoryResponse.java
│
├── user/
│   ├── auth/
│   │   ├── application/
│   │   │   ├── AuthResult.java
│   │   │   └── command/
│   │   │       ├── LoginCommand.java
│   │   │       ├── RefreshTokenCommand.java
│   │   │       └── RegisterCommand.java
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── OAuth2Account.java
│   │   │   │   ├── RefreshToken.java
│   │   │   │   └── Session.java
│   │   │   ├── repository/
│   │   │   │   ├── OAuth2AccountRepository.java
│   │   │   │   ├── RefreshTokenRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── SessionRepository.java
│   │   │   └── service/
│   │   │       └── RefreshTokenService.java
│   │   ├── dto/
│   │   ├── infrastructure/
│   │   │   └── repository/
│   │   │       ├── OAuth2AccountJpaRepository.java
│   │   │       ├── OAuth2AccountRepositoryImpl.java
│   │   │       ├── RefreshTokenJpaRepository.java
│   │   │       ├── RefreshTokenRepositoryImpl.java
│   │   │       ├── RoleJpaRepository.java
│   │   │       ├── RoleRepositoryImpl.java
│   │   │       ├── SessionJpaRepository.java
│   │   │       ├── SessionMybatisMapper.java
│   │   │       ├── SessionRepositoryImpl.java
│   │   │       ├── UserJpaRepository.java
│   │   │       ├── UserMybatisMapper.java
│   │   │       └── UserRepositoryImpl.java
│   │   └── interfaces/
│   │       ├── controller/
│   │       │   └── AuthController.java
│   │       └── dto/
│   │           ├── AuthResponse.java
│   │           ├── LoginRequest.java
│   │           ├── RefreshTokenRequest.java
│   │           ├── RegisterRequest.java
│   │           ├── UserMapper.java
│   │           └── UserResponse.java
│   │
│   ├── core/
│   │   ├── entity/
│   │   │   ├── enums/
│   │   │   │   ├── UserRole.java
│   │   │   │   └── UserStatus.java
│   │   │   ├── Role.java
│   │   │   └── User.java
│   │   ├── event/
│   │   │   └── UserRegisteredEvent.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   └── service/
│   │       └── UserService.java
│   │
│   ├── oauth2/
│   │
│   └── profile/
│       ├── application/
│       │   ├── command/
│       │   │   ├── ReorderWidgetsCommand.java
│       │   │   ├── UpdateProfileCommand.java
│       │   │   └── UpdateWidgetCommand.java
│       │   └── query/
│       │       ├── GetProfileQuery.java
│       │       └── GetWidgetsQuery.java
│       ├── domain/
│       │   ├── entity/
│       │   │   ├── AboutInfo.java
│       │   │   ├── enums/
│       │   │   │   └── AboutType.java
│       │   │   └── ProfileWidget.java
│       │   ├── repository/
│       │   │   ├── AboutInfoRepository.java
│       │   │   └── ProfileWidgetRepository.java
│       │   └── service/
│       │       └── ProfileWidgetService.java
│       ├── dto/
│       ├── infrastructure/
│       │   └── repository/
│       │       ├── AboutInfoJpaRepository.java
│       │       ├── AboutInfoRepositoryImpl.java
│       │       ├── ProfileWidgetJpaRepository.java
│       │       └── ProfileWidgetRepositoryImpl.java
│       └── interfaces/
│           ├── controller/
│           │   ├── ProfileController.java
│           │   └── ProfileWidgetController.java
│           └── dto/
│               ├── ProfileLayoutDTO.java
│               ├── WidgetDTO.java
│               ├── WidgetReorderRequest.java
│               └── WidgetUpdateRequest.java
│
└── util/
    ├── Encryptor.java
    ├── IpAddressUtils.java
    ├── markdown/
    │   ├── ext/
    │   │   ├── cover/
    │   │   │   ├── Cover.java
    │   │   │   ├── CoverExtension.java
    │   │   │   └── internal/
    │   │   │       ├── AbstractCoverNodeRenderer.java
    │   │   │       ├── CoverDelimiterProcessor.java
    │   │   │       ├── CoverHtmlNodeRenderer.java
    │   │   │       └── CoverTextContentNodeRenderer.java
    │   │   └── heimu/
    │   │       ├── Heimu.java
    │   │       ├── HeimuExtension.java
    │   │       └── internal/
    │   │           ├── AbstractHeimuNodeRenderer.java
    │   │           ├── HeimuDelimiterProcessor.java
    │   │           ├── HeimuHtmlNodeRenderer.java
    │   │           └── HeimuTextContentNodeRenderer.java
    │   └── MarkdownUtils.java
    ├── MD5Utils.java
    ├── SlugUtils.java
    ├── StringUtils.java
    └── UserAgentUtils.java
```
