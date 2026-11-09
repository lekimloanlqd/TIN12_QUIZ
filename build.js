const fs = require('fs');
const path = require('path');

const rootDir = __dirname;
const indexPath = path.join(rootDir, 'index.html');
const distDir = path.join(rootDir, 'dist');
const publicDir = path.join(rootDir, 'public');

if (!fs.existsSync(distDir)) {
  fs.mkdirSync(distDir, { recursive: true });
}

// Check environment variables at build time
const supabaseUrl = process.env.SUPABASE_URL || process.env.VITE_SUPABASE_URL || '';
const supabaseAnonKey = process.env.SUPABASE_ANON_KEY || process.env.VITE_SUPABASE_ANON_KEY || '';

let htmlContent = fs.readFileSync(indexPath, 'utf8');
if (supabaseUrl && supabaseAnonKey) {
  const envScript = `<script>window.SUPABASE_ENV = { url: ${JSON.stringify(supabaseUrl)}, anonKey: ${JSON.stringify(supabaseAnonKey)} };</script>`;
  htmlContent = htmlContent.replace('<!-- __SUPABASE_ENV_SLOT__ -->', envScript);
}

// 1. Sync the real app into public/index.html so public is never a redirect stub
if (fs.existsSync(publicDir)) {
  fs.writeFileSync(path.join(publicDir, 'index.html'), htmlContent, 'utf8');
}

// 2. Copy all public assets to dist/ (skip index.html from public)
if (fs.existsSync(publicDir)) {
  const copyRecursive = (src, dest) => {
    const entries = fs.readdirSync(src, { withFileTypes: true });
    for (let entry of entries) {
      const srcPath = path.join(src, entry.name);
      const destPath = path.join(dest, entry.name);
      if (entry.isDirectory()) {
        if (!fs.existsSync(destPath)) fs.mkdirSync(destPath, { recursive: true });
        copyRecursive(srcPath, destPath);
      } else if (entry.name !== 'index.html') {
        fs.copyFileSync(srcPath, destPath);
      }
    }
  };
  copyRecursive(publicDir, distDir);
}

// 3. Guarantee that dist/index.html is the real complete Quiz Smart application
fs.writeFileSync(path.join(distDir, 'index.html'), htmlContent, 'utf8');

console.log('[Quiz Smart] Build complete! Real index.html copied to dist/ and public/. Size:', fs.statSync(path.join(distDir, 'index.html')).size, 'bytes');

