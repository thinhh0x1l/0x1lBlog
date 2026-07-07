# Facebook-Style Story Feature — Design Spec

## Overview

Replace the existing `StoryViewer.vue` with a full Facebook/Instagram-style story system: horizontal story list with per-user circles, fullscreen viewer, per-user progress bars, video support, gestures, keyboard shortcuts, and composable-based architecture.

## Architecture

```
src/
  components/
    story/                        # New folder
      StoryList.vue               # Horizontal scroll of user circles
      StoryCard.vue               # Single user circle (avatar + ring)
      StoryViewer.vue             # Fullscreen overlay (orchestrator)
      StoryImage.vue              # Image display with Ken Burns
      StoryVideo.vue              # Video player with mute/unmute
      StoryHeader.vue             # Avatar + username + time + close
      StoryFooter.vue             # Reply + reaction + share + quick emoji
      StoryProgress.vue           # Per-user segmented progress bar
      StoryReaction.vue           # Reaction picker popover
      StoryReply.vue              # Reply input
      StoryMenu.vue               # Three-dot menu (report, etc.)
      StoryNavigation.vue         # Tap zones + swipe layer
  stores/
    storyStore.js                 # Pinia: stories, viewed state, current group
  composables/
    useStoryPlayer.js             # Play/pause/next/prev logic
    useStoryTimer.js              # Per-story timer with pause/resume
    useStoryGesture.js            # Swipe detection (left/right/down)
    useKeyboard.js                # ESC, Space, Arrow keys
    usePreload.js                 # IntersectionObserver preload next story
  utils/
    storyHelper.js                # Grouping, enrich, findIndex helpers
    time.js                       # Relative time formatting
  assets/
    mock/
      stories.js                  # Mock data from dummy.js subset
```

## Data Flow

