export function okJson(data) {
  return {
    ok: true,
    status: 200,
    headers: new Map([['content-type', 'application/json']]),
    json: async () => data,
    text: async () => JSON.stringify(data),
  };
}

export function errorResponse(status = 500, problem = { status, title: 'Error', detail: 'Falla' }) {
  const body = typeof problem === 'string' ? problem : JSON.stringify(problem);
  return {
    ok: false,
    status,
    headers: new Map([['content-type', 'application/json']]),
    json: async () => {
      try { return JSON.parse(body); } catch { return { status, detail: body }; }
    },
    text: async () => body,
  };
}