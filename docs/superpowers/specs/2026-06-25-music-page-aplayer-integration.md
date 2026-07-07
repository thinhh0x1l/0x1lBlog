# Music Page — APlayer Integration

## Overview

Replace the sidebar MusicBox component with a full-featured `/music` section powered by APlayer.js. The music section has dedicated pages with per-song reactions and comments, and an extensible plugin architecture for future features.

## Routes

| Path | Component | Purpose |
|------|-----------|---------|
| `/music` | MusicList.vue | Browse public playlists (grid cards) |
| `/music/:id` | MusicDetail.vue | Full playlist page with APlayer |

## MusicDetail.vue Layout

```
OneColumnLayout (max-width ~900px)

┌────────────────────────────────┐
│ APlayer.js (full width)         │
│ playlist cover as bg           │
│ native playback controls       │
│ built-in playlist (right)      │
├────────────────────────────────┤
│ Current song info              │
│ Title — Artist                  │
├────────────────────────────────┤
│ Reactions (per current song)    │
│ ❤️ 👍 😂 🔥 😮  — with counts │
├────────────────────────────────┤
│ Comments (per current song)     │
│ (switched via listSwitch event)│
│ threaded, max 2 levels         │
│ [Write a comment...]           │
└────────────────────────────────┘

Right sidebar: Suggested playlists
```

- `currentSongIndex` tracked via APlayer's `listswitch` event
- Reactions + comments re-fetch when song changes
- Comments API filters by `songId`

## APlayerMusicBox.vue Component

The core component wrapping APlayer.js. Replaces MusicBox.vue in the sidebar and also used in MusicDetail.vue.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `playlist` | Object | required | Playlist with songs array |
| `isLoggedIn` | Boolean | false | Auth state |
| `compact` | Boolean | false | Sidebar mode vs full page mode |
| `plugins` | APlayerPlugin[] | [] | Extensions array |

### Internal state

