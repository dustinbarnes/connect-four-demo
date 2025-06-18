export default {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
    '\\.(svg)$': '<rootDir>/src/vite-env.d.ts',
  },
  setupFilesAfterEnv: ['@testing-library/jest-dom'],
};
