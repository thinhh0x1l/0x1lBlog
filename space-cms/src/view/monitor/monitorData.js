const pages = [
  '/home',
  '/archives',
  '/blog/42',
  '/blog/18',
  '/blog/7',
  '/moments',
  '/about',
  '/tag/java',
  '/tag/vue',
  '/category/backend',
  '/category/frontend'
]

const referrers = ['', 'https://google.com', 'https://github.com', 'https://x.com', '/home', '/archives', '/blog/42']
const locations = ['Ho Chi Minh, VN', 'Ha Noi, VN', 'Da Nang, VN', 'Can Tho, VN', 'Singapore', 'Bangkok, TH']
const osList = ['Windows 11', 'macOS 15', 'Android 15', 'iOS 18', 'Ubuntu 24.04']
const browsers = ['Chrome 126', 'Safari 18', 'Edge 126', 'Firefox 127', 'Chrome Mobile']
const behaviors = ['SITE_BOOTSTRAP', 'BLOG_VIEW', 'COMMENT_LOAD', 'COMMENT_CREATE', 'LIKE', 'SEARCH', 'MUSIC_LOAD', 'ABOUT_VIEW']
const methods = ['GET', 'POST', 'PUT']

const start = new Date('2026-06-06T08:00:00').getTime()

function pad(value) {
  return String(value).padStart(2, '0')
}

function isoFromMinute(minute, plusSeconds = 0) {
  return new Date(start + minute * 60_000 + plusSeconds * 1000).toISOString()
}

function pick(list, index) {
  return list[index % list.length]
}

function hash(index) {
  return `gst_${(index * 982451653).toString(16).slice(0, 8)}`
}

export const guests = Array.from({ length: 64 }, (_, index) => {
  const id = index + 1
  const lastMinute = 20 + index * 7
  return {
    id,
    guestHash: hash(id),
    label: `G${pad(id)}`,
    firstSeen: isoFromMinute(Math.max(0, lastMinute - 280)),
    lastSeen: isoFromMinute(lastMinute + 18),
    lastIp: `14.${80 + (id % 90)}.${20 + (id % 70)}.${8 + (id % 180)}`,
    ipSource: pick(locations, id),
    os: pick(osList, id),
    browser: pick(browsers, id + 2),
    status: id % 11 === 0 ? 'Risk' : id % 5 === 0 ? 'Returning' : 'Normal'
  }
})

export const visits = Array.from({ length: 180 }, (_, index) => {
  const id = index + 1
  const guest = guests[index % guests.length]
  const startedMinute = 8 + index * 5
  const duration = 45 + (index % 13) * 37
  const pv = 1 + (index % 5)

  return {
    id,
    guestId: guest.id,
    guestHash: guest.guestHash,
    ip: guest.lastIp,
    ipSource: guest.ipSource,
    os: guest.os,
    browser: guest.browser,
    userAgent: `Mozilla/5.0 ${guest.os} ${guest.browser}`,
    pv,
    startedAt: isoFromMinute(startedMinute),
    lastActivity: isoFromMinute(startedMinute, duration),
    entryPage: pick(pages, index),
    exitPage: pick(pages, index + pv),
    active: index > 165
  }
})

export const pageViews = visits.flatMap((visit, visitIndex) => {
  return Array.from({ length: visit.pv }, (_, index) => {
    const id = visit.id * 10 + index
    const minuteOffset = visitIndex * 5 + index * 2
    return {
      id,
      visitId: visit.id,
      guestId: visit.guestId,
      page: pick(pages, visitIndex + index),
      referrer: index === 0 ? pick(referrers, visitIndex) : pick(pages, visitIndex + index - 1),
      stayDurationSeconds: 18 + ((visitIndex + index) % 16) * 11,
      createdAt: isoFromMinute(8 + minuteOffset)
    }
  })
})

export const events = pageViews.flatMap((pageView, pageIndex) => {
  const eventCount = 2 + (pageIndex % 4)
  return Array.from({ length: eventCount }, (_, index) => {
    const behavior = pick(behaviors, pageIndex + index)
    const fail = (pageIndex + index) % 17 === 0 || behavior === 'MUSIC_LOAD' && pageIndex % 9 === 0
    const statusCode = fail ? (behavior === 'MUSIC_LOAD' ? 504 : 400) : 200
    return {
      id: pageView.id * 10 + index,
      pageViewId: pageView.id,
      visitId: pageView.visitId,
      guestId: pageView.guestId,
      page: pageView.page,
      uri: uriForBehavior(behavior, pageView.page),
      method: methodForBehavior(behavior, index),
      behavior,
      source: ['BLOG_VIEW', 'LIKE', 'SEARCH', 'COMMENT_CREATE', 'ABOUT_VIEW'].includes(behavior) ? 'USER' : 'SYSTEM',
      content: contentForBehavior(behavior, pageView.page, fail),
      statusCode,
      success: !fail,
      remark: fail ? failureRemark(behavior) : '',
      param: JSON.stringify({ page: pageView.page, guestId: pageView.guestId }),
      responseTimeMs: fail ? 900 + (pageIndex % 10) * 320 : 18 + ((pageIndex + index) % 24) * 9,
      createdAt: isoFromMinute(8 + pageIndex * 2, index * 14)
    }
  })
})

