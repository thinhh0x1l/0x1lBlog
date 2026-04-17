<script setup>
</script>
<style scoped>
.gradient-badge {
  position: absolute;
  top: 0;
  right: 0;
  width: 75px;
  height: 75px;
  overflow: hidden;
  z-index: 0;
}

.badge-triangle {
  position: absolute;
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg,
  #ef4444 0%,        /* Red-500 */
  #dc2626 30%,       /* Red-600 */
  #b91c1c 70%,       /* Red-700 */
  #991b1b 100%       /* Red-800 */
  );
  clip-path: polygon(0 0, 100% 0, 100% 100%);
  border-bottom-left-radius: 0.75rem;
  box-shadow: -4px 4px 15px rgba(0, 0, 0, 0.3);
}

.badge-content {
  position: absolute;
  top: 22px;
  right: 10px;
  transform: rotate(45deg);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 12;
}

.badge-text {
  color: white;
  font-weight: 900;
  font-size: 0.95rem;
  text-shadow:
      1px 1px 3px rgba(0, 0, 0, 0.5),
      0 0 10px rgba(255, 255, 255, 0.3);
  letter-spacing: 1.5px;
  margin-bottom: 5px;
}

.badge-shine {
  position: absolute;
  top: 0;
  right: 0;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg,
  rgba(255, 255, 255, 0.6) 0%,
  rgba(255, 255, 255, 0.2) 50%,
  rgba(255, 255, 255, 0) 100%);
  transform: rotate(45deg) translate(-15px, 15px);
  z-index: 11;
  border-radius: 3px;
}


@keyframes shineMove {
  0% {
    transform: rotate(45deg) translate(-20px, 20px);
    opacity: 0.8;
  }
  100% {
    transform: rotate(45deg) translate(10px, -10px);
    opacity: 0.4;
  }
}

.badge-shine {
  animation: shineMove 2.5s ease-in-out infinite alternate;
}


@keyframes gradientPulse {
  0%, 100% {
    opacity: 1;
    background: linear-gradient(135deg, #ef4444 0%, #b91c1c 100%);
  }
  50% {
    opacity: 0.9;
    background: linear-gradient(135deg, #f87171 0%, #dc2626 100%);
  }
}

.gradient-badge:hover .badge-triangle {
  animation: gradientPulse 1.5s ease-in-out infinite;
}

.gradient-badge.gold .badge-triangle {
  background: linear-gradient(135deg,
  #ef4444 0%,        /* Red-500 */
  #dc2626 25%,       /* Red-600 */
  #b91c1c 65%,       /* Red-700 */
  #991b1b 95%
  );
}

.gradient-badge.gold .badge-text {
  color: #ffffff; /* Slate-800 */
  text-shadow: 3px 1px 3px rgba(255, 255, 255, 0.5);
}

</style>

<template>
  <div class="gradient-badge gold"  >
    <div class="badge-triangle"></div>
    <div class="badge-content">
      <span class="badge-text">TOP</span>
    </div>
    <div class="badge-shine"></div>
  </div>
</template>