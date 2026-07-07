import { h, defineComponent, inject, ref } from 'vue'
import { ElMessage, ElButton } from 'element-plus'

export const UrlSourcePlugin = {
  name: 'url-source',
  resolveSource(song) {
    return song.audioUrl || null
  },
}

export const VotePlugin = {
  name: 'vote',
  songActions: defineComponent({
    name: 'VoteActions',
    props: { song: Object, index: Number },
    setup(props) {
      const context = inject('playerContext')
      const vote = (dir) => {
        const song = props.song
        if (!song) return
        if (dir > 0) {
          song.voteCount = (song.voteCount || 0) + 1
        } else {
          song.voteCount = (song.voteCount || 0) - 1
        }
      }
      return () => h('div', { class: 'vote-actions' }, [
        h('button', {
          class: 'vote-btn vote-up',
          onClick: (e) => { e.stopPropagation(); vote(1) },
          title: 'Thích',
        }, '▲'),
        h('span', {
          class: ['vote-count', { hot: (props.song?.voteCount || 0) >= 5, cold: (props.song?.voteCount || 0) <= -3 }],
        }, props.song?.voteCount || 0),
        h('button', {
          class: 'vote-btn vote-down',
          onClick: (e) => { e.stopPropagation(); vote(-1) },
          title: 'Không thích',
        }, '▼'),
      ])
    },
  }),
}

export const PlaylistManagerPlugin = {
  name: 'playlist-manager',
  addSongSection: defineComponent({
    name: 'PlaylistManagerSection',
    props: { playlist: Object, ap: Object },
    setup(props) {
      const context = inject('playerContext')
      const showForm = ref(false)
      const title = ref('')
      const artist = ref('')

      const addSong = () => {
        if (!title.value || !artist.value) {
          ElMessage.warning('Vui lòng nhập tên bài hát và nghệ sĩ')
          return
        }
        const newSong = {
          id: Date.now(),
          playlistId: props.playlist?.id,
          title: title.value,
          artist: artist.value,
          source: 'url',
          audioUrl: `https://www.soundhelix.com/examples/mp3/SoundHelix-Song-${Math.floor(Math.random() * 16) + 1}.mp3`,
          thumbnailUrl: `https://picsum.photos/seed/music${Date.now()}/100/100`,
          durationSec: 180 + Math.floor(Math.random() * 180),
          voteCount: 0,
        }
        props.playlist?.songs?.push(newSong)
        const ap = context?.ap?.value
        if (ap) {
          ap.list.add({
            name: newSong.title,
            artist: newSong.artist,
            url: newSong.audioUrl,
            cover: newSong.thumbnailUrl,
          })
        }
        showForm.value = false
        title.value = ''
        artist.value = ''
        ElMessage.success('Đã thêm bài hát')
      }

      return () => {
        const children = []
        children.push(h('div', { class: 'playlist-manager-section' }, [
          h('button', {
            class: 'add-song-btn',
            onClick: () => { showForm.value = !showForm.value },
          }, [
            h('span', '+'),
            h('span', ' Thêm nhạc'),
          ]),
        ]))

        if (showForm.value) {
          children.push(h('div', { class: 'add-song-form' }, [
            h('input', {
              class: 'song-input',
              placeholder: 'Tên bài hát',
              value: title.value,
              onInput: (e) => { title.value = e.target.value },
            }),
            h('input', {
              class: 'song-input',
              placeholder: 'Nghệ sĩ',
              value: artist.value,
              onInput: (e) => { artist.value = e.target.value },
            }),
            h('div', { class: 'add-song-actions' }, [
              h(ElButton, { size: 'small', type: 'primary', onClick: addSong }, () => 'Thêm'),
              h(ElButton, { size: 'small', onClick: () => { showForm.value = false } }, () => 'Hủy'),
            ]),
          ]))
        }

        return h('div', null, children)
      }
    },
  }),
}
