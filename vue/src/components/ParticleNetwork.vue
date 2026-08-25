<template>
  <canvas ref="canvasRef" class="particle-network" aria-hidden="true"></canvas>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from "vue";

interface Particle {
  x: number;
  y: number;
  vx: number;
  vy: number;
  radius: number;
}

const canvasRef = ref<HTMLCanvasElement>();

// 连线距离阈值
const LINK_DIST = 120;
// 鼠标影响半径
const MOUSE_RADIUS = 150;
// 粒子数量
const PARTICLE_COUNT = 120;
// 主色（Element Plus 主色 #409EFF）
const R = 64, G = 158, B = 255;

let ctx: CanvasRenderingContext2D;
let width = 0;
let height = 0;
let particles: Particle[] = [];
const mouse = { x: -9999, y: -9999 };
let raf = 0;
let running = false;

function resize() {
  const canvas = canvasRef.value;
  if (!canvas) return;
  const rect = canvas.parentElement?.getBoundingClientRect();
  if (!rect) return;
  width = canvas.width = rect.width;
  height = canvas.height = rect.height;
  initParticles();
}

function initParticles() {
  particles = Array.from({ length: PARTICLE_COUNT }, () => ({
    x: Math.random() * width,
    y: Math.random() * height,
    vx: (Math.random() - 0.5) * 0.6,
    vy: (Math.random() - 0.5) * 0.6,
    radius: Math.random() * 2 + 1,
  }));
}

function draw() {
  ctx.clearRect(0, 0, width, height);
  // 粒子间连线
  for (let i = 0; i < particles.length; i++) {
    const p1 = particles[i]!;
    for (let j = i + 1; j < particles.length; j++) {
      const p2 = particles[j]!;
      const dist = Math.hypot(p1.x - p2.x, p1.y - p2.y);
      if (dist < LINK_DIST) {
        ctx.strokeStyle = `rgba(${R}, ${G}, ${B}, ${(1 - dist / LINK_DIST) * 0.4})`;
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.moveTo(p1.x, p1.y);
        ctx.lineTo(p2.x, p2.y);
        ctx.stroke();
      }
    }
    // 鼠标与粒子连线（高亮）
    const mdist = Math.hypot(p1.x - mouse.x, p1.y - mouse.y);
    if (mdist < MOUSE_RADIUS) {
      ctx.strokeStyle = `rgba(${R}, ${G}, ${B}, ${(1 - mdist / MOUSE_RADIUS) * 0.8})`;
      ctx.lineWidth = 1.2;
      ctx.beginPath();
      ctx.moveTo(p1.x, p1.y);
      ctx.lineTo(mouse.x, mouse.y);
      ctx.stroke();
    }
  }
  // 绘制粒子
  for (const p of particles) {
    ctx.beginPath();
    ctx.arc(p.x, p.y, p.radius, 0, Math.PI * 2);
    ctx.fillStyle = `rgba(${R}, ${G}, ${B}, 0.8)`;
    ctx.fill();
  }
}

function step() {
  for (const p of particles) {
    // 鼠标排斥力：粒子被推开
    const dx = p.x - mouse.x;
    const dy = p.y - mouse.y;
    const dist = Math.hypot(dx, dy) || 1;
    if (dist < MOUSE_RADIUS) {
      const force = (1 - dist / MOUSE_RADIUS) * 0.4;
      p.vx += (dx / dist) * force;
      p.vy += (dy / dist) * force;
    }
    // 边界反弹
    if (p.x < 0 || p.x > width) p.vx *= -1;
    if (p.y < 0 || p.y > height) p.vy *= -1;
    p.x += p.vx;
    p.y += p.vy;
  }
}

function loop() {
  if (!running) return;
  step();
  draw();
  raf = requestAnimationFrame(loop);
}

function onMouseMove(e: MouseEvent) {
  const canvas = canvasRef.value;
  if (!canvas) return;
  const rect = canvas.getBoundingClientRect();
  mouse.x = e.clientX - rect.left;
  mouse.y = e.clientY - rect.top;
}

function onMouseLeave() {
  mouse.x = -9999;
  mouse.y = -9999;
}

onMounted(() => {
  const canvas = canvasRef.value;
  if (!canvas) return;
  ctx = canvas.getContext("2d")!;
  resize();
  // 监听 window 上的 mousemove（canvas 是 pointer-events:none，不拦截表单操作）
  window.addEventListener("resize", resize);
  window.addEventListener("mousemove", onMouseMove);
  window.addEventListener("mouseout", onMouseLeave);
  running = true;
  loop();
});

onBeforeUnmount(() => {
  running = false;
  cancelAnimationFrame(raf);
  window.removeEventListener("resize", resize);
  window.removeEventListener("mousemove", onMouseMove);
  window.removeEventListener("mouseout", onMouseLeave);
});
</script>

<style scoped>
.particle-network {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}
</style>