export function uriForBehavior(behavior, page) {
  const map = {
    SITE_BOOTSTRAP: '/site',
    BLOG_VIEW: page.startsWith('/blog/') ? `/blog?id=${page.split('/').pop()}` : page,
    COMMENT_LOAD: '/comment-tree',
    COMMENT_CREATE: '/comment',
    LIKE: '/moment/like',
    SEARCH: '/search-blog',
    MUSIC_LOAD: '/music/ZW9ABC',
    ABOUT_VIEW: '/about'
  }
  return map[behavior] || page
}

function methodForBehavior(behavior, index) {
  if (behavior === 'COMMENT_CREATE') return 'POST'
  if (behavior === 'LIKE') return 'PUT'
  return pick(methods, index)
}

function contentForBehavior(behavior, page, fail) {
  if (fail) return `${behavior} failed on ${page}`
  const map = {
    SITE_BOOTSTRAP: 'Load site settings',
    BLOG_VIEW: `Read ${page}`,
    COMMENT_LOAD: 'Load comment tree',
    COMMENT_CREATE: 'Create visitor comment',
    LIKE: 'Like a moment',
    SEARCH: 'Search blog keyword',
    MUSIC_LOAD: 'Load music metadata',
    ABOUT_VIEW: 'Open about page'
  }
  return map[behavior] || page
}

function failureRemark(behavior) {
  if (behavior === 'MUSIC_LOAD') return 'Upstream timeout'
  if (behavior === 'COMMENT_CREATE') return 'Validation failed'
  return 'Unexpected response'
}

export function formatDateTime(value) {
  return new Intl.DateTimeFormat('vi-VN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  }).format(new Date(value))
}

export function formatDuration(startAt, endAt) {
  const seconds = Math.max(0, Math.round((new Date(endAt) - new Date(startAt)) / 1000))
  const minutes = Math.floor(seconds / 60)
  const rest = seconds % 60
  return minutes ? `${minutes}m ${rest}s` : `${rest}s`
}

export function getVisitPageViews(visitId) {
  return pageViews.filter((item) => item.visitId === visitId)
}

export function getVisitEvents(visitId) {
  return events.filter((item) => item.visitId === visitId)
}

export function getGuestVisits(guestId) {
  return visits.filter((item) => item.guestId === guestId)
}

export function getGuestEvents(guestId) {
  return events.filter((item) => item.guestId === guestId)
}

export function getPageEvents(page) {
  return events.filter((item) => item.page === page)
}

export function aggregatePages() {
  return pages.map((page) => {
    const pvs = pageViews.filter((item) => item.page === page)
    const pageEvents = getPageEvents(page)
    const uniqueGuests = new Set(pvs.map((item) => item.guestId)).size
    const avgStay = pvs.length
      ? Math.round(pvs.reduce((sum, item) => sum + item.stayDurationSeconds, 0) / pvs.length)
      : 0
    const avgResponse = pageEvents.length
      ? Math.round(pageEvents.reduce((sum, item) => sum + item.responseTimeMs, 0) / pageEvents.length)
      : 0
    const failed = pageEvents.filter((item) => !item.success).length

    return {
      page,
      views: pvs.length,
      uniqueGuests,
      avgStay,
      events: pageEvents.length,
      likes: pageEvents.filter((item) => item.behavior === 'LIKE').length,
      commentLoads: pageEvents.filter((item) => item.behavior === 'COMMENT_LOAD').length,
      failRate: pageEvents.length ? Math.round((failed / pageEvents.length) * 100) : 0,
      avgResponse
    }
  }).sort((a, b) => b.views - a.views)
}

export function aggregateBehaviors(sourceEvents = events) {
  return behaviors.map((behavior) => ({
    name: behavior,
    value: sourceEvents.filter((item) => item.behavior === behavior).length
  })).filter((item) => item.value > 0)
}

export function aggregateTraffic() {
  const buckets = Array.from({ length: 12 }, (_, index) => {
    const hour = 8 + index
    return {
      label: `${pad(hour)}:00`,
      visits: 0,
      pageViews: 0,
      events: 0,
      failed: 0
    }
  })

  visits.forEach((visit) => {
    const hour = new Date(visit.startedAt).getHours() - 8
    if (buckets[hour]) buckets[hour].visits += 1
  })
  pageViews.forEach((pageView) => {
    const hour = new Date(pageView.createdAt).getHours() - 8
    if (buckets[hour]) buckets[hour].pageViews += 1
  })
  events.forEach((event) => {
    const hour = new Date(event.createdAt).getHours() - 8
    if (!buckets[hour]) return
    buckets[hour].events += 1
    if (!event.success) buckets[hour].failed += 1
  })

  return buckets
}

export function statusType(success, statusCode) {
  if (success) return 'success'
  if (statusCode >= 500) return 'danger'
  return 'warning'
}

export function behaviorType(behavior) {
  if (behavior.includes('COMMENT')) return 'warning'
  if (behavior.includes('LIKE')) return 'danger'
  if (behavior.includes('SEARCH')) return 'success'
  if (behavior.includes('MUSIC')) return 'info'
  return 'primary'
}
