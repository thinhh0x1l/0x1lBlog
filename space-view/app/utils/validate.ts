import { z } from 'zod'
import type { $ZodErrorTree } from 'zod/v4/core'

export function validateSchema<T extends Record<string, any>>(
  schema: z.ZodSchema<T>,
  data: T
): {
  valid: boolean
  errors: Partial<Record<keyof T, string>>
} {
  const result = schema.safeParse(data)
  if (!result.success) {
    const tree: $ZodErrorTree<T, string> = z.treeifyError(result.error)
    const errors: Partial<Record<keyof T, string>> = {}
    for (const key in (tree as any).properties) {
      const message = (tree as any).properties[key].errors[0]
      if (message && message.length > 0)
        errors[key as keyof T] = message
    }
    return { valid: false, errors }
  }
  return { valid: true, errors: {} }
}