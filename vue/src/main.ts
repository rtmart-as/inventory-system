import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPersist from 'pinia-plugin-persistedstate'
//echarts
import * as echarts from 'echarts'

import myconfirm from './utils/myconfirm'
import { userStore } from './stores/user'
import App from './App.vue'
import router from './router'
// 引入elementplus
import ElementPlus from 'element-plus'
//国际化
import zhCn from 'element-plus/es/locale/lang/zh-cn'
// 引入样式
import 'element-plus/dist/index.css'
// 引入element-plus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//权限验证
import './permisson'


const app = createApp(App)
const pinia = createPinia()

app.use(createPinia())
app.use(router)
// 使用ElementPlus,使用国际化
app.use(ElementPlus,{ locale: zhCn})
pinia.use(piniaPersist)
// 全局挂载
app.config.globalProperties.$myconfirm = myconfirm;
// 权限判断：当前用户是否拥有权限码列表中任一权限
app.config.globalProperties.$hasPerm = (perms: string[]) => {
  const store = userStore()
  const codeList = store.getCodeList
  return perms.some(p => codeList.includes(p))
}

app.mount('#app')
// 遍历图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 全局挂载
app.config.globalProperties.$myconfirm = myconfirm;
app.config.globalProperties.$echarts = echarts;