1. **`storyStore.fetchStories()`** — calls `storyApi.getByUser()`, groups by `userId`, stores as `userGroups[]`
2. **`StoryList`** — reads `storyStore.userGroups`, renders `StoryCard` per user, handles scroll + touch
3. **Click StoryCard** → `storyStore.openViewer(groupIndex)` → sets `viewerVisible = true`
4. **`StoryViewer`** mounts, reads `storyStore.currentGroup`, renders progress + header + image/video + footer
5. **`useStoryPlayer`** controls auto-advance via `useStoryTimer` (each story's `durationMs`), handles `goNext`/`goPrev` (within group → next group → close)
6. **`useStoryGesture`** handles swipe left/right/down
7. **`useKeyboard`** handles ESC (close), Space (pause), Arrows (navigate)
8. **`usePreload`** preloads next story image/video via `IntersectionObserver` + `new Image()`

## Component Details

### StoryList.vue
- Horizontal scroll container with snap points
- Left/right arrow buttons (hidden on mobile, shown on hover desktop)
- Renders `StoryCard` for each `userGroup`
- Lazy loads cards via IntersectionObserver
- Touch scroll support
- Props: none (reads from store)

### StoryCard.vue
- Circular avatar image
- Blue gradient ring if unseen, gray if all viewed
- Username label below
- Own story at front
- Props: `{ group: Object, isOwn: Boolean }`

### StoryViewer.vue
- Fullscreen overlay (Teleport to body)
- Open animation: `transform: scale(0.95 → 1)` + `opacity: 0 → 1` over 300ms ease-out
- Close animation: reverse
- Contains: `StoryProgress`, `StoryHeader`, `StoryImage`/`StoryVideo`, `StoryFooter`, `StoryNavigation`
- Story transition: slide (horizontal) + fade, 250ms
- Responsive: max-width 420px on desktop, full width on mobile

### StoryImage.vue
- `object-fit: cover`
- Ken Burns effect: slow scale (1 → 1.05) + translate animation, duration matches story duration (CSS var)
- Loading spinner overlay until `@load`
- Props: `{ story: Object, isActive: Boolean }`

### StoryVideo.vue
- `<video>` element with muted autoplay
- Loading spinner until `canplay`
- Auto-pause on visibility change (`document.hidden`)
- Auto-next on `ended`
- Mute toggle button
- Props: `{ story: Object, isActive: Boolean }`
- Emits: `mute-change`

### StoryHeader.vue
- Row: avatar (32px) + username + relative time + close button (✕)
- Props: `{ story: Object }`

### StoryFooter.vue
- Reply input (text field + send button)
- Reaction bar (❤️, 😂, 😮, 😢, 😡 quick emoji)
- Share button
- Reaction picker popover (`StoryReaction`)
- Props: `{ story: Object }`

### StoryProgress.vue
- Segmented bar: segments = `currentGroup.stories.length`
- Each segment fills at `story.durationMs` rate
- Completed segments: solid white
- Active segment: animating
- Remaining: transparent
- Paused state: animation-play-state: paused
- Props: `{ group: Object, currentIndex: Number, paused: Boolean }`

### StoryNavigation.vue
- Invisible tap zones: left 35% (prev), right 35% (next), center (pause)
- Swipe layer: detects horizontal (prev/next) and vertical (close) swipes
- Props: none (emits events up)

## Store Design (storyStore.js)

```js
state: () => ({
  userGroups: [],       // [{ userId, userName, userAvatar, stories: [...] }]
  viewers: {},          // { [userId]: lastViewedStoryId }
  currentGroupIndex: -1,
  viewerVisible: false,
})
```

Actions:
- `fetchStories()` — load from API, group, sort (own first)
- `openViewer(groupIndex)` — set currentGroupIndex, show viewer
- `closeViewer()` — hide viewer
- `markViewed(userId, storyId)` — update viewers map
- `nextGroup()` — advance to next group or close

## Composables

### useStoryPlayer(storyStore)
Returns: `{ currentIndex, paused, goNext, goPrev, goTo, play, pause, togglePause }`
- Manages current story index within current group
- Calls `useStoryTimer` for auto-advance
- Handles end-of-group → `nextGroup()` or close

### useStoryTimer(callback, getDuration)
Returns: `{ start, stop, pause, resume }`
- Calls `callback` after `getDuration()` ms
- Supports pause/resume (clearTimeout + remaining time tracking)

### useStoryGesture(el)
Returns: `{ onTouchStart, onTouchEnd, swipeDirection }`
- Detects horizontal (prev/next) and vertical (close) swipes
- Threshold: 50px horizontal, 80px vertical

### useKeyboard(handlers)
- Registers `keydown` listener
- Handlers: `{ ArrowLeft, ArrowRight, Escape, Space }`
- Cleanup on unmount

### usePreload(stories, currentIndex)
- Preloads `stories[currentIndex + 1]` image/video
- Uses `IntersectionObserver` + `new Image()`
- Triggered when currentIndex changes

## Mock Data

Extract story-related mock from `dummy.js` into `assets/mock/stories.js`:
- 80 stories across 30 users
- Each story: `{ id, userId, mediaUrl, mediaType, caption, durationMs, createdAt, expiresAt, viewCount }`
- Enriched with `userName`, `userAvatar` from users array

## States & Edge Cases

### Loading
- Story list: skeleton shimmer while fetching
- Story viewer: spinner overlay until image/video loads

### Empty
- No stories: hide story strip entirely

### Error
- Image load error: show fallback placeholder with emoji
- Video load error: show error state, allow retry

### Edge Cases
- Single user with all stories: progress bar shows all segments, close after last
- Single story: progress bar = 1 segment
- Rapid tapping: debounce goNext/goPrev to prevent double-fire
- All stories viewed: all rings gray
- Own story always first regardless of viewed state
- Video autoplay blocked: show play button overlay
- Tab switch during video: pause, resume on return

## Performance

- Lazy load story list cards via IntersectionObserver
- Preload next story image/video
- Cleanup: removeEventListener on unmount for all composables
- Timer cleanup: clearTimeout on pause/unmount
- Video cleanup: pause + reset src on unmount
- No render of non-active stories in viewer
- CSS animations for 60 FPS (transform/opacity only, no layout triggers)

## Responsive

- Desktop: max-width 420px centered in overlay
- Mobile: full width, full height overlay
- Story list: horizontal scroll, arrow buttons on desktop, touch scroll on mobile
- Footer: sticky at bottom, compact on mobile

## Integration

- Replace `StoryViewer` import in `Home.vue` and `Blog.vue` with new `StoryList` + `StoryViewer`
- Delete old `src/components/blog/StoryViewer.vue`
- Update `src/api/story.js` if needed (likely compatible)
- Add new mock file `assets/mock/stories.js`
- Register `storyStore` in Pinia

## Future Considerations (Out of Scope)

- Camera/upload integration
- Story replies persistence
- Story views count display
- Hashtag/location stickers
- Music overlay
- Poll/slider stickers
