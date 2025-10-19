export const okJson = (data) => ({ ok: true, json: async () => data });

export const errorResponse = (status = 400, body = { status }) => ({
  ok: false,
  status,
  json: async () => body,
});