<template>
  <div class="">
    <!-- Thống kê -->
    <el-row class="stats-row" :gutter="20">
      <el-col :xs="12" :sm="12" :md="6" :lg="6" v-for="stat in statsData" :key="stat.label">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <component :is="stat.icon" :size="48" />
            </div>
            <div class="stat-card__info">
              <div class="stat-card__label">{{ stat.label }}</div>
              <div class="stat-card__value">{{ stat.value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- Biểu đồ -->
    <div class="charts-container">
      <!-- LEFT -->
      <div class="charts-left">
        <!-- Hàng 1 -->
        <div class="charts-row">
          <el-card class="chart-card" shadow="hover">
            <div ref="categoryChartRef" class="chart"></div>
          </el-card>

          <el-card class="chart-card" shadow="hover">
            <div ref="tagChartRef" class="chart"></div>
          </el-card>
        </div>

        <!-- Hàng 2 -->
        <el-card class="chart-card-full" shadow="hover">
          <div ref="visitChartRef" class="chart"></div>
        </el-card>
      </div>

      <!-- RIGHT -->
      <div class="charts-right">
        <el-card class="map-card" shadow="hover">
          <div ref="mapEcharts" class="map-chart"></div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts'
import vietnamGeoJson from '@/assets/gadm41_VNM_1.json'
import { fDashboard } from "@/api/dashboard"
import CommentSvg from "@/components/SvgIcon/CommentSvg"
// import  from "@icons/svg"


// Dữ liệu thống kê
const pv = ref(0)
const uv = ref(0)
const blogCount = ref(0)
const commentCount = ref(0)

const statsData = ref([
  { label: 'Lượt xem hôm nay', value: pv, icon: 'ViewIcon' },
  { label: 'Khách truy cập hôm nay', value: uv, icon: 'UserIcon' },
  { label: 'Số bài viết', value: blogCount, icon: '' },
  { label: 'Số bình luận', value: commentCount, icon: CommentSvg }
])

// Refs
const categoryChartRef = ref(null)
const tagChartRef = ref(null)
const visitChartRef = ref(null)
const mapEcharts = ref(null)

let mapChart = null
let categoryChart = null
let tagChart = null
let visitChart = null

// Colors
const colors = ['#fb7293', '#e062ae', '#32c5e9', '#37a2da', '#96bfff', '#e062ae', '#fb7293', '#ff9f7f', '#ffdb5c', '#e690d1', '#fb7293', '#8378ea', '#ff9f7f', '#37a2da', '#67e0e3', '#9d96f5', '#37a2da', '#32c5e9', '#96bfff', '#ff9f7f', '#ffdb5c', '#9fe6b8', '#32c5e9', '#e7bcf3', '#e690d1', '#ffdb5c', '#9fe6b8', '#8378ea', '#9d96f5', '#e7bcf3', '#e062ae', '#9fe6b8', '#67e0e3']

function createTitle(text) {
  return {
    text,
    left: 'center',
    top: 10,
    textStyle: {
      fontSize: 18,
      color: '#050505',
      fontWeight: 600,
      fontFamily: "Lato, 'Helvetica Neue', Arial, Helvetica, sans-serif"
    }
  }
}

const initCategoryChart = (data) => {
  if (!categoryChartRef.value) return
  categoryChart = echarts.init(categoryChartRef.value)
  categoryChart.setOption({
    title: createTitle('Số lượng bài viết theo danh mục'),
    color: colors.slice(8),
    tooltip: { trigger: "item" },
    legend: { bottom: 0 },
    series: [{
      name: "Bài viết",
      type: "pie",
      radius: [30, 110],
      label: { show: true, color: 'auto' },
      roseType: "area",
      data: data.series
    }]
  })
}

const initTagChart = (data) => {
  if (!tagChartRef.value) return
  tagChart = echarts.init(tagChartRef.value)
  tagChart.setOption({
    color: colors,
    title: createTitle('Số lượng bài viết theo thẻ'),
    tooltip: { trigger: "item" },
    legend: { bottom: 0 },
    series: [{
      name: "Bài viết",
      type: "pie",
      radius: [30, 110],
      roseType: "area",
      label: { show: true, color: 'auto' },
      data: data.series
    }]
  })
}

const loadDashboard = async () => {
  // Dummy data for stats
  pv.value = 12847
  uv.value = 3421
  blogCount.value = 1256
  commentCount.value = 8934

  // Dummy data for category chart
  const categoryData = {
    series: [
      { name: 'Công nghệ', value: 312 },
      { name: 'Lập trình', value: 256 },
      { name: 'Web Development', value: 198 },
      { name: 'DevOps', value: 145 },
      { name: 'AI & ML', value: 128 },
      { name: 'Database', value: 98 },
      { name: 'Security', value: 87 },
      { name: 'UI/UX', value: 76 },
      { name: 'Mobile', value: 65 },
      { name: 'Cloud', value: 54 },
      { name: 'Game Dev', value: 43 },
      { name: 'Blockchain', value: 32 },
      { name: 'IoT', value: 21 },
      { name: 'Data Science', value: 18 },
      { name: 'Career', value: 12 },
    ].sort((a,b) => Math.random() - 0.5)
  }

  // Dummy data for tag chart
  const tagData = {
    series: [
      { name: 'javascript', value: 456 },
      { name: 'springboot', value: 389 },
      { name: 'vuejs', value: 345 },
      { name: 'typescript', value: 298 },
      { name: 'postgresql', value: 267 },
      { name: 'docker', value: 234 },
      { name: 'redis', value: 198 },
      { name: 'kubernetes', value: 176 },
      { name: 'react', value: 165 },
      { name: 'nodejs', value: 154 },
      { name: 'python', value: 143 },
      { name: 'aws', value: 132 },
      { name: 'graphql', value: 121 },
      { name: 'microservices', value: 110 },
      { name: 'java', value: 98 },
    ].sort((a,b) => Math.random() - 0.5)
  }

  // Dummy data for visit chart (last 7 days)
  const visitData = {
    dates: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
    pv: [1200, 1350, 1100, 1450, 1280, 1650, 1400],
    uv: [320, 380, 290, 420, 350, 480, 410],
  }

  initCategoryChart(categoryData)
  initTagChart(tagData)
  // initVisitChart(visitData) // nếu có visit chart
}

const handleResize = () => {
  mapChart?.resize()
  categoryChart?.resize()
  tagChart?.resize()
  visitChart?.resize()
}

const initMap = () => {
  if (!mapEcharts.value) return
  echarts.registerMap('vietnam', vietnamGeoJson)
  mapChart = echarts.init(mapEcharts.value)
  mapChart.setOption(mapOption)
  window.addEventListener('resize', handleResize)
}

// Map data
const cityVisitor = [
  { name: 'Hà Nội', value: 1200 },
  { name: 'Hồ Chí Minh', value: 1800 },
  { name: 'Đà Nẵng', value: 800 },
  { name: 'Hải Phòng', value: 450 },
  { name: 'Cần Thơ', value: 320 }
]

const scatterData = [
  { name: 'Hà Nội', value: [105.834, 21.027], uv: 1200 },
  { name: 'Hồ Chí Minh', value: [106.629, 10.823], uv: 1800 },
  { name: 'Hồ Chí Minh', value: [106.6257, 10.822], uv: 1800 },
  { name: 'Đà Nẵng', value: [108.221, 16.048], uv: 800 },
  { name: 'Hải Phòng', value: [106.691, 20.848], uv: 450 },
  { name: 'Cần Thơ', value: [105.748, 10.045], uv: 320 },
  { name: 'An Giang', value: [105.1, 10.5], uv: 150 },
  { name: 'Bắc Giang', value: [106.2, 21.3], uv: 150 },
  { name: 'Bắc Kạn', value: [105.8, 22.1], uv: 150 },
  { name: 'Bạc Liêu', value: [105.7, 9.3], uv: 150 },
  { name: 'Bắc Ninh', value: [106.1, 21.2], uv: 150 },
  { name: 'Bến Tre', value: [106.4, 10.2], uv: 150 },
  { name: 'Bình Định', value: [109.1, 13.8], uv: 150 },
  { name: 'Bình Dương', value: [106.7, 11.1], uv: 280 },
  { name: 'Bình Phước', value: [106.9, 11.6], uv: 150 },
  { name: 'Bình Thuận', value: [108.1, 11.1], uv: 150 },
  { name: 'Cà Mau', value: [105.0, 9.0], uv: 150 },
  { name: 'Cao Bằng', value: [106.3, 22.7], uv: 150 },
  { name: 'Đắk Lắk', value: [108.1, 12.7], uv: 150 },
  { name: 'Đắk Nông', value: [107.7, 12.2], uv: 150 },
  { name: 'Điện Biên', value: [103.0, 21.4], uv: 150 },
  { name: 'Đồng Nai', value: [107.2, 11.1], uv: 150 },
  { name: 'Đồng Nai', value: [107.3, 11.1], uv: 150 },
  { name: 'Đồng Tháp', value: [105.6, 10.5], uv: 150 },
  { name: 'Gia Lai', value: [108.0, 13.8], uv: 150 },
  { name: 'Hà Giang', value: [105.0, 22.8], uv: 150 },
  { name: 'Hà Nam', value: [106.0, 20.5], uv: 150 },
  { name: 'Hà Tĩnh', value: [105.9, 18.3], uv: 150 },
  { name: 'Hải Dương', value: [106.3, 20.9], uv: 150 },
  { name: 'Hậu Giang', value: [105.8, 9.7], uv: 150 },
  { name: 'Hòa Bình', value: [105.3, 20.8], uv: 150 },
  { name: 'Hưng Yên', value: [106.0, 20.7], uv: 150 },
  { name: 'Khánh Hòa', value: [109.2, 12.2], uv: 150 },
  { name: 'Kiên Giang', value: [105.0, 9.8], uv: 150 },
  { name: 'Kon Tum', value: [107.8, 14.3], uv: 150 },
  { name: 'Lai Châu', value: [103.5, 22.4], uv: 150 },
  { name: 'Lâm Đồng', value: [108.5, 11.9], uv: 150 },
  { name: 'Lạng Sơn', value: [106.8, 21.8], uv: 150 },
  { name: 'Lào Cai', value: [104.0, 22.5], uv: 150 },
  { name: 'Long An', value: [106.2, 10.5], uv: 150 },
  { name: 'Nam Định', value: [106.2, 20.4], uv: 150 },
  { name: 'Nghệ An', value: [105.7, 19.2], uv: 150 },
  { name: 'Ninh Bình', value: [105.9, 20.2], uv: 150 },
  { name: 'Ninh Thuận', value: [108.9, 11.6], uv: 150 },
  { name: 'Phú Thọ', value: [105.3, 21.3], uv: 150 },
  { name: 'Phú Yên', value: [109.1, 13.1], uv: 150 },
  { name: 'Quảng Bình', value: [106.6, 17.8], uv: 150 },
  { name: 'Quảng Nam', value: [108.3, 15.6], uv: 150 },
  { name: 'Quảng Ngãi', value: [108.7, 15.1], uv: 150 },
  { name: 'Quảng Ninh', value: [107.3, 21.0], uv: 150 },
  { name: 'Quảng Trị', value: [107.2, 16.7], uv: 150 },
  { name: 'Sóc Trăng', value: [105.9, 9.6], uv: 150 },
  { name: 'Sơn La', value: [104.1, 21.3], uv: 150 },
  { name: 'Tây Ninh', value: [106.1, 11.3], uv: 150 },
  { name: 'Thái Bình', value: [106.3, 20.4], uv: 150 },
  { name: 'Thái Nguyên', value: [105.8, 21.6], uv: 150 },
  { name: 'Thanh Hóa', value: [105.8, 19.8], uv: 150 },
  { name: 'Thừa Thiên Huế', value: [107.6, 16.4], uv: 150 },
  { name: 'Tiền Giang', value: [106.3, 10.4], uv: 150 },
  { name: 'Trà Vinh', value: [106.3, 9.9], uv: 150 },
  { name: 'Tuyên Quang', value: [105.2, 21.8], uv: 150 },
  { name: 'Vĩnh Long', value: [106.0, 10.2], uv: 150 },
  { name: 'Vĩnh Phúc', value: [105.6, 21.3], uv: 150 },
  { name: 'Yên Bái', value: [104.9, 21.7], uv: 150 },
  { name: 'Yên Bái', value: [107.0737, 10.6377], uv: 150 }
];

const mapOption = {
  title: createTitle('Bản đồ Việt Nam'),
  tooltip: {
    trigger: 'item',
    formatter: (params) => {
      if (params.seriesType === 'scatter') {
        return `${params.data.name}<br/>Khách: ${params.data.uv}`
      }
      return `${params.name}<br/>Giá trị: ${params.value || 0}`
    }
  },
  geo: {
    map: 'vietnam',
    roam: true,
    zoom: 1.8,
    bottom: 0,
    left: 80,
    center: [108.3, 15.5],
    nameProperty: 'ten_tinh',
    label: { show: false },
    itemStyle: {
      areaColor: "#0d0059",
      borderColor: "#389dff",
      borderWidth: 1,
      shadowBlur: 5,
      shadowOffsetY: 8,
      shadowColor: "#01012a"
    },
    emphasis: {
      itemStyle: {
        areaColor: "#17008d",
        shadowBlur: 5,
        borderWidth: 1
      }
    }
  },
  series: [
    {
      type: 'scatter',
      coordinateSystem: "geo",
      data: scatterData,
      symbol: "circle",
      symbolSize: 4,
      hoverSymbolSize: 20,
      label: {
        formatter: "{b}",
        position: "right",
        show: false,
        color: "#0efacc",
        fontSize: 11,
        fontWeight: "bold"
      },
      itemStyle: {
        color: "#0efacc",
        borderColor: "#fff",
        borderWidth: 1
      }
    },
    {
      type: 'effectScatter',
      coordinateSystem: 'geo',
      data: scatterData.slice(0, 5),
      symbol: 'circle',
      symbolSize: 8,
      showEffectOn: 'render',
      rippleEffect: {
        brushType: 'stroke',
        scale: 4,
        period: 4
      },
      hoverAnimation: true,
      label: {
        formatter: '{b}',
        position: 'right',
        show: false,
        color: '#fff'
      },
      emphasis: {
        label: { show: true },
        itemStyle: { color: '#00ffff' }
      },
      itemStyle: {
        color: '#0efacc',
        shadowBlur: 15,
        shadowColor: '#0efacc'
      },
      zlevel: 2
    }
  ]
}

onMounted(() => {
  loadDashboard()
  initMap()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  mapChart?.dispose()
  categoryChart?.dispose()
  tagChart?.dispose()
  visitChart?.dispose()
})
</script>

<style scoped>
/* Dashboard Container */
.dashboard-container {
  //padding: 20px;
  background: #fffbfb;
  min-height: 100vh;
}

/* Stats Row */
.stats-row {
  margin-bottom: 24px;
}

/* Stat Card */
.stat-card {
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;
  height: 108px;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-card__content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-card__icon {
  flex-shrink: 0;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(255, 107, 107, 0.1), rgba(77, 171, 247, 0.1));
  border-radius: 12px;
  margin-right: 16px;
}

.stat-card__icon :deep(svg) {
  width: 32px;
  height: 32px;
}

.stat-card__info {
  flex: 1;
}

.stat-card__label {
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
  margin-bottom: 8px;
  font-weight: 500;
}

.stat-card__value {
  font-size: 24px;
  font-weight: 700;
  color: #1a1a1a;
  line-height: 1.2;
}

/* Charts Container */
.charts-container {
  display: flex;
  gap: 20px;
}

.charts-left {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.charts-row {
  display: flex;
  gap: 20px;
}

.chart-card {
  flex: 1;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.chart-card-full {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.charts-right {
  flex: 1;
}

.map-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
  height: 100%;
}

.chart {
  height: 500px;
  width: 100%;
}

.map-chart {
  height: 1020px;
  width: 100%;
}

/* Responsive */
@media (max-width: 1200px) {
  .chart {
    height: 380px;
  }

  .map-chart {
    height: 1020px;
  }
}

@media (max-width: 992px) {
  .dashboard-container {
    padding: 16px;
  }

  .charts-container {
    flex-direction: column;
  }

  .charts-left {
    width: 100%;
  }

  .charts-right {
    width: 100%;
  }

  .chart {
    height: 350px;
  }

  .map-chart {
    height: 500px;
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 12px;
  }

  .stats-row {
    margin-bottom: 16px;
  }

  .charts-row {
    flex-direction: column;
    gap: 16px;
  }

  .chart {
    height: 300px;
  }

  .map-chart {
    height: 400px;
  }

  .stat-card {
    height: 96px;
  }

  .stat-card__icon {
    width: 48px;
    height: 48px;
  }

  .stat-card__icon :deep(svg) {
    width: 24px;
    height: 24px;
  }

  .stat-card__value {
    font-size: 20px;
  }
}

@media (max-width: 576px) {
  .dashboard-container {
    padding: 8px;
  }
  .stat-card__icon {
    width: 20px;
    height: 20px;
  }

  .stat-card__label {
    font-size: 12px;
  }

  .stat-card__value {
    font-size: 18px;
  }

  .chart {
    height: 400px;
  }

  .map-chart {
    height: 650px;
  }
}

/* Loading animation */
.stat-card {
  position: relative;
  overflow: hidden;
}

.stat-card::after {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s ease;
}

.stat-card:hover::after {
  left: 100%;
}

/* Scrollbar styling */
.chart::-webkit-scrollbar,
.map-chart::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.chart::-webkit-scrollbar-track,
.map-chart::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.chart::-webkit-scrollbar-thumb,
.map-chart::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

.chart::-webkit-scrollbar-thumb:hover,
.map-chart::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>