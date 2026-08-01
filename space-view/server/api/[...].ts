export default defineEventHandler(async (event) => {
  const config = useRuntimeConfig()
  const path = event.path?.replace('/api', '') || ''
  const method = event.method

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  }

  const authHeader = getHeader(event, 'authorization')
  if (authHeader) headers['Authorization'] = authHeader

  const guestToken = getHeader(event, 'x-guest-token')
  if (guestToken) headers['X-Guest-Token'] = guestToken

  try {
    const body = method !== 'GET' ? await readBody(event) : undefined
    const response = await $fetch(`${config.public.apiBase}${path}`, {
      method: method as any,
      headers,
      body,
    })
    return response
  } catch (error: any) {
    throw createError({
      statusCode: error.status || 500,
      message: error.message || 'Internal Server Error',
    })
  }
})