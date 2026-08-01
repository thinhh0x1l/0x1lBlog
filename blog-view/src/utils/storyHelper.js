export const enrichStory = (story, users) => {
  const author = users.find(u => u.id === story.userId)
  return {
    ...story,
    userName: author?.displayName || 'Người dùng',
    userAvatar: author?.avatarUrl || '',
  }
}

export const groupByUser = (stories) => {
  const map = new Map()
  for (const s of stories) {
    if (!map.has(s.userId)) {
      map.set(s.userId, {
        userId: s.userId,
        userName: s.userName || 'Người dùng',
        userAvatar: s.userAvatar || '',
        storyCount: 0,
        stories: [],
      })
    }
    const group = map.get(s.userId)
    group.stories.push(s)
    group.storyCount++
  }
  return Array.from(map.values())
}

export const findFlatIndex = (groups, userId) => {
  let idx = 0
  for (const g of groups) {
    if (g.userId === userId) return idx
    idx += g.stories.length
  }
  return 0
}
