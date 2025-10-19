import { defineConfig } from 'vitest/config'
import { fileURLToPath } from 'node:url'

export default defineConfig({
  test: {
    environment: 'node',           // por defecto node; los de UI ya ponen jsdom por file
    globals: true,
    include: ['src/test/js/**/*.test.{js,jsx,ts,tsx}'],
    coverage: {
      provider: 'v8',
      all: true, // cuenta también archivos no tocados por tests
      include: ['src/main/resources/static/js/**/*.js'],
      exclude: [
        'src/test/**',
        'target/**'
      ],
    },
  },
  resolve: {
    alias: {
      '@app': fileURLToPath(new URL('./src/main/resources/static/js', import.meta.url)),
      '@test': fileURLToPath(new URL('./src/test/js', import.meta.url)),
    },
  },
});