<script setup lang="ts">
import { useRouter } from 'vue-router';
import GlassCard from '../../components/common/GlassCard.vue';
import TrendChart from '../../components/health/TrendChart.vue';
import DataInputForm from '../../components/health/DataInputForm.vue';
import { Activity, Heart, Moon, Footprints, MessageSquare } from 'lucide-vue-next';

const router = useRouter();

// 刷新触发器
const refreshTrigger = ref(0);
const todayHealthData = ref<HealthDataResponse[]>([]);

// 获取用户名
const username = ref('User');
onMounted(() => {
  const storedUsername = localStorage.getItem('username');
  if (storedUsername) {
    username.value = storedUsername;
  }
  loadTodayData();
});

// 加载今日数据
const loadTodayData = async () => {
  try {
    const today = new Date().toISOString().split('T')[0];
    const res = await getHealthDataList('', today, today);
    todayHealthData.value = res.data || [];
  } catch (error) {
    console.error('加载今日数据失败:', error);
  }
};

// 刷新数据
const handleDataChange = () => {
  refreshTrigger.value++;
  loadTodayData();
};

const chartData = [1200, 1500, 1100, 1800, 2000, 1600, 2400];
const chartLabels = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

const goToAiChat = () => {
  router.push('/ai-chat');
};
</script>

<template>
  <div class="home-container p-6 md:p-8">
    <!-- 欢迎标题 -->
    <header class="mb-8">
      <div class="flex items-center gap-3 mb-2">
        <div class="w-12 h-12 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center">
          <User class="w-6 h-6 text-white" />
        </div>
        <div>
          <h1 class="text-3xl md:text-4xl font-bold">你好, {{ username }}!</h1>
          <p class="text-gray-400">这是您今天的健康概览</p>
        </div>
      </div>
    </header>

    <!-- 健康提醒 -->
    <HealthAlerts :refresh-trigger="refreshTrigger" />

    <!-- 今日统计卡片 -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
      <GlassCard 
        v-for="stat in stats" 
        :key="stat.label" 
        class="flex flex-col items-center justify-center p-4 md:p-6 hover:bg-white/10 transition-colors group"
      >
        <div :class="['w-10 h-10 rounded-lg flex items-center justify-center mb-3', stat.bgColor]">
          <component :is="stat.icon" class="w-5 h-5 text-white" />
        </div>
        <div class="flex items-baseline gap-1">
          <span class="text-2xl md:text-3xl font-bold">{{ stat.value }}</span>
          <span class="text-sm text-gray-400">{{ stat.unit }}</span>
        </div>
        <span class="text-xs text-gray-400 mt-1">{{ stat.label }}</span>
      </GlassCard>
    </div>

    <!-- 健康指标统计 -->
    <GlassCard class="mb-6 p-6">
      <HealthStats :refresh-trigger="refreshTrigger" />
    </GlassCard>

    <!-- 主内容区 -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- 左侧：趋势图表 -->
      <GlassCard class="lg:col-span-2 p-6">
        <TrendChart :refresh-trigger="refreshTrigger" />
      </GlassCard>

        <GlassCard @click="goToAiChat" class="cursor-pointer">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center">
              <MessageSquare class="w-6 h-6 text-white" />
            </div>
            <div class="flex-1">
              <h2 class="text-xl font-bold text-gray-200">AI 健康助手</h2>
              <p class="text-gray-400 text-sm">智能咨询，个性化建议</p>
            </div>
            <div class="text-indigo-400">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
              </svg>
            </div>
          </div>
        </GlassCard>

        <GlassCard>
          <h2 class="text-xl font-bold mb-6 text-gray-200">Daily Goals</h2>
          <div class="space-y-6">
            <div v-for="i in 3" :key="i" class="space-y-2">
              <div class="flex justify-between text-sm text-gray-400">
                <span>Water Intake</span>
                <span>75%</span>
              </div>
              <div class="w-full bg-white/10 rounded-full h-2">
                <div class="bg-indigo-500 h-2 rounded-full w-3/4"></div>
              </div>
            </div>
          </div>
        </GlassCard>
      </div>
    </div>

    <!-- 底部：数据列表 -->
    <GlassCard class="mt-6 p-6">
      <DataList 
        :refresh-trigger="refreshTrigger" 
        @deleted="handleDataChange"
      />
    </GlassCard>
  </div>
</template>

<style scoped>
.home-container {
  max-width: 1440px;
  margin: 0 auto;
  min-height: 100vh;
}
</style>