- `currentIndex` (synced from APlayer's `listswitch`)
- `isPlaying` (synced from APlayer's `play`/`pause`)
- `currentSong` (computed from currentIndex)
- `ap` (APlayer instance ref, NOT exposed to parent)

### Plugin API

```typescript
interface APlayerPlugin {
  name: string
  // Source resolution: map song → audio URL for APlayer
  resolveSource?: (song: Song) => string | null
  // Vue components for UI injection
  songActions?: Component  // rendered per song item (e.g. vote buttons)
  toolbarActions?: Component // rendered in toolbar area
  addSongSection?: Component // rendered below playlist
  // Lifecycle hooks
  onInit?: (ap: APlayer) => void
  onPlay?: (song: Song) => void
  onPause?: (song: Song) => void
  onListSwitch?: (index: number, song: Song) => void
  onError?: (err: any) => void
  // APlayer theme/option overrides
  aplayerOptions?: Partial<APlayerOptions>
}
```

### Built-in plugins

| Plugin | Features |
|--------|----------|
| `VotePlugin` | ▲/▼ per song, vote count, color transitions |
| `PlaylistManagerPlugin` | Add song form, remove button (owner only) |
| `UrlSourcePlugin` | Default: reads `song.audioUrl` as APlayer audio URL |

### APlayer integration pattern

```js
// APlayer — instantiated imperatively (not template)
// plugins are resolved before creating APlayer
const resolvedAudio = playlist.songs.map(song => {
  const url = resolveSource(song, plugins)
  return {
    name: song.title,
    artist: song.artist,
    url,
    cover: song.thumbnailUrl,
  }
})

ap = new APlayer({
  container: containerRef.value,
  audio: resolvedAudio,
  fixed: false,
  mini: compact,
  ...mergedPluginOptions,
})

ap.on('listswitch', (index) => {
  currentIndex.value = index
  emit('songChange', currentSong.value)
})
```

- `ap` is kept private inside the component
- Parent interacts via props + events
- For `mini/compact` mode (sidebar), APlayer.mini is used
- For full page (MusicDetail), full APlayer UI shown with cover art background

## Mock Data Changes (src/data/dummy.js)

### Song structure
```js
// Replace source: 'youtube' with audioUrl — direct MP3
{
  id: 1,
  playlistId: 1,
  addedBy: 1,
  title: 'Coding Session',
  artist: 'Lofi Artist',
  source: 'url',
  sourceId: '',
  sourceUrl: '',
  audioUrl: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3',
  thumbnailUrl: 'https://picsum.photos/seed/music0/100/100',
  durationSec: 180 + Math.floor(Math.random() * 180),
  sortOrder: 0,
  voteCount: Math.floor(Math.random() * 20) - 5,
  isApproved: true,
  createdAt: '',
}
```

All songs use SoundHelix MP3 URLs (Song-1.mp3 through Song-16.mp3).

## API Changes (src/api/)

### playlistApi
- Add `getAll()` → returns public playlists (for /music browse)
- `getById(id)` → already exists, attach songs

### commentApi
New mock API for song comments:
- `getBySong(songId)` → comments for a song
- `create(songId, content, parentId?)` → new comment
- Comments shape: `{ id, songId, userId, authorName, authorAvatar, content, parentId, createdAt }`

### reactionApi
New mock API for song reactions:
- `getBySong(songId)` → reactions summary
- `toggle(songId, type)` → toggle reaction
- Reaction types: `LIKE`, `LOVE`, `HAHA`, `FIRE`, `SAD`

## New Components

| File | Purpose |
|------|---------|
| `src/view/music/MusicList.vue` | Browse page — grid of playlist cards |
| `src/view/music/MusicDetail.vue` | Playlist page — APlayer + comments + reactions |
| `src/components/blog/APlayerMusicBox.vue` | Core APlayer wrapper with plugin system |
| `src/components/music/ReactionBar.vue` | Per-song reaction buttons |
| `src/components/music/CommentSection.vue` | Per-song comment section |
| `src/components/music/SuggestedPlaylists.vue` | Right sidebar suggested playlists |

## Existing Component Changes

### MusicBox.vue
- Remove or keep as fallback — replaced by APlayerMusicBox.vue

### Home.vue
- Replace `<MusicBox>` import with `<APlayerMusicBox compact>` — APlayerMusicBox in sidebar mode with `UrlSourcePlugin` + `VotePlugin`

## Deleted/Removed

- `src/components/blog/MusicBox.vue` — replaced by APlayerMusicBox.vue

## Plugin System Implementation

### Injection mechanism

Plugins' Vue components are injected via dynamic components:

```vue
<component 
  v-for="plugin in plugins" 
  :is="plugin.songActions" 
  :song="song" 
  :key="plugin.name" 
/>
```

Each plugin component receives the song data as props. The plugin context (APlayer instance, currentIndex, playlist) is provided via provide/inject.

### Source resolution pipeline

```
song.audioUrl? ← UrlSourcePlugin
     ↓ null
each plugin.resolveSource(song) → first non-null URL wins
     ↓ null
fall back to song.sourceUrl or ''
```

### Event hooks

APlayer events are bridged to plugin hooks:

```js
ap.on('play', () => {
  plugins.forEach(p => p.onPlay?.(currentSong.value))
})
ap.on('listswitch', (index) => {
  currentIndex.value = index
  plugins.forEach(p => p.onListSwitch?.(index, currentSong.value))
})
```

## Dark Theme Compatibility

All new components use CSS variables (`--surface`, `--text-primary`, etc.) — no hardcoded colors. APlayer's theme color synced via `var(--primary)`:

```js
new APlayer({
  theme: getComputedStyle(document.documentElement)
    .getPropertyValue('--primary').trim() || '#0ea5e9',
})
```

## Build Verification

- `npm run build` must pass (vue-tsc + vite build)
- No new npm packages required (aplayer already in dependencies)

## Future Extensions

Plugins can be developed independently and registered:

```js
// Community SoundCloud plugin
const SoundCloudPlugin = {
  name: 'soundcloud',
  resolveSource(song) {
    if (song.source === 'soundcloud') {
      return `https://api.soundcloud.com/tracks/${song.sourceId}/stream`
    }
  },
}
```
