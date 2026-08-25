/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BASE_API_IMG: string;
  readonly VITE_BASE_API: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
