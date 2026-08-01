<template>
    <canvas ref="canvasRef" class="universe"/>
</template>
<script setup>
const canvasRef = ref(null)

let canvas = null
let ctx = null

let screenWidth = 0
let screenHeight = 0

let stars = []
let animationId = null

const STAR_COLOR = '120,180,255'
const COMET_COLOR = '255,255,255'
const GIANT_COLOR = '180,220,255'
//
// const STAR_COLOR = '226,225,142'
// const GIANT_COLOR = '180,184,240'
// const COMET_COLOR = '226,225,224'

const BASE_SPEED = 0.05

let firstRender = true

class Star {
  constructor() {
    this.reset()
  }

  reset() {
    this.isGiant = randomChance(5)

    this.isComet =
        !this.isGiant &&
        !firstRender &&
        randomChance(10)

    this.x = random(0, screenWidth - 10)
    this.y = random(0, screenHeight)

    this.radius = random(1.1, 2.6)

    this.dx =
        random(BASE_SPEED, BASE_SPEED * 6) +
        (this.isComet ? BASE_SPEED * random(50, 120) : 0) +
        0.1

    this.dy =
        -random(BASE_SPEED, BASE_SPEED * 6) -
        (this.isComet ? BASE_SPEED * random(50, 120) : 0)

    this.opacity = 0

    this.fadeIn = true
    this.fadeOut = false

    this.opacityThreshold =
        random(
            0.2,
            1 - (this.isComet ? 0.4 : 0)
        )

    this.opacitySpeed =
        random(0.0005, 0.002) +
        (this.isComet ? 0.001 : 0)
  }

  update() {
    this.move()
    this.handleFadeIn()
    this.handleFadeOut()
  }

  move() {
    this.x += this.dx
    this.y += this.dy

    if (
        this.x > screenWidth - screenWidth / 4 ||
        this.y < 0
    ) {
      this.fadeOut = true
    }

    if (
        this.x > screenWidth ||
        this.y < 0
    ) {
      this.reset()
    }
  }

  handleFadeIn() {
    if (!this.fadeIn) return

    this.opacity += this.opacitySpeed

    if (
        this.opacity >
        this.opacityThreshold
    ) {
      this.fadeIn = false
    }
  }

  handleFadeOut() {
    if (!this.fadeOut) return

    this.opacity -= this.opacitySpeed / 2

    if (this.opacity <= 0) {
      this.reset()
    }
  }

  draw() {
    ctx.beginPath()

    if (this.isGiant) {
      this.drawGiant()
    } else if (this.isComet) {
      this.drawComet()
    } else {
      this.drawStar()
    }

    ctx.closePath()
    ctx.fill()
  }

  drawStar() {
    ctx.fillStyle =
        `rgba(${STAR_COLOR},${this.opacity})`

    ctx.fillRect(
        this.x,
        this.y,
        this.radius,
        this.radius
    )
  }

  drawGiant() {
    ctx.fillStyle =
        `rgba(${GIANT_COLOR},${this.opacity})`

    ctx.arc(
        this.x,
        this.y,
        2,
        0,
        Math.PI * 2,
        false
    )
  }

  drawComet() {
    ctx.fillStyle =
        `rgba(${COMET_COLOR},${this.opacity})`

    ctx.arc(
        this.x,
        this.y,
        1.5,
        0,
        Math.PI * 2,
        false
    )

    for (let i = 0; i < 30; i++) {
      ctx.fillStyle =
          `rgba(${COMET_COLOR},${
              this.opacity -
              (this.opacity / 20) * i
          })`

      ctx.fillRect(
          this.x - (this.dx / 4) * i,
          this.y - (this.dy / 4) * i - 2,
          2,
          2
      )
    }
  }
}

function random(min, max) {
  return Math.random() * (max - min) + min
}

function randomChance(percent) {
  return (
      Math.floor(Math.random() * 1000) + 1 <
      percent * 10
  )
}

function resizeCanvas() {
  screenWidth = window.innerWidth
  screenHeight = window.innerHeight

  canvas.width = screenWidth
  canvas.height = screenHeight
}

function createStars() {
  stars = []

  const starCount = Math.floor(
      screenWidth * 0.216
  )

  for (let i = 0; i < starCount; i++) {
    stars.push(new Star())
  }
}

function render() {
  ctx.clearRect(
      0,
      0,
      screenWidth,
      screenHeight
  )

  for (const star of stars) {
    star.update()
    star.draw()
  }
}

function animate() {
  render()

  animationId =
      requestAnimationFrame(animate)
}

function initUniverse() {
  canvas = canvasRef.value

  if (!canvas) return

  ctx = canvas.getContext('2d')

  resizeCanvas()
  createStars()

  animate()

  setTimeout(() => {
    firstRender = false
  }, 50)
}
function renderBackground() {
  const gradient =
      ctx.createLinearGradient(
          0,
          0,
          0,
          screenHeight
      )

  gradient.addColorStop(
      0,
      '#050816'
  )

  gradient.addColorStop(
      0,
      '#000000'
  )

  ctx.fillStyle = gradient

  ctx.fillRect(
      0,
      0,
      screenWidth,
      screenHeight
  )
}
onMounted(() => {
  initUniverse()

  window.addEventListener(
      'resize',
      resizeCanvas
  )
})

onUnmounted(async() => {
  cancelAnimationFrame(animationId)
  window.removeEventListener(
      'resize',
      resizeCanvas
  )
})
</script>

<style scoped>
.universe {
  display: block;
  position: fixed;
  margin: 0;
  padding: 0;
  border: 0;
  outline: 0;
  left: -50px;
  top: 50px;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 2000;
}

</style>